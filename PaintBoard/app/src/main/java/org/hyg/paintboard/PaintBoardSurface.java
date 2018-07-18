package org.hyg.paintboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Stack;

/**
 * Created by shiny on 2018-03-21.
 * 서피스뷰는 하나의 뷰이고 이 서피스뷰를 내부적으로 제어하는 것이 서피스홀더이다.
 *  SurfaceView : 안드로이드 앱에서 3D 객체를 보여주려면 하드웨어 가속이 가능한 서피스뷰를 사용해야 함
 *   - SurfaceView 를 상속하면서 SurfaceHolder.Callback 인터페이스를 구현하는 클래스
 *   - SurfaceHolder.Callback : 서비스뷰의 상태 정보 관리(상태정보가 변할때 자동 호출됨)
 */

public class PaintBoardSurface
        extends SurfaceView
        implements SurfaceHolder.Callback{




    /**
     * [1] 서피스뷰의 제어 기능 객체 인스턴스
     */
    private SurfaceHolder mHolder;


    private Canvas mCanvas;
    private Bitmap mBitmap;
    public void setImage(int width, int height){
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bmp);

        mBitmap = bmp;
        mCanvas = canvas;

        drawBackground(mCanvas);

        mIsChanged = false;

        // TODO : [SF] invalidate() 대신 draw() 호출
        redraw();
    }

    public void setImage(Bitmap bmpNew){
        mIsChanged = false;
        setImage(bmpNew.getWidth(), bmpNew.getHeight(), bmpNew);

        // TODO : [SF] invalidate() 대신 draw() 호출
        redraw();
    }

    public void setImage(int width, int height, Bitmap bmpNew){
        if(mBitmap != null){
            if(width < mBitmap.getWidth()) { width = mBitmap.getWidth(); }
            if(height < mBitmap.getHeight()) { height = mBitmap.getHeight(); }
        }

        if(width < 1 || height < 1) { return; }

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        drawBackground(canvas);

        if(bmpNew != null){
            canvas.setBitmap(bmpNew);
        }

        if(mBitmap != null){
            mBitmap.recycle();
            mCanvas.restore();
        }

        mBitmap = bmp;
        mCanvas = canvas;

        clearUndo();
    }

    private float mLastX, mLastY;
    private final Paint mPaint = new Paint();
    public void setPaint(int color, int size){
        mPaint.setColor(color);
        mPaint.setStrokeWidth(size);
    }

    /**
     * 터치 이벤트 지점의 좌표 저장
     */
    private final Path mPath = new Path();
    private float mCurtX, mCurtY;

    private final float TOUCH_TOLERANCE = 8f;
    public static final int MAX_UNDO = 10;
    private static final boolean RENDERING_ANTIALIAS = true;
    // 디더링(색보정)
    private static final boolean DITHER_FLAG = true;

    // UNDO 히스토리
    private Stack mUndoHistory = new Stack();
    private boolean mIsChanged = false;
    public boolean getIsChanged() { return mIsChanged; }

    private final int INVALID_EXTRA_BORDER = 8;
    private int mCertainColor = 0xff000000;
    private float mStrokeWidth = 2.0f;



    public PaintBoardSurface(Context context) {
        super(context);

        initHolder();
        init();
    }

    public PaintBoardSurface(Context context, AttributeSet attrs) {
        super(context, attrs);

        initHolder();
        init();
    }

    private void init() {
        mPaint.setAntiAlias(RENDERING_ANTIALIAS);
        mPaint.setColor(mCertainColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setDither(DITHER_FLAG);

        mLastX = mLastY = -1;
    }


    /**
     * 작업 내역을 마지막부터 역순으로 하나씩 취소 (비트맵 삭제)
     */
    public void clearUndo(){
        while(true){
            Bitmap bmp = (Bitmap)mUndoHistory.pop();
            if(bmp == null) { return; }

            bmp.recycle();
        }
    }

    /**
     *
     */
    public void saveUndo() {
        if (mBitmap == null) { return; }

        Bitmap bmp;
        while(mUndoHistory.size() >= MAX_UNDO){
            bmp = (Bitmap)mUndoHistory.get(mUndoHistory.size() - 1);
            bmp.recycle();
            mUndoHistory.remove(bmp);
        }

        bmp = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bmp);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

        mUndoHistory.push(bmp);
    }

    public void undo(){
        Bitmap bmp = null;

        try{
            bmp = (Bitmap)mUndoHistory.pop();
        } catch (Exception ex) {
            Log.e("BestPaintBoard View", "Exception: " + ex.getMessage());
        }

        if(bmp != null){
            drawBackground(mCanvas);
            mCanvas.drawBitmap(bmp, 0, 0, mPaint);

            // TODO: [SF] invalidate() 대신 redraw() 메서드 호출
            redraw();

            bmp.recycle();
        }
    }

    /**
     * 배경색을 흰색으로 다시 채움
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        if(canvas != null){
            canvas.drawColor(Color.WHITE);
        }
    }


    /**
     * [그리기 작업 불가]
     *  SurfaceView 에서는 onDraw() 와 invalidate() 메서드 사용 불가.
     *   => onDraw() 대신 사용자 정의 메서드 정의 (예. draw() )
     *      invalidate() 대신 사용자 정의 메서더 정의 (예. redraw() )
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * [SF] onDraw() 메서드를 대체하는 그리기 사용자 정의 함수
     */
    private void draw() {
        Rect rect = new Rect();
        rect.set(0, 0, 0, 0);
        draw(rect);
    }

    private void draw(@Nullable Rect rect) {
        Canvas cvsLock = null;

        try{
            // TODO : [SF-1] 캔바스 잠금
            cvsLock = mHolder.lockCanvas(null);


            // TODO : [SF-2] onDraw() 의 작업
            super.draw(cvsLock);

            if (rect != null && rect.width() > 0 && rect.height() > 0) { cvsLock.drawRect(rect, mPaint); }
            if(mBitmap != null) {
                cvsLock.drawBitmap(mBitmap, 0, 0, null);
            }

        } catch (Exception ex) {
            if (cvsLock != null) {
                // TODO : [SF-Option]
            }
        } finally {
            // TODO: [SF-3] 캔바스 잠금 해제
            if (cvsLock != null) {
                mHolder.unlockCanvasAndPost(cvsLock);
            }
        }
    }


    private void redraw(@Nullable Rect rect) {
        draw(rect);
    }

    /**
     * [SF] invalidate() 메서드를 대체하는 다시 그리기 사용자 정의 함수
     */
    private void redraw() {
        draw();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Rect rect;

        switch (action){
            case MotionEvent.ACTION_DOWN:
                saveUndo();

                rect = touchDown(event);
                if(rect != null) { redraw(rect); }

                break;

            case MotionEvent.ACTION_MOVE:
                rect = touchMove(event);
                if(rect != null) { redraw(rect); }

                break;

            case MotionEvent.ACTION_UP:
                mIsChanged = true;

                rect = touchUp(event, false);
                if(rect != null) { redraw(rect); }

                mPath.rewind();

                break;
        }

        return true;
    }

    private Rect touchMove(MotionEvent event) {
        Rect rect = processSmoothMove(event);

        return rect;
    }

    private Rect touchDown(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        mLastX = x;
        mLastY = y;

        Rect rect = new Rect();
        mPath.moveTo(x, y);

        rect.set((int)x - INVALID_EXTRA_BORDER
                , (int)y - INVALID_EXTRA_BORDER
                , (int)x + INVALID_EXTRA_BORDER
                , (int)y + INVALID_EXTRA_BORDER);
        mCurtX = x;
        mCurtY = y;

        mCanvas.drawPath(mPath, mPaint);

        return rect;
    }

    private Rect touchUp(MotionEvent event, boolean cancel) {
        Rect rect = processSmoothMove(event);

        return rect;
    }

    /**
     * 부드러운 곡선처리
     * @param event
     * @return
     */
    private Rect processSmoothMove(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        final float dx = Math.abs(x - mLastX);
        final float dy = Math.abs(y - mLastY);

        Rect rect = new Rect();
        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            rect.set((int)mCurtX - INVALID_EXTRA_BORDER
                    , (int)mCurtY - INVALID_EXTRA_BORDER
                    , (int)mCurtX + INVALID_EXTRA_BORDER
                    , (int)mCurtY + INVALID_EXTRA_BORDER);

            float cx = mCurtX = (x + mLastX) / 2;
            float cy = mCurtY = (y + mLastY) / 2;

            // curve
            mPath.quadTo(mLastX, mLastY, cx, cy);

            /**/
            rect.union((int)mLastX - INVALID_EXTRA_BORDER
                    , (int)mLastY - INVALID_EXTRA_BORDER
                    , (int)mLastX + INVALID_EXTRA_BORDER
                    , (int)mLastY + INVALID_EXTRA_BORDER);

            rect.union((int)cx - INVALID_EXTRA_BORDER
                    , (int)cy - INVALID_EXTRA_BORDER
                    , (int)cx + INVALID_EXTRA_BORDER
                    , (int)cy + INVALID_EXTRA_BORDER);


            mLastX = x;
            mLastY = y;

            mCanvas.drawPath(mPath, mPaint);
        }

        return rect;
    }


    /**
     * SurfaceView 의 상태 변화는 surfaceChanged() 메서드가 대신함
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /* surfaceChanged 메서드에서 처리함
        if(w > 0 && h > 0){
            setImage(w, h);
        }
        */
    }

    /**
     * [2] SurfaceHolder 의 객체 참조와 Callback 인터페이스 설정
     */
    private void initHolder() {
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    /**
     * [3] Callback 인터페이스의 surfaceCreate() 메서드 구현
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    /**
     * [4] Callback 인터페이스의 surfaceChanged() 메서드 구현
     *  : onSizeChanged 이벤트를 대신함
     * @param surfaceHolder
     * @param i
     * @param w
     * @param h
     */
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int w, int h) {
        // TODO: [SF]
        if(w > 0 && h > 0){
            setImage(w, h);
        }
    }

    /**
     * [5] Callback 인터페이스의 surfaceDestroyed() 메서드 구현
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // TODO: [SF]
    }
}

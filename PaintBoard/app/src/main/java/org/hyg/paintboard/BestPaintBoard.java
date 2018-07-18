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
import android.view.View;

import java.io.OutputStream;
import java.util.Stack;

/**
 * Created by shiny on 2018-03-21.
 */

public class BestPaintBoard extends View {

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

        invalidate();
    }

    public void setImage(Bitmap bmpNew){
        mIsChanged = false;
        setImage(bmpNew.getWidth(), bmpNew.getHeight(), bmpNew);
        invalidate();
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


    public BestPaintBoard(Context context){
        super(context);

        init();
    }

    public BestPaintBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

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

            invalidate();

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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if(w > 0 && h > 0){
            setImage(w, h);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    /**
     *
     *  : Path.rewind() - 라인, 커브는 지우고, 내부정보는 재사용
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Rect rect;

        switch (action){
            case MotionEvent.ACTION_DOWN:
                saveUndo();

                rect = touchDown(event);
                if(rect != null) { invalidate(rect); }

                break;

            case MotionEvent.ACTION_MOVE:
                rect = touchMove(event);
                if(rect != null) { invalidate(rect); }

                break;

            case MotionEvent.ACTION_UP:
                mIsChanged = true;

                rect = touchUp(event, false);
                if(rect != null) { invalidate(rect); }

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
     * 페인트보드의 비트맵을 JPEG 파일 형식으로 저장
     * @param stream
     * @return
     */
    public boolean saveJPEG(OutputStream stream){
        try{
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            invalidate();
        } catch (Exception ex) {
            return false;
        }

        return true;
    }
}

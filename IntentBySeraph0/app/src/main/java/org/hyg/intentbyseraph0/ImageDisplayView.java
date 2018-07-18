package org.hyg.intentbyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by shiny on 2018-03-20.
 */

public class ImageDisplayView extends View implements View.OnTouchListener {

    // 화면 넓이, 높이
    private int mWidth, mHeight;

    private final int SINGLE_TOUCH = 1;
    private final int MULTI_TOUCH = 2;

    private Context mContext;
    private Paint mPaint;
    private Matrix mMatrix;
    // 첫번째 손가락 터치지점의 좌표
    private float mStartX, mStartY;
    // 두번째 손가락 터치지점의 좌표
    private float mLastX, mLastY;
    private float mOldDistance, mOldPointerCount, mTotalScaleRatio, mOutScaleRatio;
    private boolean mIsScrolling;
    private Bitmap mBitmap;
    public void setImageData(Bitmap bmp) {
        mBitmap = bmp;
        mBmpW = bmp.getWidth() * 2;
        mBmpH = bmp.getHeight() * 2;
        mBmpX = mBmpW / 2;
        mBmpY = mBmpH / 2;
    }
    // 비트맵 이미지의 중심좌표
    private float mBmpX, mBmpY;
    private int mBmpW, mBmpH;

    public ImageDisplayView(Context context){
        super(context);

        mContext = context;
        init();
    }

    public ImageDisplayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mMatrix = new Matrix();

        mLastX = mLastY = -1;

        setOnTouchListener(this);
    }

    /**
     * 뷰가 화면에 보이기 전에 비트맵 이미지 초기화
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        if(w > 0 && h > 0){
            createBitmap(w, h);
            redraw();
        }
    }

    /**
     * 더블 버퍼링 방식으로 이미지 그리기 위해 먼저, 메모리에 비트맵 이미지 생성
     * @param w : 비트맵 이미지 넓이
     * @param h : 비트맵 이미지 높이
     */
    private void createBitmap(int w, int h) {

        mBmpX = w / 2;
        mBmpY = h / 2;
    }

    private void redraw() {
        // TODO:
        invalidate();
    }

    /**
     * 더블 버퍼링 방식으로 이미지 그리기
     *  : 메모리에 비트맵 이미지를 만들어 두고(createBitmap 메서드), 그 이미지에 미리 그린 후 화면에 보여주는 방식
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    /**
     * 터치 이벤트에 따른 이미지 변형
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();

        int pointerCount = motionEvent.getPointerCount();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(pointerCount == SINGLE_TOUCH){
                    float curX = motionEvent.getX();
                    float curY = motionEvent.getY();
                    mStartX = curX;
                    mStartY = curY;
                } else if (pointerCount == MULTI_TOUCH) {
                    mOldDistance = 0.0f;
                    mIsScrolling = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if(pointerCount == SINGLE_TOUCH){
                    if(mIsScrolling) { return true; }

                    float curX = motionEvent.getX();
                    float curY = motionEvent.getY();

                    if(mStartX == 0.0f){
                        mStartX = curX;
                        mStartY = curY;

                        return true;
                    }

                    float offsetX = mStartX - curX;
                    float offsetY = mStartY - curY;

                    if(mOldPointerCount != MULTI_TOUCH){
                        if(mTotalScaleRatio > 1.0f){
                            moveImage(-offsetX, -offsetY);
                        }

                        mStartX = curX;
                        mStartY = curY;
                    }
                } else if(pointerCount == MULTI_TOUCH){
                    scaleImage(mOutScaleRatio);
                }

                mOldPointerCount = pointerCount;
                break;

            case MotionEvent.ACTION_UP:
                if(pointerCount == SINGLE_TOUCH){
                    float curX = motionEvent.getX();
                    float curY = motionEvent.getY();
                    float offsetX = mStartX - curX;
                    float offsetY = mStartY - curY;

                    if(mOldPointerCount != MULTI_TOUCH){
                        moveImage(-offsetX, -offsetY);
                    }
                } else {
                    mIsScrolling = false;
                }

                break;
        }

        return true;
    }

    /**
     * 한 손가락으로 터치하여 움직이고 있을 때, 이미지 이동
     * @param offsetX : 이미지 X축 이동 픽셀
     * @param offsetY : 이미지 Y축 이동 픽셀
     */
    private void moveImage(float offsetX, float offsetY) {
        mMatrix.postTranslate(offsetX, offsetY);

        redraw();
    }


    /**
     * 두 손가락으로 터치하여 움직이고 있을 때, 이미지 크기 조절
     * @param scaleRatio : 이미지 크기 X/Y축 조절 비율
     */
    private void scaleImage(float scaleRatio) {
        mMatrix.postScale(scaleRatio, scaleRatio, mBmpW, mBmpH );
        mMatrix.postRotate(0);
        mTotalScaleRatio *= scaleRatio;

        redraw();
    }

}

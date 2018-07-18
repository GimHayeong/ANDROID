package org.hyg.paintboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.OutputStream;

/**
 * Created by shiny on 2018-03-21.
 * PaintBoardActivity 에서 보여질 뷰
 */

public class PaintBoard extends View {

    private Canvas mCanvas;
    private Bitmap mBitmap;
    private int mLastX, mLastY;
    private final Paint mPaint = new Paint();

    public PaintBoard(Context context) {
        super(context);

        init();
    }

    private void init() {
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1.6f);
        mPaint.setStyle(Paint.Style.STROKE);
        mLastX = mLastY = -1;
    }

    /**
     * 화면 뷰와 메모리상의 비트맵 이미지 크기를 동일하게 하기 위해
     * 사이즈 변경시 비트맵이미지 새로 생성하여
     * 캔바스에 그려질 비트맵으로 메모리상의 비트맵이미지 설정
     *  : 캔바스 컬러를 먼저 WHITE로 설정하고 비트맵이미지 설정
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.drawColor(Color.WHITE);
        mCanvas.setBitmap(mBitmap);
    }

    /**
     * 더블 버퍼링 기법
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmap != null){
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();
        Log.d("TOUCH EVENT ::: ", "x :" + x + ", y :" + y);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(mLastX != -1){
                    if(x != mLastX || y != mLastY){
                        mCanvas.drawLine(mLastX, mLastY, x, y, mPaint);
                    }
                }

                mLastX = x;
                mLastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                if(mLastX != -1){
                    mCanvas.drawLine(mLastX, mLastY, x, y, mPaint);
                }

                mLastX = x;
                mLastY = y;
                break;

            case MotionEvent.ACTION_UP:
                mLastX = mLastY = -1;
                break;
        }

        invalidate();

        return true;
    }

    public boolean Save(OutputStream stream){
        try{
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            invalidate();
        } catch (Exception ex){
            return false;
        }

        return true;
    }
}

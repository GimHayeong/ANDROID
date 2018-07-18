package org.hyg.intentbyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by shiny on 2018-03-20.
 */

public class CustomView extends View {

    // 화면 크기
    private int mWidth, mHeight;

    private Paint mPaint;
    private Rect mRect = new Rect(100, 100, 200, 200);
    private boolean mIsScrolling = false;
    private int mOfsX, mOfsY;

    // 메모리에 만들어질 비트맵 객체
    private Bitmap mBitmap;
    // 메모리에 만들어질 비트맵 객체를 그리기 위한 캔버스 객체
    private Canvas mCanvas;

    public CustomView(Context context) {
        super(context);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
    }

    /**
     * 메모리상에 만들어진 비트맵 이미지를 화면뷰에 표시(drawBitmap)
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas = canvas;

        if(mBitmap != null) {
            mCanvas.drawBitmap(mBitmap, 0, 0, null);
        }

        canvas.drawRect(mRect, mPaint);

        //drawSample(canvas);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        initBitmap();

    }


    /**
     * 메모리상에 비트맵 이미지 생성
     *  : 뷰와 메모리상의 비트맵 이미지 크기를 동일하게 하기 위해
     *    OnSizeChanged 메서드 호출될 때마다 비트맵객체 생성
     *  : 캔바스에 그리기를 하면 setBitmap 으로 설정한 비트맵에 그려진다.
     */
    private void initBitmap() {
        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mIsScrolling = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if(mIsScrolling) {
                    mRect.offsetTo((int)event.getX(), (int)event.getY());
                }
                break;

            case MotionEvent.ACTION_UP:
                mIsScrolling = false;
                break;

        }

        invalidate();
        return true;
    }


    /**
     * 매트릭스 객체를 이용해 좌우 대칭 비트맵 이미지 그리기
     * @param bmpSrc
     */
    private void drawRotateHorizontal(Bitmap bmpSrc){
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        bmpSrc = Bitmap.createBitmap(bmpSrc, 0, 0, bmpSrc.getWidth(), bmpSrc.getHeight(), matrix, false);
        mCanvas.drawBitmap(bmpSrc, 30, 130, mPaint);
    }

    /**
     * 매트릭스 객체를 이용해 상하 대칭 비트맵 이미지 그리기
     * @param bmpSrc
     */
    private void drawRotateVertical(Bitmap bmpSrc){
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        bmpSrc = Bitmap.createBitmap(bmpSrc, 0, 0, bmpSrc.getWidth(), bmpSrc.getHeight(), matrix, false);
        mCanvas.drawBitmap(bmpSrc, 30, 230, mPaint);
    }

    /**
     * 블러 효과
     *  : 2배 확대한 후 번짐 효과 마스크 적용
     * @param bmpSrc : 이미지 소스
     * @param radius : 블러 정도
     */
    private void drawBlur(Bitmap bmpSrc, float radius){
        Paint blurPaint = new Paint();
        blurPaint.setMaskFilter(new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL));
        bmpSrc = Bitmap.createScaledBitmap(bmpSrc, bmpSrc.getWidth() * 2, bmpSrc.getHeight() * 2, false);
        mCanvas.drawBitmap(bmpSrc, 100, 230, blurPaint);
    }




    /**
     * 사각형과 원을 그림
     *  @param canvas
     */
    private void drawSample(Canvas canvas){
        mRect.set(10, 10, 100, 100);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        canvas.drawRect(mRect, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2.0f);
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(mRect, mPaint);

        mRect.set(120, 10, 210, 100);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setARGB(128, 0, 0, 255);
        canvas.drawRect(mRect, mPaint);

        DashPathEffect effect = new DashPathEffect(new float[] {5, 5}, 1);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3.0f);
        mPaint.setPathEffect(effect);
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(mRect, mPaint);

        mPaint = new Paint();
        float cx = 50, cy = 160, radius = 40;

        mPaint.setColor(Color.MAGENTA);
        canvas.drawCircle(cx, cy, radius, mPaint);

        cx = 160;
        mPaint.setAntiAlias(true);
        canvas.drawCircle(cx, cy, radius, mPaint);

        cx = 20;
        cy = 260;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.MAGENTA);
        mPaint.setTextSize(30);
        canvas.drawText("Text (Stroke)", cx, cy, mPaint);

        cy = 320;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(30);
        canvas.drawText("Text(채우기)", cx, cy, mPaint);
    }
}

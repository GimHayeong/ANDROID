package org.hyg.rolypolybyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    Bitmap m_bmpBack;
    Bitmap m_bmpToy;
    Bitmap m_bmpShadow;

    int m_screenWidth, m_screenHeight;
    int m_centerX, m_centerY;
    int m_toyWidth, m_toyHeight;
    int m_shadowWidth, m_shadowHeight;

    int m_angle = 0;
    int m_direction = 0;
    int m_lLimit = -15;
    int m_rLimit = 15;

    Handler m_hdlRotate = new Handler(){
        public void handleMessage(Message msg){
            invalidate();

            m_hdlRotate.sendEmptyMessageDelayed(0, 10);
        }
    };

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_hdlRotate.sendEmptyMessageDelayed(0, 10);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(m_bmpBack, 0, 0, null);

        canvas.drawBitmap(m_bmpShadow
                        , m_centerX - m_shadowWidth
                        , m_centerY - m_shadowHeight
                        , null);

        RotateToy();
        canvas.rotate(m_angle, m_centerX, m_centerY);
        canvas.drawBitmap(m_bmpToy
                        , m_centerX - m_toyWidth
                        , m_centerY - m_toyHeight
                        , null);
        canvas.rotate(-m_angle, m_centerX, m_centerY);
    }

    private void RotateToy() {
        m_angle += m_direction;

        // 좌우 한계치에 도달하면 회전방향 바꾸기
        if(m_angle <= m_lLimit || m_angle >= m_rLimit){
            m_lLimit++;
            m_rLimit--;
            m_direction = -m_direction;
            m_angle += m_direction;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.m_screenWidth = w;
        this.m_screenHeight = h;

        m_centerX = w / 2;
        m_centerY = (int) (h * 0.8f);

        getBitmap();
    }

    public void getBitmap() {
        m_bmpToy = BitmapFactory.decodeResource(getResources(), R.drawable.toy);
        m_bmpShadow = BitmapFactory.decodeResource(getResources(), R.drawable.shadow);
        m_bmpBack = BitmapFactory.decodeResource(getResources(), R.drawable.back);

        // 배경이미지 화면크기에 맞춰 확대
        m_bmpBack = Bitmap.createScaledBitmap(m_bmpBack, m_screenWidth, m_screenHeight, true);

        m_toyWidth = m_bmpToy.getWidth() / 2;
        m_toyHeight = m_bmpToy.getHeight();

        m_shadowWidth = m_bmpShadow.getWidth() / 2;
        m_shadowHeight = m_bmpShadow.getHeight() / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            m_lLimit = -15;
            m_rLimit = 15;

            if(m_direction == 0){
                m_direction = -1;
            }
        }

        return true;
    }
}

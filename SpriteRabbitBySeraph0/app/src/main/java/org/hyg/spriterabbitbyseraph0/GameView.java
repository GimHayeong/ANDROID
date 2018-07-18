package org.hyg.spriterabbitbyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    int m_w;
    int m_h;
    int m_xRabbit, m_yRabbit;
    int m_wRabbit, m_hRabbit;
    int m_speedX = 3;
    int m_speedY = 2;
    int m_idxRabbit = 0;
    int m_cntLoop = 0;
    float m_startX, m_startY;
    public boolean isRun = true;

    Bitmap[] m_rabbits = new Bitmap[2];
    int[] imgSrcs = {R.drawable.rabbit_1, R.drawable.rabbit_2};

    // 달리는 동작을 구현하기 위해
    // 10/1000초마다 화면을 다시 그리는 요청을 하는 핸들러
    Handler m_hdlrRun = new Handler(){
        public void handleMessage(Message msg){
            if(isRun) {
                invalidate();
            }

            // 특별한 조건없이 반복만 요청하는 경우, what: 0
            m_hdlrRun.sendEmptyMessageDelayed(0, 10);
        }
    };

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Convert to Bitmap
        for(int i=0; i<m_rabbits.length; i++){
            m_rabbits[i] = BitmapFactory.decodeResource(context.getResources(), imgSrcs[i]);
        }

        m_wRabbit = m_rabbits[m_idxRabbit].getWidth() / 2;
        m_hRabbit = m_rabbits[m_idxRabbit].getHeight() / 2;

        // 외부에서 최소 1회이상 호출 필수
        m_hdlrRun.sendEmptyMessageDelayed(0, 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(++m_cntLoop % 5 == 0){
            m_idxRabbit = 1 - m_idxRabbit;
        }

        m_xRabbit += m_speedX;
        if(m_xRabbit < m_wRabbit || m_xRabbit > m_w - m_wRabbit){
            m_speedX = -m_speedX;
            m_xRabbit += m_speedX;
        }

        m_yRabbit += m_speedY;
        if(m_yRabbit < m_hRabbit || m_yRabbit > m_h - m_hRabbit){
            m_speedY = -m_speedY;
            m_yRabbit += m_speedY;
        }

        canvas.drawBitmap(m_rabbits[m_idxRabbit],
                m_xRabbit - m_wRabbit,
                m_yRabbit - m_hRabbit,
                null);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.m_w = w;
        this.m_h = h;

        this.m_xRabbit = w / 2;
        this.m_yRabbit = h / 2;
    }

    /*
    * 드래그&드롭으로 길이 비례 이동방향과 속도 변경
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                m_startX = event.getX();
                m_startY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                float destX = event.getX();
                float destY = event.getY();

                m_speedX = (int)(destX - m_startX) / 100;
                m_speedY = (int)(destY - m_startY) / 100;
                break;

            case MotionEvent.ACTION_MOVE:
                break;
        }

        return true;
    }
}

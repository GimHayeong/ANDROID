package org.hyg.analogclockbyseraph0;

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
import android.view.View;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    int m_centerX, m_centerY;

    Bitmap m_bmpClock;
    Bitmap[] m_bmpPins = new Bitmap[3];
    int m_hour, m_min, m_sec, m_millisec;

    float m_angleHour, m_angleMin, m_angleSec;

    int m_clockWH, m_pinWidth, m_pinHeight;

    Handler m_hdlClock = new Handler(){
      public void handleMessage(Message msg){
          GetTime();
          invalidate();
          m_hdlClock.sendEmptyMessageDelayed(0, 100);
      }
    };

    private void GetTime() {
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+9"));

        // 시:분:초.밀리초
        m_hour = calendar.get(Calendar.HOUR);
        m_min = calendar.get(Calendar.MINUTE);
        m_sec = calendar.get(Calendar.SECOND);
        m_millisec = calendar.get(Calendar.MILLISECOND) / 100;

        // 시계 바늘 각도
        // 360 / 60분 = 360 / 60초 = 6도
        // 360 / 12시간 = 30도
        // 초침의 회전에 따른 분침의 미세한 회전 반영(분침의 회전각에 초침의 회전각 반영)
        // 분침의 회전에 따른 시침의 미세한 회전 반영(시침의 회전각에 분침의 회전각 반영)
        m_angleSec = m_sec * 6;
        m_angleMin = m_min * 6 + m_angleSec / 60;
        m_angleHour = m_hour * 30 + m_angleMin / 12;
    }


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_bmpClock = BitmapFactory.decodeResource(getResources(), R.drawable.clock);
        for(int i=0; i<m_bmpPins.length; i++){
            m_bmpPins[i] = BitmapFactory.decodeResource(getResources(), R.drawable.pin_1 + i);
        }

        m_clockWH = m_bmpClock.getWidth() / 2;
        m_pinWidth = m_bmpPins[0].getWidth() / 2;
        m_pinHeight = m_bmpPins[0].getHeight() - 60;

        m_hdlClock.sendEmptyMessageDelayed(0, 100);
    }



    @Override
    protected void onDraw(Canvas canvas) {

        canvas.scale(0.9f, 0.9f, m_centerX, m_centerY);
        canvas.save();

        canvas.drawBitmap(m_bmpClock
                    , m_centerX - m_clockWH
                    , m_centerY - m_clockWH
                    , null);

        int pinX = m_centerX - m_pinWidth;
        int pinY = m_centerY - m_pinHeight;
        canvas.rotate(m_angleHour
                    , m_centerX, m_centerY);
        canvas.drawBitmap(m_bmpPins[2]
                    , pinX
                    , pinY
                    , null);

        // 시침을 그릴때 반영된 캔바스의 회전값을 고려하여 그만큼 뺀 값만큼 회전
        canvas.rotate(m_angleMin - m_angleHour
                     , m_centerX, m_centerY);
        canvas.drawBitmap(m_bmpPins[1]
                        , pinX
                        , pinY
                        , null);

        // 분침을 그릴때 반영된 캔바스의 회전값을 고려하여 그만큼 뺀 값만큼 회전
        canvas.rotate(m_angleSec - m_angleMin
                     , m_centerX, m_centerY);
        canvas.drawBitmap(m_bmpPins[0]
                        , pinX
                        , pinY
                        , null);

        // 회전전으로 복원
        canvas.restore();
        Paint timePaint = new Paint();
        timePaint.setTextSize(60);
        timePaint.setTextAlign(Paint.Align.CENTER);
        String strTime = String.format("%d : %d : %d.%d", m_hour, m_min, m_sec, m_millisec);
        canvas.drawText(strTime, m_centerX, m_centerY + m_clockWH + 100, timePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_centerX = w / 2;
        m_centerY = h / 2;
    }
}

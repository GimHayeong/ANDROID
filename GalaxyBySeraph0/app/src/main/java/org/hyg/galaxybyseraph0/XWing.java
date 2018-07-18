package org.hyg.galaxybyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by shiny on 2018-03-02.
 */

public class XWing {

    // 화면크기
    private int m_scrW, m_scrH;

    // 한계속도, 현재속도
    private final float MAX_SPEED = 1200f;
    private float m_speed;

    // 이동방향(-1, 0, 1), 목적지 x좌표, 출발여부
    private int m_direction;
    private float m_tx;
    private boolean m_isStart;

    // 현재위치
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    private Bitmap m_bmp;
    public Bitmap getXWing() { return m_bmp; }

    public XWing(Context context, int width, int height){
        m_scrW = width;
        m_scrH = height;

        m_bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.xwing);
        m_width = m_bmp.getWidth() / 2;
        m_height = m_bmp.getHeight() / 2;

        // 초기위치
        m_x = m_scrW / 2;
        m_y = m_scrH - m_height - 40;
    }

    /**
     * 출발이면 가속, 아니면 감속
     * 목적지 50픽셀(50 x 50 = 2500) 이내이면 감속하며 정지
     * 이동한 좌표가 화면 가장자리이면 정지
     */
    public void moveToNext(){
        if(m_isStart){
            m_speed = MathF.getLerp(m_speed, MAX_SPEED, 5 * DTime.DeltaTime);
        } else {
            m_speed = MathF.getLerp(m_speed, 0, 10 * DTime.DeltaTime);
        }

        if(MathF.getDistanceWithoutSqrt(m_x, m_y, m_tx, m_y) < 2500) {
            m_isStart = false;
        }

        m_x += m_direction * m_speed * DTime.DeltaTime;

        if(m_x < m_width || m_x > m_scrW - m_width){
            m_x -= m_direction * m_speed * DTime.DeltaTime;
            m_speed = m_direction = 0;
            m_isStart = false;
        }
    }

    public void getIntoAction(float tx){
        m_tx = tx;
        m_direction = (m_x < tx) ? 1 : -1;
        m_isStart = true;
    }
}

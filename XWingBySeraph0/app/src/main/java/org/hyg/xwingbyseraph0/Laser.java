package org.hyg.xwingbyseraph0;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by shiny on 2018-02-24.
 */

public class Laser {

    // 화면 크기
    private int m_scrW, m_scrH;

    // 이동 속도 픽셀
    private int m_speed = 1200;
    // 이동 방향 벡터
    private PointF m_direction = new PointF();
    // 레이저 좌표
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    // 레이저 크기
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }
    // 레이저 이미지
    private Bitmap m_bmpLaser;
    public Bitmap getLaser() { return m_bmpLaser; }
    // 레이저 소멸 여부
    private boolean m_isDestruction;
    public boolean getIsDestruction() { return m_isDestruction; }


    public Laser(int width, int height, float px, float py){
        m_scrW = width;
        m_scrH = height;

        m_x = px;
        m_y = py;

        m_bmpLaser = CommonResources.getLaser();
        m_width = CommonResources.getLaserWidth();
        m_height = CommonResources.getLaserHeight();

        m_direction.set(0, m_speed);
    }

    public void moveToNext(){
        m_y -= m_direction.y * DTime.DeltaTime;

        if(m_y < -m_height) {
            m_isDestruction = true;
        }
    }

}

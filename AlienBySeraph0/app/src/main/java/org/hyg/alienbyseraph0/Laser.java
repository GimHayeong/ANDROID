package org.hyg.alienbyseraph0;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by shiny on 2018-02-24.
 *
 * 우주선의 회전방향으로 발사되는 레이저
 *
 */

public class Laser {

    // 화면 크기
    private int m_scrW, m_scrH;

    // 이동 속도 픽셀
    private int m_speed = 1200;
    // 이동 발사 방향 벡터
    private PointF m_direction = new PointF();

    // 레이저 현재 위치 벡터
    private PointF m_pos = new PointF();
    public PointF getPos() { return m_pos; }

    // 레이저 크기
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    // 레이저 회전각도
    private float m_angle;
    public float getAngle() { return m_angle; }

    // 레이저 이미지
    private Bitmap m_bmpLaser;
    public Bitmap getLaser() { return m_bmpLaser; }
    // 레이저 소멸 여부
    private boolean m_isDestruction = false;
    public boolean getIsDestruction() { return m_isDestruction; }


    public Laser(int width, int height, PointF pos, PointF direction, float angle){
        m_scrW = width;
        m_scrH = height;

        m_pos.set(pos);
        m_direction.set(direction);
        m_angle = angle;

        m_bmpLaser = CommonResources.getLaser();
        m_width = CommonResources.getLaserWidth();
        m_height = CommonResources.getLaserHeight();
    }

    public void moveToNext(){
        m_pos.x += m_direction.x * m_speed * DTime.DeltaTime;
        m_pos.y += m_direction.y * m_speed * DTime.DeltaTime;

        if(m_pos.x < -m_width || m_pos.x > m_scrW + m_width || m_pos.y < -m_height || m_pos.y > m_scrH + m_height) {
            m_isDestruction = true;
        }
    }

}

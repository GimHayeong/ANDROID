package org.hyg.destoryalienbyseraph0;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by shiny on 2018-02-28.
 * 매 프레임마다 에일리언과의 충돌여부 체크
 */

public class Laser {

    // 화면크기
    private int m_scrW, m_scrH;

    // 이동속도, 이동방향벡터
    private final int SPEED = 1200;
    private PointF m_direction = new PointF();

    // 현재위치, 크기
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    // 이미지, 소멸여부
    private Bitmap m_bmp;
    public Bitmap getLaser() { return m_bmp; }
    private boolean m_isDestructed = false;
    public boolean getIsDestructed() { return m_isDestructed; }


    public Laser(int width, int height, float px, float py){
        m_scrW = width;
        m_scrH = height;

        m_x = px;
        m_y = py;

        m_bmp = CommonResources.getLaser();
        m_width = CommonResources.getLaserWidth();
        m_height = CommonResources.getLaserHeight();

        m_direction.set(0, SPEED);
    }

    /**
     * 충돌체크 후 이동
     * 화면을 벗어나면 소멸시킴
     */
    public void moveToNext(){
        checkCollision();

        m_y -= m_direction.y * DTime.DeltaTime;

        if(m_y < -m_height){
            m_isDestructed = true;
        }
    }

    /**
     * 에일리언과 레이저 충돌여부
     *  : 레이저의 위치를 에얼리언의 충돌체크함수로 전달하여 충돌여부 확인
     */
    private void checkCollision() {
        for(Alien itm : CommonResources.AlienList){
            if(itm.checkCollision(m_x, m_y, m_width, m_height)){
                m_isDestructed = true;
                break;
            }
        }
    }
}

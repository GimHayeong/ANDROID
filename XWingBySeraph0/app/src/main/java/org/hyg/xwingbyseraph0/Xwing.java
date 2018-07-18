package org.hyg.xwingbyseraph0;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by shiny on 2018-02-24.
 */

public class Xwing {

    // 화면 크기
    private int m_scrW, m_scrH;

    // 목적지 좌표
    private PointF m_posTarget = new PointF();
    // 목적지 존재 여부
    private boolean m_isExistTarget;

    // 전투기 위치
    private PointF m_pos = new PointF();
    public PointF getPos() { return m_pos; }
    // 전투기 크기
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    // 전투기 이미지
    private Bitmap m_bmpXwing;
    public Bitmap getXwing() { return m_bmpXwing; }

    public Xwing(int width, int height){
        m_scrW = width;
        m_scrH = height;

        m_bmpXwing = CommonResources.getXwing();
        m_width = CommonResources.getXwingWidth();
        m_height = CommonResources.getXwingHeight();

        m_pos.set(width / 2
                , height - m_height - 40);
    }

    public void moveToNext(){
        if(m_isExistTarget){
            m_pos.set(MathF.getLerp(m_pos, m_posTarget, 3.0f * DTime.DeltaTime));

            // 목적지에 도착하면 이동 중지
            if(MathF.getDistance(m_pos, m_posTarget) < 1) {
                m_isExistTarget = false;
            }
        }
    }

    public void getIntoAction(float tx, float ty){
        // 전투기를 터치하면 레이저 발사
        if(MathF.getIsTouch(m_pos.x, m_pos.y, m_width * 0.9f, tx, ty)){
            fire();
        } else {
            setTarget(tx, ty);
        }
    }

    // 전투기 이동 목적지 설정
    private void setTarget(float tx, float ty){
        m_posTarget.set(tx, ty);
        m_isExistTarget = true;
    }

    private void fire(){
        m_isExistTarget = false;
        CommonResources.playSound();

        synchronized (GameView.LaserList){
            GameView.LaserList.add(new Laser(m_scrW, m_scrH, m_pos.x - m_width, m_pos.y - m_height / 2));
            GameView.LaserList.add(new Laser(m_scrW, m_scrH, m_pos.x, m_pos.y - m_height / 2));
        }
    }
}

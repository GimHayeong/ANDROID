package org.hyg.destoryalienbyseraph0;

import android.graphics.Bitmap;

/**
 * Created by shiny on 2018-02-28.
 */

public class XWing {
    // X-Wing 상태 : 대기, 출발(가속), 멈춤(감속)
    private enum STATE { IDLE, START, STOP }
    private STATE m_state = STATE.IDLE;

    // 화면크기
    private int m_scrW, m_scrH;

    // 애니메이션 지연시간, 이전프레임으로부터의 경과시간, 이미지 인덱스
    private final float ANY_TIME = 0.5f;
    private float m_aniSpan;
    private int m_bmpIdx;

    // 최대 속도, 현재 속도
    private final float MAX_SPEED = 1200;
    private float m_speed;

    // 목적지, 이동방향(-1, 0, 1)
    private float m_tx;
    private int m_direction;

    // 현재위치, 크기
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    private Bitmap m_bmp;
    public Bitmap getXWing() { return m_bmp; }


    public XWing(int width, int height){
        m_scrW = width;
        m_scrH = height;

        m_bmp = CommonResources.getXWing()[0];
        m_width = CommonResources.getXWingHeight();
        m_height = CommonResources.getXWingHeight();

        // 초기 위치
        m_x = width / 2;
        m_y = height - m_height - 40;
    }




    /**
     * 출발이면 가속, 멈춤이면 감속
     * 목적지와 50픽셀(50 * 50 = 2500) 이내로 가까워지면 멈춤으로 상태 변환(감속)
     * 화면 가장자리이면 정지(속도, 이동방향 초기화)
     */
    public void moveToNext() {
        startAnimation();

        switch(m_state){
            case START:
                m_speed = MathF.getLerp(m_speed, MAX_SPEED, 5 * DTime.DeltaTime);
                break;

            case STOP:
                m_speed = MathF.getLerp(m_speed, 0, 10 * DTime.DeltaTime);
                break;
        }

        m_x += m_speed * m_direction * DTime.DeltaTime;

        if(MathF.getDistanceWithoutSqrt(m_x, m_y, m_tx, m_y) < 2500){
            m_state = STATE.STOP;
        }

        if(m_x < m_width || m_x > m_scrW - m_width){
            m_x -= m_speed * m_direction * DTime.DeltaTime;
            m_speed = m_direction = 0;
        }
    }


    /**
     * 전투기를 터치하면 레이저 발사, 다른 곳을 터치하면 해당 지점으로 전투기 이동
     * @param tx
     * @param ty
     */
    public void getIntoAction(float tx, float ty){
        if(MathF.getIsTouch(m_x, m_y, m_height, tx, ty)){
            fire();
        } else {
            moveIntoTarget(tx);
        }
    }


    /**
     * 어뢰와 충돌여부 확인
     * 어뢰로부터 어뢰의 위치 등 정보를 전달받아 충돌여부 확인
     * 어뢰와 충돌시 전투기 주변에 작은 폭발을 발생시킨다.
     * @param tx : 어뢰 x자표
     * @param ty : 어뢰 y좌표
     * @param tr : 어뢰 반지름
     * @return
     */
    public boolean checkCollision(float tx, float ty, int tr){
        boolean hit = MathF.getIsCollision(m_x, m_y, m_height * 0.7f, tx, ty, tr);

        if(hit){
            CommonResources.playSound("Small");
            CommonResources.addExplosion(tx, ty + tr, "Small");
        }

        return hit;
    }


    /**
     * 목적지로 이동 출발
     * @param tx
     */
    private void moveIntoTarget(float tx) {
        m_tx = tx;
        m_direction = (m_x < tx) ? 1 : -1;
        m_state = STATE.START;
    }

    /**
     * 레이저 두 발 발사
     */
    private void fire() {
        CommonResources.playSound("Laser");

        CommonResources.addLaser(m_scrW, m_scrH, m_x - m_width, m_y);
        CommonResources.addLaser(m_scrW, m_scrH, m_x + m_width, m_y);
    }


    /**
     * 깜빡(0 => 1 => 0 => 1)이는 애니메이션
     */
    private void startAnimation() {
        m_aniSpan += DTime.DeltaTime;
        if(m_aniSpan < ANY_TIME){
            m_aniSpan = 0;

            m_bmpIdx = 1 - m_bmpIdx;
            m_bmp = CommonResources.getXWing()[m_bmpIdx];
        }
    }
}

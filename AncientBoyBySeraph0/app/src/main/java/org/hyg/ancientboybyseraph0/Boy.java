package org.hyg.ancientboybyseraph0;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by shiny on 2018-02-26.
 */

public class Boy {
    
    // 동작상태(0: 대기, 1: 달리기, 2: 높이뛰기
    private final int IDLE = 0;
    private final int RUN = 1;
    private final int JUMP = 2;
    
    // 화면크기 
    private int m_scrW, m_scrH;
    
    // 속도, 점프속도, 중력, 이동방향벡터
    private float m_speed = 600;
    private float m_speedJump = 800;
    private float m_gravity = 1200;
    private PointF m_direction = new PointF();
    
    // 현재상태
    private int m_state = IDLE;
    private void setState(int state) {
        this.m_state = state;
        startAnimation();
    }

    // 애니메이션 이미지 인덱스, 이미지 수
    private int m_bmpIdx, m_bmpCnt = 5;
    
    // 애니메이션 지연시간, 이전프레임으로부터 경과시간
    private float m_aniSpan = 0.2f, m_aniTime;
    
    // 현재위치, 크기, 오른쪽방향으로 이동 여부
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }
    private boolean m_isRight = true;
    public boolean getIsRight() { return m_isRight; }
    
    // 지면높이
    private float m_groundH;
    public float getGroundHeight() { return m_groundH; }
    
    // 현재 이미지
    private Bitmap m_bmp;
    public Bitmap getBoy() { return m_bmp; }
    
    // 그림자 크기, 비율, 이미지
    private int m_sw, m_sh;
    public int getShadowWidth() { return m_sw; }
    public int getShadowHeight() { return m_sh; }
    private float m_sScale = 1;
    public float getShadowScale() { return m_sScale; }
    private Bitmap m_bmpShadow;
    public Bitmap getShadow() { return m_bmpShadow; }
    
    public Boy(int width, int height){
        m_scrW = width;
        m_scrH = height;
        
        // 소년 이미지 초기화
        m_bmp = CommonResources.getBoys()[IDLE][0];
        m_width = CommonResources.getBoyWidth();
        m_height = CommonResources.getBoyHeight();
        
        // 그림자 이미지 초기화
        m_bmpShadow = CommonResources.getShadow();
        m_sw = CommonResources.getShadowWidth();
        m_sh = CommonResources.getShadowHeight();
        
        // 초기 위치
        m_groundH = m_scrH * 0.85f;
        m_x = width / 2;
        m_y = m_groundH - m_height;
    }
    
    public void moveToNext(){
        playAnimation();
        
        switch(m_state){
            case IDLE:
                break;
                
            case JUMP:
                m_direction.y += m_gravity * DTime.DeltaTime;
                m_sScale = m_y / (m_groundH - m_height);
                checkIsLanded();
                // RUN 연결해서 수행

            case RUN:
                m_x += m_direction.x * DTime.DeltaTime;
                m_y += m_direction.y * DTime.DeltaTime;
                if(m_y + m_height > m_groundH) { m_y = m_groundH - m_height; }
                break;
        }

    }

    private void playAnimation() {
        m_aniTime += DTime.DeltaTime;

        if(m_aniTime > m_aniSpan) {
            m_aniTime = 0;

            m_bmpIdx = MathF.getRepeatIdx(m_bmpIdx, m_bmpCnt);
            m_bmp = CommonResources.getBoys()[m_state][m_bmpIdx];
        }
    }

    /**
     * 동작상태가 변경되면 해당 동작의 첫 애니메이션 시작
     */
    private void startAnimation() {
        m_bmpIdx = 0;
        m_bmp = CommonResources.getBoys()[m_state][m_bmpIdx];
    }

    /**
     * 바닥 착지상태이면 점프중지하고 달리기
     */
    private void checkIsLanded() {
        if(m_y + m_height > m_groundH){
            m_y = m_groundH - m_height;
            m_direction.y = 0;
            setState(RUN);
        }
    }

    // 소년을 클릭했는지 여부
    private boolean isHit(float tx, float ty){
        return MathF.getIsTouch(m_x, m_y, m_height, tx, ty);
    }

    /**
     * 소년이 달릴때 소년을 터치하면 멈추기
     * @param tx : 터치 x좌표
     * @param ty : 터치 y좌표
     */
    public void stop(float tx, float ty){
        if(m_state == RUN && isHit(tx, ty)){
            m_direction.x = 0;
            setState(IDLE);
        }
    }

    /**
     * 같은 방향으로 스크롤하면 계속 달리고, 스크롤 방향이 바뀌면 방향을 달리해서 달린다.
     * @param dist
     */
    public void run(float dist){

        m_isRight = (dist <= 0);
        m_direction.x = m_isRight ? m_speed : -m_speed;

        if(m_state != RUN) {
            m_y = m_groundH- m_height;
            setState(RUN);
        }
    }

    /**
     * 화면을 더블터치하면 달리는 중이면 점프한다.
     */
    public void jump(){
        if(m_state == RUN){
            m_direction.y = -m_speedJump;
            setState(JUMP);
        }
    }

}

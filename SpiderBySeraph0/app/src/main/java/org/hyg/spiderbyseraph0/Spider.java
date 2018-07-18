package org.hyg.spiderbyseraph0;

import android.graphics.Bitmap;
import android.widget.GridView;

/**
 * Created by shiny on 2018-02-26.
 */

public class Spider {
    // 거미 동작 상태 상수
    private final int IDLE = 0;
    private final int MOVE = 1;
    private final int STOP = 2;

    // 현재 상태
    private int m_state = IDLE;

    // 화면 크기
    private int m_scrW, m_scrH;
    // 이동 속도, 최대속도
    private float m_speed = 0;
    private final float MAX_SPEED = 1000;

    // 이미지 인덱스, 총 이미지 갯수
    private int m_bmpIdx;
    private int m_bmpCnt = 5;

    // 애니메이션 지연시간, 이전프레임으로부터의 경과시간
    private float m_aniSpan = 0.4f;
    private float m_aniTime = 0;

    // 독액 발사간격(지연시간), 이전프레임으로부터의 경과시간
    private float m_fireSpan = 0.2f;
    private float m_fireTime = m_fireSpan;

    // 이동방향(-1, 0, 1), 발사여부
    private int m_direction;
    private boolean m_isEnableFire;

    /*================= 나비 추가관련 변수 */
    private int m_fireCnt = 0;
    public int getFireCount() { return m_fireCnt; }
    /* 나비 추가관련 변수 =================*/

    // 위치, 크기, 이미지
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }
    private Bitmap m_bmp;
    public Bitmap getSpider() { return m_bmp; }

    public Spider(int width, int height){
        m_scrW = width;
        m_scrH = height;

        m_bmp = CommonResources.getSpider()[0];
        m_width = CommonResources.getSpiderWidth();
        m_height = CommonResources.getSpiderHeight();

        // 초기위치(화면 중앙 하단)
        m_x = width / 2;
        m_y = height - m_height - 10;
    }

    public void moveToNext(){
        startAnimation();

        if(m_isEnableFire){
            firePoison();
        }

        switch (m_state){
            case MOVE:
                // 점점 가속
                m_speed = MathF.getLerp(m_speed, MAX_SPEED, 5 * DTime.DeltaTime);
                break;
            case STOP:
                // 점점 감속
                m_speed = MathF.getLerp(m_speed, 0, 20 * DTime.DeltaTime);
                break;
        }

        m_x += m_direction * m_speed * DTime.DeltaTime;

        // 화면을 벗어나기 전 정지
        if(m_x < m_width || m_x > m_scrW - m_width){
            m_x -= m_direction * m_speed *DTime.DeltaTime;
            m_direction = 0;
        }

    }

    /**
     * [ACTION_DOWN]
     * 거미를 터치하면 독액 발사, 다른 지점을 터치하면 해당 저점 방향으로 거미 이동시작
     * @param tx : 터치 x좌표
     * @param ty : 터치 y좌표
     */
    public void getIntoAction(float tx, float ty) {
        if(MathF.getIsTouch(m_x, m_y, m_width, tx, ty)){
            m_isEnableFire = true;
        } else {
            m_direction = (m_x < tx) ? 1 : -1;
            m_state = MOVE;
        }
    }

    /**
     * [ACTION_UP]
     * 터치를 중단하면, 독액 발사 중지 (거미가 이동 중이었으면 감속하면서 서서히 중지)
     */
    public void stopAction(){
        m_isEnableFire = false;
        if(m_state == MOVE){
            m_state = STOP;
        }
    }

    private void startAnimation() {
        m_aniTime += DTime.DeltaTime;

        // 지연시간이 지나면 다음 이미지(애니메이션)
        if(m_aniTime > m_aniSpan){
            m_aniTime = 0;
            m_bmpIdx = MathF.getRepeatIdx(m_bmpIdx, m_bmpCnt);
            m_bmp = CommonResources.getSpider()[m_bmpIdx];
        }
    }

    private void firePoison() {
        m_fireTime += DTime.DeltaTime;

        if(m_fireTime > m_fireSpan){
            m_fireTime = 0;
            CommonResources.playSound("Poison");
            synchronized (GameView.PoisonList){
                GameView.PoisonList.add(new Poison(m_x, m_y));
                m_fireCnt++;
            }
        }
    }
}

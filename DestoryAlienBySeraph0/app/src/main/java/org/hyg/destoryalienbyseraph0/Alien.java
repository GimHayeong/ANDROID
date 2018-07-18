package org.hyg.destoryalienbyseraph0;

import android.graphics.Bitmap;

import java.util.Random;

/**
 * Created by shiny on 2018-02-28.
 */

public class Alien {

    // 화면 크기
    private int m_scrW, m_scrH;
    private Random m_rnd = new Random();

    // 이동속도(400 ~ 500 픽셀), 이동방향(-1, 1)
    private float m_speed;
    private int m_direction;

    // 발사지연시간(1 ~ 2초), 파격횟수
    private float m_fireDelay;
    private int m_hitCnt;

    // 현재위치, 크기
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    private Bitmap m_bmp;
    public Bitmap getAlien() { return m_bmp; }


    public Alien(int width, int height){
        m_scrW = width;
        m_scrH = height;

        m_bmp = CommonResources.getAlien();
        m_width = CommonResources.getAlienWidth();
        m_height = CommonResources.getAlienHeight();

        init();
    }

    /**
     * 어뢰를 발사하고 x축 이동
     * 화면을 벗어나면 에일리언 속도, 위치, 이동방향, 발사지연시간, 파격횟수 초기화
     */
    public void moveToNext(){
        fire();

        m_x += m_speed * m_direction * DTime.DeltaTime;

        if(m_x < -m_width * 2 || m_x > m_scrW + m_width * 2){
            init();
        }
    }

    /**
     * 레이저(사각형영역)와의 충돌 판정
     * 레이저로부터 레이저 위치 등 정보를 전달받아 충돌여부 확인
     *  : 충돌하면 에일리언 위치에 작은 폭발을 발생시킨다.
     *    충돌횟수를 누적하여 4회를 초과하면 에일리언은 폭발하고 초기화된다.
     * @param tx : 레이저 x좌표
     * @param ty : 레이저 y좌표
     * @param tw : 레이저크기 넓이
     * @param th : 레이저크기 높이
     * @return
     */
    public boolean checkCollision(float tx, float ty, int tw, int th){
        boolean hit = MathF.getIsCollision(m_x, m_y, m_height, tx, ty, tw, th);

        if(!hit) return false;

        m_hitCnt++;

        if(m_hitCnt >= 4){
            CommonResources.playSound("Big");
            CommonResources.addExplosion(m_x, m_y, "Big");
            init();
        } else {
            CommonResources.playSound("Small");
            CommonResources.addExplosion(m_x, m_y, "Small");
        }

        return true;
    }

    /**
     * 어뢰 발사
     *  : 1 ~ 2초 주기로 랜덤하게 발사
     */
    private void fire() {
        m_fireDelay -= DTime.DeltaTime;

        if(m_fireDelay < 0){
            CommonResources.addTorpedo(m_scrW, m_scrH, m_x, m_y);
            m_fireDelay = m_rnd.nextInt(2) + 1;
        }
    }



    /**
     * 초기화
     * 에일리언 속도, 위치, 이동방향, 발사지연시간, 파격횟수
     */
    private void init() {
        m_speed = m_rnd.nextInt(101) + 400;
        m_y = m_rnd.nextInt(201) + m_height;

        if(m_rnd.nextInt(2) == 1){
            m_x = -m_width * 2;
            m_direction = 1;
        } else {
            m_x = m_scrW + m_width * 2;
            m_direction = -1;
        }

        m_fireDelay = m_rnd.nextInt(2) + 1;
        m_hitCnt = 0;
    }

}

package org.hyg.spiderbyseraph0;

import android.graphics.Bitmap;

import java.util.Random;

/**
 * Created by shiny on 2018-02-27.
 */

public class Butterfly {
    // 화면 크기
    private int m_scrW, m_scrH;
    // 나비 종류
    private int m_kind;

    // 애니메이션 지연시간, 이전프레임으로부터 경과시간(0.07 ~ 0.12)
    private float m_aniTime = 0;
    private float m_aniSpan;
    private int m_bmpIdx = 0;

    // 현재위치(y:150~450), 이동방향, 속도(200 ~ 600)
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private int m_direction;
    private int m_speed;

    private Bitmap m_bmp;
    public Bitmap getButterfly() { return m_bmp; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    // 포획시 배당된 점수(10 ~ 50), 포획여부
    private int m_score;
    public int getScore() { return m_score; }
    private boolean m_isDestructed = false;
    public boolean getIsDestructed() { return m_isDestructed; }


    public Butterfly(int width, int height){
        m_scrW = width;
        m_scrH = height;

        init();
    }

    /**
     * 나비 이동 애니메이션
     * 화면을 벗어나면 초기화
     */
    public void moveToNext(){
        startAnimation();

        m_x += m_direction * m_speed * DTime.DeltaTime;

        if(m_x < -m_width * 4 || m_x > m_scrW + m_width * 4){
            init();
        }
    }

    public boolean checkCollision(float px, float py, int pr){
        boolean hit = MathF.getIsCollision(m_x, m_y, m_width, px, py, pr);
        if(hit){
            CommonResources.playSound("Capture");
            m_isDestructed = true;
        }

        return m_isDestructed;
    }

    /**
     *
     */
    private void startAnimation() {
        m_aniTime += DTime.DeltaTime;
        if(m_aniTime > m_aniSpan){
            m_aniTime = 0;
            m_bmpIdx = MathF.getRepeatIdx(m_bmpIdx, 10);
        }

        m_bmp = CommonResources.getButterfly()[m_kind][m_bmpIdx];
    }

    /**
     * 랜덤한 종류의 나비로 초기화
     * 나비의 종류에 따른 이미지를 설정하는 부분은 동기화 필요
     */
    public void init() {
        Random rnd = new Random();
        m_kind = rnd.nextInt(CommonResources.BUTTERFLY_KIND);

        m_speed = rnd.nextInt(401) + 200;
        m_y = rnd.nextInt(301) + 150;
        m_score = (rnd.nextInt(5) + 1) * 10;

        if(rnd.nextInt(2) == 0){
            m_direction = -1;
            m_x = m_scrW + m_width * 4;
        } else {
            m_direction = 1;
            m_x = -m_width * 4;
        }

        m_aniSpan = (rnd.nextInt(6) + 7) / 100f;
        m_isDestructed = false;

        synchronized (GameView.ButterflyList){
            m_bmp = CommonResources.getButterfly()[m_kind][0];
            m_width = CommonResources.getButterflyWidth();
            m_height = CommonResources.getButterflyHeight();
        }
    }
}

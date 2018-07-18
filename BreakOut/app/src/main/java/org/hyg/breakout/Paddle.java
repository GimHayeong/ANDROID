package org.hyg.breakout;

import android.graphics.Bitmap;

/**
 * Created by shiny on 2018-03-06.
 * 패들은 좌우로 이동하며 이동시 가속 처리.(거리 비례 가속처리)
 * 터치하면 터치한 지점쪽으로 패들이동하고, 손을 떼면 정지
 */

public class Paddle {

    // 화면크기
    private int m_scrW, m_scrH;

    // 이동방향(-1, 0, 1), 이동속도
    private int m_direction = 0;
    private final int MIN_SPEED = 300;
    private final int MAX_SPEED = 1500;
    private float m_speed = MIN_SPEED;

    // 위치, 이미지, 크기(스테이지 인덱스와 패들 크기 반비례)
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private Bitmap m_bmp;
    public Bitmap getPaddle() { return m_bmp; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    // 패들의 종류
    private final int MAX_KIND = 2;


    public Paddle(int w, int h){
        m_scrW = w;
        m_scrH = h;
    }

    /**
     * 패들 초기화
     *  : 패들의 종류는 0 ~ 2이므로 최댓값을 2로 제한
     * @param stageIdx : 스테이지 인덱스 (스테이지 인덱스와 패들 크기 반비례)
     */
    public void init(int stageIdx){
        int idx = Math.min(MAX_KIND, stageIdx);
        m_bmp = CommonResources.getPaddles()[idx];

        m_width = m_bmp.getWidth() / 2;
        m_height = m_bmp.getHeight() / 2;

        reset();
    }

    /**
     * 터치하면 화면 중앙을 기준으로 터치한 지점으로 좌우 이동방향을 변경
     *  : ACTION_UP 즉, 손을 떼면 이동을 중지(이동방향과 속도 초기화)
     * @param isPress : ACTION_DOWN 여부
     * @param tx
     */
    public void getIntoAction(boolean isPress, float tx){
        if(isPress){
            m_direction = (tx < m_scrW / 2) ? -1 : 1;
        } else {
            m_direction = 0;
            m_speed = MIN_SPEED;
        }
    }

    /**
     * 공의 좌표와 크기를 Ball로부터 전달받아 패들과의 충돌여부 체크후 반환
     *  : 공이 패들의 좌우 옆면이나 아래에 부딪히면 그 충돌은 무시한다.
     * @param bx : 공의 x좌표
     * @param by : 공의 y좌표
     * @param br : 공의 반지름
     * @return
     */
    public boolean hitTest(float bx, float by, int br){
        if(by > m_y - m_height) { return false; }

        return MathF.getIsCollision(bx, by, br, m_x, m_y, m_width, m_height);
    }

    /**
     * 이동
     *  : Demo 모드일 때, 공이 이동할 때 패들도 함께 이동
     *  : 공을 발사하지 않은 경우, 패들 이동시 공도 함께 이동
     */
    public void moveToNext(){
        if(GameView.getIsDemo()){
            m_x = GameView.getBall().getX();
            return;
        }

        m_speed = MathF.getLerp(m_speed, MAX_SPEED, 2 * DTime.DeltaTime);
        m_x += m_direction * m_speed * DTime.DeltaTime;

        if(GameView.getBall().getIsDestructed()){
            GameView.getBall().reset();
        }
    }

    /**
     * 초기화
     */
    public void reset(){
        m_x = m_scrW / 2;
        m_y = m_scrH * 0.9f;
    }
}

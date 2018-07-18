package org.hyg.breakoutbyseraph0;

import android.graphics.Bitmap;

/**
 * Created by shiny on 2018-03-06.
 */

public class Block {

    // 블록번호, 충돌횟수
    private int m_no;
    private int m_hitCnt;

    // 이미지, 위치, 크기
    private Bitmap m_bmp;
    public Bitmap getBlock() { return m_bmp; }
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    // 점수, 소멸여부
    private int m_score;
    public int getScore() { return m_score; }
    private boolean m_isDestructed;
    public boolean getIsDestructed() { return m_isDestructed; }

    /**
     *
     * @param no : 블럭 번호
     * @param x : 블럭 x좌표
     * @param y : 블럭 y좌표
     */
    public Block(int no, float x, float y){
        m_no = no;
        m_x = x;
        m_y = y;

        m_bmp = CommonResources.getBlock()[no - 1];
        m_width = CommonResources.getBlockWidth();
        m_height = CommonResources.getBlockHeight();

        m_hitCnt = no;
        m_score = no * 10;
    }

    /**
     * 블럭의 어느 경계면과 충돌하였는지 반환
     *  : 공의 좌표, 크기를 Ball 로부터 전달받아 충돌여부, 충돌한 경계면 정보 반환
     * @param bx : 공 x좌표
     * @param by : 공 y좌표
     * @param br : 공 반지름. (충돌 영역은 공 반지름의 60% 로 설정)
     * @return
     *   0 : 충돌하지 않음
     *   1 : 상/하 경계면과 충돌
     *   2 : 좌/우 경계면과 충돌
     *   3 : 모서리와 충돌
     */
    public int getHitTarget(float bx, float by, int br){
        if(!MathF.getIsCollision(bx, by, br, m_x, m_y, m_width, m_height)){
            return 0;
        }

        int result = 3;
        float region = br * 0.6f;

        if((by - region > m_y - m_height && by + region < m_y + m_height)
            && (bx - region < m_x - m_width || bx + region > m_x + m_width)) {
            result = 2;
        } else if(bx - region>= m_x - m_width && bx + region <= m_x + m_width){
            result = 1;
        }

        CommonResources.playSound(m_no);

        if(!GameView.getIsDemo()){
            GameView.setScore(m_no);
            GameView.setScoreFormat();

            if(--m_hitCnt <= 0){
                GameView.setScore(m_score);
                GameView.setHitCount();
                GameView.setScoreFormat();
                m_isDestructed = true;
            }
        }


        return result;
    }
}

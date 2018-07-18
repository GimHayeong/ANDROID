package org.hyg.breakout;

import android.graphics.Bitmap;
import android.graphics.PointF;

import java.util.Random;

/**
 * Created by shiny on 2018-03-06.
 */

public class Ball {

    // 화면 크기
    private int m_scrW, m_scrH;

    Random m_rnd = new Random();

    // 이동방향벡터, 이동속도
    private PointF m_direction = new PointF();
    private float m_speed = 800;
    public float getSpeed() { return m_speed; }

    // 위치, 이미지, 반지름
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private Bitmap m_bmp;
    public Bitmap getBall() { return m_bmp; }
    private int m_r;
    public int getRadius() { return m_r; }

    // 공이 화면 아래를 벗어났는지 여부
    private boolean m_isDestructed = true;
    public boolean getIsDestructed() { return m_isDestructed; }


    public Ball(int w, int h){
        m_scrW = w;
        m_scrH = h;

        m_bmp = CommonResources.getBall();
        m_r = CommonResources.getBallSize();

        reset();
    }

    /**
     * 공 주변을 터치하면 공의 이동방향 설정
     *  : 공이 이동중이면 터치이벤트 무시
     * @param tx : 터치한 x좌표
     * @param ty : 터치한 y좌표
     * @return
     */
    public boolean getIntoAction(float tx, float ty){
        if(!m_isDestructed) return false;

        if(MathF.getIsTouch(m_x, m_y, m_r * 10, tx, ty)){
            setDirection();
            m_isDestructed = false;
        }

        return !m_isDestructed;
    }

    /**
     * 이동
     *  : 공이 화면 아래를 벗어났으면 애니메이션 없음
     *  : 패들이나 블록과 충달하면 이동 애니메이션 없음
     *  : 위쪽 경계면과 닿으면 정반사
     *  : 좌우 경계면과 닿으면 정반사
     *  : 아래쪽 경계면과 닿으면 제거대상
     */
    public void moveToNext(){
        if(m_isDestructed) { return; }

        m_x += m_direction.x * DTime.DeltaTime;
        m_y += m_direction.y * DTime.DeltaTime;

        if(isCollisionBetweenPaddleAndMe() || isCollisionBetweenBlockAndMe()) { return; }

        if(m_y < m_r){
            m_y -= m_direction.y * DTime.DeltaTime;
            m_direction.y = -m_direction.y;
            CommonResources.playSound(0);
        }

        if(m_x < m_r || m_x > m_scrW - m_r){
            m_x -= m_direction.x * DTime.DeltaTime;
            m_direction.x = -m_direction.x;
            CommonResources.playSound(0);
        }

        if(m_y > m_scrH + m_r * 4){
            GameView.getPaddle().reset();
            m_isDestructed = true;

            GameView.setGameOver();
        }
    }

    /**
     * 패들과의 충돌여부
     *  : 공의 정보를 패들로 전달하여 충돌여부 확인
     * @return
     */
    private boolean isCollisionBetweenPaddleAndMe() {
        if(!GameView.getPaddle().hitTest(m_x, m_y, m_r)) { return false; }

        CommonResources.playSound(4);
        if(isClear()) { return true; }

        setDirection();

        if(!GameView.getIsDemo()) { m_speed += 4; }

        return true;
    }


    /**
     * 블럭과의 충돌여부
     * @return
     */
    private boolean isCollisionBetweenBlockAndMe() {

        // 충돌 여부 및 충돌 경계면
        // 0 : 충돌하지 않음
        // 1 : 상/하 경계면과 충돌
        // 2 : 좌/우 경계면과 충돌
        // 3 : 모서리와 충돌
        int hitRegion = 0;

        for(Block itm : GameView.BlockList){
            hitRegion = itm.getHitTarget(m_x, m_y, m_r);
        }

        switch(hitRegion){
            case 1:
                m_direction.y = -m_direction.y;
                break;
            case 2:
                m_direction.x = -m_direction.x;
                break;
            case 3:
                m_direction.x = -m_direction.x;
                m_direction.y = -m_direction.y;
                break;
        }

        if(hitRegion > 0 && !GameView.getIsDemo()){
            m_speed += 2;
        }

        return hitRegion > 0;
    }

    /**
     * 충돌후 반사방향 설정
     *  : 이동방향 30 ~ 150 도 사이
     */
    private void setDirection() {
        float rds = (float)Math.toRadians(m_rnd.nextInt(120) + 30);
        m_direction.set((float)Math.cos(rds) * m_speed
                      , (float)Math.sin(rds) * m_speed);
    }


    /**
     * 초기화
     */
    public void reset() {
        m_isDestructed = true;
        m_x = GameView.getPaddle().getX();
        m_y = GameView.getPaddle().getY() - GameView.getPaddle().getHeight() - m_r;
        m_direction.set(0, 0);
    }

    /**
     * 현재 스테이지의 블록이 모두 제거되었는지 여부
     *  : 모두 제거되었으면 다음 스테이지로 이동하기 위한 스테이지 초기화
     * @return
     */
    public boolean isClear() {

        int cnt = GameView.BlockList.size();

        if(cnt == 0){
            GameView.setStage();
            GameView.initStage();
        }

        return cnt == 0;
    }
}

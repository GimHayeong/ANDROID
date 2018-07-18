package org.hyg.huntingbirdbyseraph0;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by shiny on 2018-02-21.
 */

public class Bird {
    // 화면크기
    protected int m_width, m_height;

    // 터치영역
    protected RectF m_rect = new RectF();

    // 이동 속도
    protected int m_speed;

    // 이동 방향 벡터
    protected PointF m_direction = new PointF();

    // 프레임 지연시간
    protected float m_aniTime;

    // 경과시간
    protected float m_aniSpan = 0;

    // 이미지 번호
    protected int m_aniBmpIdx = 0;

    // 공용리소스 대체로 삭제
    //protected Bitmap[] m_birds = new Bitmap[6];

    // 새 위치
    protected float m_bX, m_bY;

    // 새 크기
    protected int m_bW, m_bH;

    protected Bitmap m_bird;

    // 추락시 회전각도
    protected int m_angle = 0;

    // 사냥여부
    protected boolean m_isDead;

    public float getBirdX() { return m_bX; }
    //protected void setBirdX(float x) { m_bX = x; }
    public float getBirdY() { return m_bY; }
    //protected void setBirdY(float y) { m_bY = y; }
    public int getBirdWidth() { return m_bW; }
    //protected void setBirdWidth(int width) { m_bW = width; }
    public int getBirdHeight() { return m_bH; }
    //protected void setBirdHeight(int height) { m_bH = height; }
    public Bitmap getBird() { return m_bird; }
    //protected void setBird(Bitmap bmp) { m_bird = bmp;}
    public int getAngle() { return m_angle; }
    //protected void setAngle(int angle) { m_angle = angle; }
    public boolean getIsDead() { return m_isDead; }
    //protected void setIsDead(boolean isDead) { m_isDead = isDead; }

    public void moveToNext(){
        m_bX += m_direction.x * BirdTime.deltaTime;
        m_bY += m_direction.y * BirdTime.deltaTime;

        startAnimation();

        // 화면을 벗어나면 사냥완료 처리
        if(m_bX > m_width + m_bW || m_bY > m_height + m_bH){
            m_isDead = true;
        }
    }

    private void startAnimation() {
        m_aniSpan += BirdTime.deltaTime;

        if(m_direction.y > 0 || m_aniSpan < m_aniTime) { return; }

        m_aniSpan = 0;
        m_aniBmpIdx++;

        if(m_aniBmpIdx >= 5) { m_aniBmpIdx = 0; }

        // 공용리소스 대체로 변경
        //== m_bird = m_birds[m_aniBmpIdx];
        m_bird = CommonBird.Birds[m_aniBmpIdx];
    }

    public boolean hitTest(float px, float py){
        if(m_direction.y > 0) { return false; }



        // 터치 영역내이면 사냥성공
        if(hitCollision(px, py)){

            m_direction.y = m_speed;

            // 사냥성공 설정
            m_direction.x = 0;

            // 추락
            m_angle = 180;

            // 추락하는 새 날개 접기
            m_bird = CommonBird.Birds[0];
        }

        return m_direction.x == 0;
    }

    private boolean hitRect(float px, float py){
        // 새 위치에 새 크기의 Rect 생성
        m_rect.set(m_bX - m_bW, m_bY - m_bH, m_bX + m_bW, m_bY + m_bH);

        return m_rect.contains(px, py);
    }

    private boolean hitCollision(float px, float py){

        // 새의 중심부에 새 높이의 70% 크기의 원을 Collision으로 설정
        float collision = m_bH * m_bH * 0.7f;

        float dist = (px - m_bX) * (px - m_bX) + (py - m_bY) * (py - m_bY);

        return dist < collision;
    }

}

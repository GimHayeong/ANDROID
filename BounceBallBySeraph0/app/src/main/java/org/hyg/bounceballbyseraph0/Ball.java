package org.hyg.bounceballbyseraph0;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by shiny on 2018-02-22.
 */

public class Ball {

    // 화면 크기
    private int m_width, m_height;

    // 바닥 높이
    private float m_groundHt;

    // 이동 속도 : 초당 이동 픽셀
    private int m_speed = 300;

    // 회전 속도(초) : 초당 회전 각도
    private int m_rotationSpeed = 120;

    // 중력 : 추락시 적용 중력 픽셀
    private float m_gravity = 1500f;

    // 반발
    private float m_bounce = 0.8f;

    // 이동방향벡터
    private PointF m_direction = new PointF();

    // 공의 위치
    private float m_bX, m_bY;
    public float getBallX() { return m_bX; }
    public float getBallY() { return m_bY; }

    // 공의 반지름
    private int m_bR;
    public int getBallRadius() { return m_bR; }

    // 현재 각도
    private float m_angle;
    public float getBallAngle() { return m_angle; }

    // 현재 이미지
    private Bitmap m_ball;
    public Bitmap getBall() { return m_ball; }

    // 소멸 여부
    private boolean m_isDead;
    public boolean getIsDead() { return m_isDead; }

    public Ball(int width, int height, float px, float py){
        m_width = width;
        m_height = height;
        m_bX = px;
        m_bY = py;

        m_ball = CommonBall.SmBall;
        m_bR = CommonBall.SmRadius;

        m_groundHt = height * 0.8f;

        m_direction.set(m_speed, 0);
    }

    public void moveToNext(){
        m_angle += m_rotationSpeed * BallTime.SmDeltaTime;

        m_direction.y += m_gravity * BallTime.SmDeltaTime;

        m_bX += m_direction.x * BallTime.SmDeltaTime;
        m_bY += m_direction.y * BallTime.SmDeltaTime;

        // 바닥과 충돌하면 반대방향으로 튕기기
        if (m_bY > m_groundHt) {
            m_bY = m_groundHt;

            m_direction.y = -m_direction.y * m_bounce;
        }

        // 화면을 벗어나면 제거
        m_isDead = (m_bX > m_width + m_bR);
    }


}

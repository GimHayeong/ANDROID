package org.hyg.parkalonebyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import java.util.Random;

/**
 * Created by shiny on 2018-02-26.
 */

public class Ball {

    // 화면 크기
    private int m_scrW, m_scrH;

    // 공의 이동속도, 중력
    private float m_speed;
    private final float GRAVITY = 800;

    // 이동방향 벡터
    private PointF m_direction = new PointF();

    // 현지위치
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    // 반지름
    private int m_radius;
    public int getRadius() { return m_radius; }
    // 회전각도
    private int m_angle;
    public int getAngle() { return m_angle; }

    // 이미지
    private Bitmap m_bmp;
    public Bitmap getBall() { return m_bmp; }

    // 그림자 이미지, 크기, 축소비율
    private Bitmap m_bmpShadow;
    public Bitmap getShadow() { return m_bmpShadow; }
    private int m_sw, m_sh;
    public int getShadowWidth() { return m_sw; }
    public int getShadowHeight() { return m_sh; }
    private float m_sScale;
    public float getShadowScale() { return m_sScale; }

    // 지면 높이
    private float m_groundH;
    public float getGroundHeight() { return m_groundH; }

    public Ball(Context context, int width, int height){
        m_scrW = width;
        m_scrH = height;

        m_groundH = height * 0.9f;

        initResources(context);
        init();
    }

    /**
     * 수직 이동방향(공이 떨어질 때)에 중력 누적
     * 회전각에 이동속도를 반영하면 이동속도에 비례하여 회전속도도 증감
     * 화면을 벗어나면 초기화
     */
    public void moveToNext(){
        m_direction.y += GRAVITY * DTime.DeltaTime;
        m_angle += m_direction.x * m_speed + DTime.DeltaTime;

        // 수평 이동
        m_x += m_direction.x * m_speed * DTime.DeltaTime;
        // 수직 이동
        m_y += m_direction.y * DTime.DeltaTime;

        checkCollision();
        checkIsLanded();

        if(m_x < -m_radius * 4 || m_x > m_scrW + m_radius * 4){
            init();
        }
    }

    /**
     * 바닥에 닿으면 100% 반사
     * 그림자 축소비율 설정
     */
    private void checkIsLanded(){
        if(m_y > m_groundH - m_radius){
            m_y = m_groundH - m_radius;
            m_direction.y = -m_direction.y;
        }

        m_sScale = m_y / (m_groundH - m_radius);
    }

    /**
     * 충돌 판정
     * 소년과 충돌하면 반사
     */
    private void checkCollision(){
        if(GameView.sm_boy.getIsCollision(m_x, m_y, m_radius)){
            m_direction.x = -m_direction.x;
        }
    }

    /**
     * 리소스 초기화
     * @param context
     */
    private void initResources(Context context) {
        m_bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
        m_radius = m_bmp.getWidth() / 2;

        m_bmpShadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.shadow);
        m_sw = m_bmpShadow.getWidth() / 2;
        m_sh = m_bmpShadow.getHeight() / 2;
    }

    /**
     * 좌표, 이동속도, 이동방향 초기화
     * 바닥에 충돌한 공은 600 ~ 700 까지 튀어 오른다.
     */
    private void init() {
        Random rnd = new Random();

        if(rnd.nextInt(2) == 1){
            m_direction.x = 1;
            m_x = -m_radius * 4;
        } else {
            m_direction.x = -1;
            m_x = m_scrW + m_radius * 4;
        }

        m_y = rnd.nextInt(101) + 600;
        m_speed = rnd.nextInt(101) + 400;
        m_direction.y = 0;
    }



}

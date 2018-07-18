package org.hyg.rocketbyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

/**
 * Created by shiny on 2018-02-22.
 */

public class Rocket {

    // 화면크기
    private int m_scrnWt, m_scrnHt;

    // 이동속도
    private int m_speed = 1000;
    // 중력
    private float m_gravity = 400f;
    // 이동(발사)방향벡터
    private PointF m_direction = new PointF();

    // 로켓 위치
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }

    // 로켓 크기
    private int m_wt, m_ht;
    public int getWidth() { return m_wt; }
    public int getHeight() { return m_ht; }

    // 회전각
    private float m_angle;
    public float getAngle() { return m_angle; }

    // 로켓이미지
    private Bitmap m_rocket;
    public Bitmap getRocket() { return m_rocket; }

    public Rocket(Context context, int width, int height){
        m_scrnWt = width;
        m_scrnHt = height;

        makeBitmap(context);

        m_wt = m_rocket.getWidth() / 2;
        m_ht = m_rocket.getHeight() / 2;

        init();
    }

    // 해상도에 관계없이 원본 크기 표시
    // ==> 해상도(XX-High Density)를 설정하고 해상도에 따라 일정한 비율로 이미지 조절
    private void makeBitmap(Context context){
        m_rocket = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket);
    }

    // 해상도에 따라 이미지 크기 조절
    // ==> 비트맵 확대금지 옵션 사용
    private void makeBitmapWithInScaledOption(Context context){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        m_rocket = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket, options);
    }

    // 로켓 위치, 회전각 초기화
    private void init() {
        m_x = m_wt;
        m_y = m_scrnHt - m_ht;

        m_angle = 0;
    }

    // 로켓 발사
    public void launch(float px, float py){
        double radius = -Math.atan2(py - m_y - m_ht
                                  , px - m_x);

        m_direction.set((float)Math.cos(radius) * m_speed
                      , -(float)Math.sin(radius) * m_speed);
    }

    // 로켓 이동
    public boolean moveToNext(){
        boolean isRun = true;

        m_direction.y += m_gravity * DTime.DeltaTime;

        m_x += m_direction.x * DTime.DeltaTime;
        m_y += m_direction.y * DTime.DeltaTime;

        double radius = -Math.atan2(m_direction.y
                                  , m_direction.x);
        m_angle = 90 - (float)Math.toDegrees(radius);

        if(m_x > m_scrnWt + m_wt * 2 || m_y > m_scrnHt + m_ht * 2) {
            init();

            isRun = false;
        }

        return isRun;
    }
}

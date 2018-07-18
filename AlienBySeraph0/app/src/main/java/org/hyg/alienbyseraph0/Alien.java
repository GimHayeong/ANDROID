package org.hyg.alienbyseraph0;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by shiny on 2018-02-24.
 */

public class Alien {

    // 화면 크기
    private int m_scrW, m_scrH;

    // 속도
    private final float MAX_SPEED = 1000;
    private float m_speed = 0;

    // 이동방향, 목적지
    private PointF m_direction = new PointF(0, -1);
    private PointF m_posTarget = new PointF();

    // 출발할 수 있는가?
    private boolean isReady;

    // 위치, 크기, 회전각
    private PointF m_pos = new PointF();
    public PointF getPos() { return m_pos; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }
    private float m_angle;
    public float getAngle() { return m_angle; }

    // 우주선 이미지
    private Bitmap m_bmp;
    public Bitmap getAlien() { return m_bmp; }

    public Alien(int width, int height) {
        m_scrW = width;
        m_scrH = height;

        m_bmp = CommonResources.getAlien();
        m_width = CommonResources.getAlienWidth();
        m_height = CommonResources.getAlienHeight();

        // 초기위치 (화면 중앙)
        m_pos.set(width / 2, height / 2);
    }

    public void moveToNext() {
        // 출발 준비되었으면 우주선 점점 가속
        if(isReady){
            m_speed = MathF.getLerp(m_speed, MAX_SPEED, 3 * DTime.DeltaTime);
        }

        // 목적지 근처에 도착할때까지 점점 감속
        if(MathF.getDistance(m_pos, m_posTarget) < 50){
            isReady = false;
            m_speed = MathF.getLerp(m_speed, 0, 15 * DTime.DeltaTime);
        }

        // 우주선 이동
        m_pos.x += m_direction.x * m_speed * DTime.DeltaTime;
        m_pos.y += m_direction.y * m_speed * DTime.DeltaTime;
    }

    /**
     * 우주선 내부를 터치하면 레이저 발사하고 아니면 목적지로 이동
     * @param tx : 터치한 x좌표
     * @param ty : 터치한 y좌표
     */
    public void getIntoAction(float tx, float ty){
        // 우주선을 터치하면 레이저 발사
        if(MathF.getIsTouch(m_pos.x, m_pos.y, m_width * 0.9f, tx, ty)) {
            fire();
        } else {
            setStart(tx, ty);
        }
    }

    /**
     * 새로운 목적지로 출발 준비
     * @param tx : 목적지 x좌표
     * @param ty : 목적지 y좌표
     */
    private void setStart(float tx, float ty){
        m_posTarget.set(tx, ty);

        m_direction.set(MathF.getDirection(m_pos, m_posTarget));
        m_angle = MathF.getDegreeCW(m_pos, m_posTarget);

        isReady = true;
    }

    private void fire(){
        CommonResources.playSound();

        synchronized (GameView.LaserList){
            GameView.LaserList.add(new Laser(m_scrW, m_scrH, m_pos, m_direction, m_angle));
        }
    }
}

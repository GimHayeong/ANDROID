package org.hyg.destoryalienbyseraph0;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by shiny on 2018-02-28.
 */

public class Torpedo {

    // 화면크기
    private int m_scrW, m_scrH;

    // 이동속도, 이동방향벡터, 목표물 좌표
    private final int SPEED = 800;
    private PointF m_direction = new PointF();
    private PointF m_posTarget = new PointF();

    // 현재위치, 크기, 회전각
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private int m_width, m_height;
    public int getWidth() { return m_width;}
    public int getHeight() { return m_height; }
    private float m_angle;
    public float getAngle() { return m_angle; }

    // 이미지, 소멸여부
    private Bitmap m_bmp;
    public Bitmap getTorpedo() { return m_bmp; }
    private boolean m_isDestructed = false;
    public boolean getIsDestructed() { return m_isDestructed; }

    public Torpedo(int width, int height, float px, float py){
        m_scrW = width;
        m_scrH = height;

        m_x = px;
        m_y = py;

        m_bmp = CommonResources.getTorpedo();
        m_width = CommonResources.getTorpedoWidth();
        m_height = CommonResources.getTorpedoHeight();

        getTargetInfo();

    }

    /**
     * 이동
     *  : 화면을 벗어난 어뢰는 소멸대상
     */
    public void moveToNext(){
        checkCollision();

        m_x += m_direction.x * SPEED * DTime.DeltaTime;
        m_y += m_direction.y * SPEED * DTime.DeltaTime;

        if(m_x < -m_width || m_x > m_scrW + m_height || m_y < -m_height || m_y > m_scrH + m_height){
            m_isDestructed = true;
        }
    }

    /**
     * 전투기와 충돌 체크
     *  : 어뢰 자신의 위치와 크기 정보를 전투기의 충돌함수로 전달해 충돌여부 확인
     *    전투기와 충돌한 어뢰는 소멸대상
     *    (전투기는 파괴되지 않음)
     */
    private void checkCollision() {
        if(GameView.getXWing().checkCollision(m_x, m_y, m_height)){
            m_isDestructed = true;
        }
    }


    /**
     * 전투기 타겟의 정보를 확인하여 해당 위치로 이동하기 위한 방향, 회전각 설정
     */
    private void getTargetInfo() {
        m_posTarget.set(GameView.getXWing().getX()
                      , GameView.getXWing().getY());

        PointF pos = new PointF(m_x, m_y);
        m_direction.set(MathF.getDirection(pos, m_posTarget));
        m_angle = MathF.getDegreeCW(pos, m_posTarget);

    }
}

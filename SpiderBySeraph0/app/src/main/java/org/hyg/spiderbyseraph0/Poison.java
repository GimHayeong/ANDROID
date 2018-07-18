package org.hyg.spiderbyseraph0;

import android.graphics.Bitmap;
import android.widget.GridView;

/**
 * Created by shiny on 2018-02-26.
 */

public class Poison {
    // 이동 속도 픽셀
    private int m_speed = 1200;
    // 현재 위치
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    // 크기
    private int m_radius;
    public int getSize() { return m_radius; }
    // 이미지
    private Bitmap m_bmp;
    public Bitmap getPoison() { return m_bmp; }
    // 소멸여부
    private boolean m_isDestructed;
    public boolean getIsDestructed() { return m_isDestructed; }

    public Poison(float tx, float ty){
        m_x = tx;
        m_y = ty;

        m_bmp = CommonResources.getPoison();
        m_radius = CommonResources.getPoisonSize();
    }

    public void moveToNext(){
        m_y -= m_speed * DTime.DeltaTime;
        m_isDestructed = (m_y < -m_radius);

        /*================= 나비 추가관련 변수 */
        checkCollision();
        /* 나비 추가관련 변수 =================*/
    }

    /**
     * 나비와 충돌했으면 소멸 설정
     * 동기화대상 컬렉션객체의 다른 요소에 영향이 없는 함수 호출은 동기화 불필요(?)
     */
    private void checkCollision() {
        /*synchronized (GameView.ButterflyList){
            for (Butterfly itm : GameView.ButterflyList) {
                if (itm.checkCollision(m_x, m_y, m_radius)) {
                    m_isDestructed = true;
                    break;
                }
            }
        }*/
        for (Butterfly itm : GameView.ButterflyList) {
            if (itm.checkCollision(m_x, m_y, m_radius)) {
                m_isDestructed = true;
                break;
            }
        }
    }
}

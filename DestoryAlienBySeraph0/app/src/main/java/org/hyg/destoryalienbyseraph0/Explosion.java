package org.hyg.destoryalienbyseraph0;

import android.graphics.Bitmap;

/**
 * Created by shiny on 2018-02-28.
 */

public class Explosion {

    // 폭파불꽃 지연시간, 이전프레임으로부터의 경과시간, 이미지 인덱스
    private float m_aniDelay = 0.04f;
    private float m_aniSpan;
    private int m_bmpIdx;

    // 현재위치, 크기
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height;}

    // 이미지, 소멸여부
    private Bitmap m_bmp;
    public Bitmap getExplosion() { return m_bmp; }
    private boolean m_isDestructed;
    public boolean getIsDestructed() { return m_isDestructed; }


    public Explosion(float tx, float ty, String kind){
        m_x = tx;
        m_y = ty;

        if(kind == "Small"){
            m_bmpIdx = 20;
            m_aniDelay = 0.1f;
        }

        m_bmp = CommonResources.getExplosion()[m_bmpIdx];
        m_width = CommonResources.getExplosionWidth();
        m_height = CommonResources.getExplosionHeight();
    }

    /**
     * 불꽃애니메이션
     *  : 마지막 이미지(24번째)이면 소멸대상
     */
    public void moveToNext(){
        m_aniSpan += DTime.DeltaTime;
        if(m_aniSpan > m_aniDelay) {
            m_aniSpan = 0;
            m_bmpIdx++;

            m_bmp = CommonResources.getExplosion()[m_bmpIdx];
            m_isDestructed = (m_bmpIdx == 24);
        }
    }

}

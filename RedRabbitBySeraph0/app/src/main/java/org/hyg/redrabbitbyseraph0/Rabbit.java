package org.hyg.redrabbitbyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by shiny on 2018-03-02.
 */

public class Rabbit {

    // 화면크기
    private int m_scrW, m_scrH;

    // 이동속도, 걷기여부
    private final int MAX_SPEED = 400;
    private int m_speed;
    private boolean m_isWalk;

    // 이미지
    private Bitmap[] m_bmpRabbits = new Bitmap[8];
    private int m_cntImages = 8;

    // 애니메이션 지연시간, 이전프레임으로부터의 경과시간, 이미지인덱스
    private float m_aniTime = 0;
    private float m_aniSpan = 0.1f;
    private int m_bmpIdx = 0;

    // 현재위치, 이동방향(1, -1), 지면높이
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private int m_direction = 1;
    public int getDirection() { return m_direction; }
    private float m_groundH;
    public float getGroundHeight() { return m_groundH; }

    // 현재이미지
    private Bitmap m_bmp;
    public Bitmap getRabbit() { return m_bmp; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    // 그림자
    private Bitmap m_bmpShadow;
    public Bitmap getShadow() { return m_bmpShadow; }
    private int m_sw, m_sh;
    public int getShadowWidth() { return m_sw; }
    public int getShadowHeight() { return m_sh; }

    public Rabbit(Context context, int w, int h){
        m_scrW = w;
        m_scrH = h;

        setBitmap(context);

        m_groundH = m_scrH * 0.9f;

        m_x = m_scrW / 3;
        m_y = m_groundH - m_height;
    }

    /**
     * 그림자 이미지를 만든다.
     * 토끼의 4가지 동작이 연결된 하나의 이미지를 분할하여 이미지를 생성하면서 같은 이미지의 복사본을 만든다.
     * @param context
     */
    public void setBitmap(Context context) {
        m_bmpShadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.shadow);
        m_sw = m_bmpShadow.getWidth() / 2;
        m_sh = m_bmpShadow.getHeight() / 2;

        Bitmap bmpOrigin = BitmapFactory.decodeResource(context.getResources(), R.drawable.rabbit);
        m_width = bmpOrigin.getWidth() / 4;
        m_height = bmpOrigin.getHeight();

        for(int i=0; i<4; i++){
            m_bmpRabbits[i] = Bitmap.createBitmap(bmpOrigin, m_width * i, 0, m_width, m_height);
            m_bmpRabbits[i + 4] = m_bmpRabbits[i];
        }

        m_width /= 2;
        m_height /= 2;
        m_bmp = m_bmpRabbits[0];
    }

    /**
     * 걷기 상태일때만 애니메이션
     * 화면을 벗어나면 이동 방향을 바꾸고 속도를 줄임
     */
    public void moveToNext(){
        if(m_isWalk){
            startAnimation();
        }

        m_x += m_direction * m_speed * DTime.DeltaTime;

        if(m_x < m_width || m_x > m_scrW - m_width){
            m_x -= m_direction * m_speed * DTime.DeltaTime;
            m_speed = 0;
        }
    }

    /**
     * 터치한 지점쪽으로 이동
     * @param tx
     */
    public void getIntoAction(float tx){
        m_direction = (m_x < tx) ? 1  : -1;
        m_speed = MAX_SPEED;
        m_isWalk = true;
    }

    /**
     * 애니메이션
     * 마지막 애니메이션이면 재생후 정지
     */
    private void startAnimation() {
        m_aniTime += DTime.DeltaTime;

        if(m_aniTime < m_aniSpan) { return; }

        m_aniTime = 0;
        m_bmpIdx = MathF.getRepeatIdx(m_bmpIdx, m_cntImages);

        if(m_bmpIdx == 0){
            m_speed = 0;
            m_isWalk = false;
        }

        m_bmp = m_bmpRabbits[m_bmpIdx];
    }


}

package org.hyg.runninjabyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

/**
 * Created by shiny on 2018-03-02.
 */

public class Ninja {


    // 동작상태
    protected enum STATE { JUMP, RUN, IDLE };
    protected STATE m_state = STATE.RUN;

    // 점프속도, 중력
    protected int m_speedJump = 1200;
    protected int m_gravity = 2000;

    // 이미지 (8개의 이미지가 한줄로 연결된 것이 2줄)
    protected final int IMG_COUNT = 8;
    protected Bitmap[][] m_bmpNinjas = new Bitmap[2][IMG_COUNT];

    // 애니메이션 지연시간, 이전프레임으로부터의 경과시간(1초당 12프레임의 속도), 이미지인덱스
    protected float m_aniTime = 0;
    protected float m_aniSpan = 1f / 12;
    protected int m_bmpIdx;

    // 현재위치, 이동방향벡터, 지면높이
    protected float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    protected PointF m_direction = new PointF(1, 0);
    public float getDirectionX() { return m_direction.x; }
    protected float m_groundH;

    // 현재이미지
    protected Bitmap m_bmp;
    public Bitmap getNinja() { return m_bmp; }
    protected int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }


    /**
     *
     * @param context
     * @param width : 화면크기
     * @param height : 화면크기
     */
    public Ninja(Context context, int width, int height){
        setBitmap(context);

        m_groundH = height * 0.9f;

        m_x = width / 2;
        m_y = m_groundH - m_height;
    }

    /**
     * 애니메이션
     *  : 제자리 달리기를 하므로 달리는 상태에서는 특별한 애니메이션이 없으므로 리턴
     *  : 착지이면 달리는 상태로 초기화
     */
    public void moveToNext(){
        startAnimation();

        if(m_state == STATE.RUN) { return; }

        m_direction.y += m_gravity * DTime.DeltaTime;
        m_y += m_direction.y * DTime.DeltaTime;

        if(m_y > m_groundH - m_height){
            m_y = m_groundH - m_height;
            m_state = STATE.RUN;
            m_bmpIdx = 0;
        }
    }

    /**
     * 닌자를 터치하면 점프, 다른 곳을 터치하면 터치한 지점 쪽으로 이동
     * 점프 상태일 때는 점프가 끝날 때까지 대기
     * @param tx : 터치한 x좌표
     * @param ty : 터치한 y좌표
     */
    public void getIntocAction(float tx, float ty){

        if(m_state == STATE.JUMP) { return; }

        if(MathF.getIsTouch(m_x, m_y, (float)m_height, tx, ty)){
            m_direction.y = -m_speedJump;
            m_state = STATE.JUMP;
            m_bmpIdx = 0;
        } else {
            m_direction.x = (m_x < tx) ? 1 : -1;
        }
    }

    /**
     * 애니메이션 (해당 상태의 다음 이미지 애니메이션)
     *  : 점프일때는 착지때까지 마지막 동작유지
     */
    private void startAnimation() {

        m_aniTime += DTime.DeltaTime;

        if(m_aniTime > m_aniSpan){
            m_aniTime = 0;
            m_bmpIdx = MathF.getRepeatIdx(m_bmpIdx, IMG_COUNT);

            if(m_state == STATE.JUMP && m_bmpIdx == 0){
                m_bmpIdx = IMG_COUNT - 1;
            }
        }

        m_bmp = m_bmpNinjas[m_state.ordinal()][m_bmpIdx];
    }


    private void setBitmap(Context context) {

        Bitmap bmpOrigin = BitmapFactory.decodeResource(context.getResources(), R.drawable.ninja);

        m_width = bmpOrigin.getWidth() / IMG_COUNT;
        m_height = bmpOrigin.getHeight() / 2;

        for(int i=0; i<2; i++){
            for(int j=0; j<IMG_COUNT; j++){
                m_bmpNinjas[i][j] = Bitmap.createBitmap(bmpOrigin, m_width * j, m_height * i, m_width, m_height);
            }
        }

        m_width /= 2;
        m_height /=2;
        m_bmp = m_bmpNinjas[m_state.ordinal()][0];

    }

}

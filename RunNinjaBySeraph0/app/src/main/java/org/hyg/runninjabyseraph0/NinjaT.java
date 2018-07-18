package org.hyg.runninjabyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

/**
 * Created by shiny on 2018-03-05.
 */

public class NinjaT extends Ninja {

    //점프카운트, 점프중여부, 이동방향 왼쪽여부
    private int m_jumpCnt = 0;
    private boolean m_isJump;
    private boolean m_isLeft;
    public boolean getIsLeft() { return m_isLeft; }

    /**
     *
     * @param context
     * @param width : 화면크기
     * @param height : 화면크기
     */
    public NinjaT(Context context, int width, int height){
        super(context, width, height);
    }

    /**
     * 애니메이션
     *  : 제자리 달리기를 하므로 달리는 상태에서는 특별한 애니메이션이 없으므로 리턴
     *  : 착지이면 달리는 상태로 초기화
     */
    @Override
    public void moveToNext(){
        startAnimation();

        if(!m_isJump) { return; }

        m_direction.y += m_gravity * DTime.DeltaTime;
        m_y += m_direction.y * DTime.DeltaTime;

        if(m_y > m_groundH - m_height){
            m_y = m_groundH - m_height;
            m_jumpCnt = 0;
            m_isJump = false;
            m_bmpIdx = 0;
        }
    }

    /**
     * 닌자를 터치하면 점프, 다른 곳을 터치하면 터치한 지점 쪽으로 이동
     * 점프 상태일 때는 점프가 끝날 때까지 대기
     * @param btnLeft : Left 버튼 터치여부
     * @param btnRight : Right 버튼 터치여부
     * @param btnJump : Jump 버튼 터치여부
     */
     public void getIntoAction(boolean btnLeft, boolean btnRight, boolean btnJump){
        m_state = STATE.RUN;
        if(btnLeft) {
            m_direction.x = -1;
            m_isLeft = true;
        }

        if(btnRight){
            m_direction.x = 1;
            m_isLeft = false;
        }

        /*
        정지하는 조건을 제거하여 계속 움직이도록 한다.
        if(!btnLeft && !btnRight){
            m_direction.x = 0;
            m_state = STATE.IDLE;
        }
        */

        if(btnJump && m_jumpCnt < 2){
            m_direction.y = -m_speedJump;
            m_bmpIdx = 0;
            m_jumpCnt++;

            m_state = STATE.JUMP;
            m_isJump = true;
        }
    }

    /**
     * 애니메이션 (해당 상태의 다음 이미지 애니메이션)
     *  : 정지상태일때는 고정된 이미지 표시
     *  : 점프일때는 착지때까지 마지막 동작유지
     */
    private void startAnimation() {

        m_aniTime += DTime.DeltaTime;

        if(m_aniTime <= m_aniSpan) { return; }

        if(m_direction.x == 0 && !m_isJump){
            m_bmp = m_bmpNinjas[1][1];
            return;
        }

        m_aniTime = 0;
        m_bmpIdx = MathF.getRepeatIdx(m_bmpIdx, IMG_COUNT);

        if(m_isJump){
            if(m_bmpIdx == 0){
                m_bmpIdx = IMG_COUNT - 1;
            }
            m_state = STATE.JUMP;
        } else {
            m_state = STATE.RUN;
        }
        m_bmp = m_bmpNinjas[m_state.ordinal()][m_bmpIdx];
    }



}

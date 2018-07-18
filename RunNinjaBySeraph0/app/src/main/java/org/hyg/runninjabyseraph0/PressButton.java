package org.hyg.runninjabyseraph0;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Created by shiny on 2018-03-05.
 */

public class PressButton {
    // 버튼이미지, 위치, 터치여부
    private Bitmap m_bmp;
    public Bitmap getButton() { return m_bmp; }
    private int m_x, m_y;
    public int getX() { return m_x; }
    public int getY() { return m_y; }
    private boolean m_isTouch = false;
    public boolean getIsTouch() { return m_isTouch; }

    // 터치 아이디, 터치 판정 영역
    private int m_ptrId = -1;
    private RectF m_rect;

    public PressButton(Bitmap bmp, Point pos){
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        m_bmp = bmp;

        m_rect = new RectF(pos.x, pos.y, pos.x + w, pos.y + h);

        m_x = pos.x;
        m_y = pos.y;
    }

    /**
     * _DOWN이벤트이고 터치한 지점이 터치 판정 영역이면 이벤트를 발생시킨 버튼이 터치한 것으로 인식
     * _UP이벤트이고 이벤트를 발생시킨 아이디가 이전 _DOWN이벤트를 일으킨 버튼이라면 터치해제한 것으로 인식
     * @param ptrId : 이벤트를 발생시킨 버튼아이디
     * @param isDown : _DOWN 이벤트 여부
     * @param tx : 터치한 x좌표
     * @param ty : 터치한 y좌표
     */
    public void getIntoAction(int ptrId, boolean isDown, float tx, float ty){
        if(isDown && m_rect.contains(tx, ty)){
            m_isTouch = true;
            m_ptrId = ptrId;
        }

        if(!isDown && ptrId == m_ptrId){
            m_isTouch = false;
        }
    }


    /**
     * 멀티 터치 처리
     * @param event
     * @return
     */
    private boolean setMultiTouch(MotionEvent event, PressButton[] pbButtons){
        boolean isTouch = false;

        // 터치 이벤트를 구분하기 위한 마스크 연산결과
        int action = event.getActionMasked();
        //::: 호환성 목적 라이브러리 사용시 MotionEventCompat 사용
        //::: int action = MotionEventCompat.getActionMasked(event);

        switch(action){
            // 첫번째 손가락이 화면에 닿을 때
            // 두번째 이후의 손가락이 화면에 닿을 때
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                isTouch = true;
                break;

            // 첫번째 손가락이 화면에서 떨어졌을 때
            // 두번째 이후의 손가락이 화면에서 떨어졌을 때
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                isTouch = false;
                break;

            // 화면의 손가락이 3~5픽셀 이상 이동했을 때 (무시)
            // 기타 이벤트는 무시
            case MotionEvent.ACTION_MOVE:
            default:
                return true;

        }

        // 포인터(터치) 인덱스(이벤트 순서값), 터치 아이디
        int ptrIdx = event.getActionIndex();
        //::: 호환성 목적 라이브러리 사용시 MotionEventCompat 사용
        //::: int ptrIdx = MotionEventCompat.getActionIndex(event);
        int touchId = event.getPointerId(ptrIdx);
        //::: 호환성 목적 라이브러리 사용 MotionEventCompat 사용시
        //::: int touchId = MotionEventCompat.getPointerId(event);

        // 터치 좌표
        float touchX = event.getX(ptrIdx);
        //::: 호환성 목적 라이브러리 사용시 MotionEventCompat 사용
        //::: float touchX = MotionEventCompat.getX(event, ptrIdx);
        float touchY = event.getY(ptrIdx);
        //::: 호환성 목적 라이브러리 사용시 MotionEventCompat 사용
        //::: float touchY = MotionEventCompat.getY(event, ptrIdx);

        // 각각의 버튼에 통지 (터치 아이디로 현재 터치가 각각의 버튼 자신에 해당하는 것인지 구분)
        for(int i=0; i<pbButtons.length; i++){
            pbButtons[i].getIntoAction(touchId, isTouch, touchX, touchY);
        }
        //btnLeft.action(tocuhId, isTouch, touchX, touchY);
        //btnRight.action(touchId, isTouch, touchX, touchY);
        //btnJump.action(touchId, isTouch, touchX, touchY);

        return true;
    }


}

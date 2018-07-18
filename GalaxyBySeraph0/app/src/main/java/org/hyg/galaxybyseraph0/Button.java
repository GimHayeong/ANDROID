package org.hyg.galaxybyseraph0;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;

/**
 * Created by shiny on 2018-03-05.
 */

public class Button {

    // 버튼이미지, 위치, 터치여부
    private Bitmap m_bmp;
    public Bitmap getButton() { return m_bmp; }
    private int m_x, m_y;
    public int getX() { return m_x; }
    public int getY() { return m_y; }
    private boolean m_isTouch;
    public boolean getIsTouch() { return m_isTouch; }

    // 터치 아이디, 터치 판정 영역
    private int m_ptrId = -1;
    private RectF m_rect;

    public Button(Bitmap bmp, Point pos){
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

}

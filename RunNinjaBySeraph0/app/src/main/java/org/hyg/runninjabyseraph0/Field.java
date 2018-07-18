package org.hyg.runninjabyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by shiny on 2018-03-02.
 */

public class Field {

    // 화면 크기
    protected int m_scrW, m_scrH;

    // 스크롤 속도
    protected int m_speed;

    // 이미지, 갯수, 현재 이미지인덱스, 다음 이미지인덱스
    protected Bitmap[] m_bmpFields = new Bitmap[2];
    private int m_bmpCnt = 2;
    private int m_currentIdx, m_nextIdx;

    // 이동방향, offset
    private int m_direction;
    private float m_offset;

    protected float m_py = 0;


    /*public Field(Context context, int width, int height){
        this(context, width, height, 50, R.drawable.far0, (int)(height * 0.6f), 0);
    }*/

    /**
     *
     * @param context
     * @param width : 화면 크기
     * @param height : 화면 크기
     */
    public Field(Context context, int width, int height){
        m_scrW = width;
        m_scrH = height;
    }


    /**
     *
     * @param context
     * @param width : 화면 크기
     * @param height : 화면 크기
     * @param speed : 배경 이미지의 좌우로 이동속도
     * @param imgId : 배경 이미지 아이디
     * @param imgH : 배경 이미지 높이 (넓이는 화면 넓이와 동일하므로 매개변수로 받지 않음)
     * @param py : 배경 이미지 초기 y좌표 (x 좌표는 0이므로 매개변수로 받지 않음)
     */
    public Field(Context context, int width, int height, int speed, int imgId, int imgH, float py){
        m_scrW = width;
        m_scrH = height;

        m_speed = speed;
        m_py = py;

        setBitmap(context, imgId, imgH);
    }

    public void moveToNext(){
        scrollHorizontal();
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(m_bmpFields[m_currentIdx], -m_offset, m_py, null);
        canvas.drawBitmap(m_bmpFields[m_nextIdx], m_scrW - m_offset, m_py, null);
    }

    private void setBitmap(Context context, int imgId, int imgH) {
        Bitmap bmp;
        for(int i=0; i<m_bmpFields.length; i++){
            bmp = BitmapFactory.decodeResource(context.getResources(), imgId + i);
            m_bmpFields[i] = Bitmap.createScaledBitmap(bmp, m_scrW, imgH, true);
        }
    }

    /**
     * 두 개의 배경 이미지를 연결하여 좌우로 스크롤
     * 닌자가 움직이는 방향과 반대로 배경을 스크롤하여 애니메이션효과를 줌
     */
    private void scrollHorizontal(){
        m_direction = (int)GameView.getNinja().getDirectionX();
        m_offset += m_direction * m_speed * DTime.DeltaTime;

        switch(m_direction){
            case 1:
                if(m_offset > m_scrW){
                    m_offset -= m_scrW;
                    m_currentIdx = MathF.getRepeatIdx(m_currentIdx++, m_bmpCnt);
                }

                m_nextIdx = m_currentIdx + 1;
                if(m_nextIdx >= m_bmpCnt) { m_nextIdx = 0; }
                break;

            case -1:
                if(m_offset < 0) {
                    m_offset += m_scrW;
                    m_currentIdx--;
                    if(m_currentIdx < 0) m_currentIdx = m_bmpCnt - 1;
                }

                m_nextIdx = m_currentIdx - 1;
                if(m_nextIdx < 0) { m_nextIdx = m_bmpCnt - 1; }
        }
    }

}

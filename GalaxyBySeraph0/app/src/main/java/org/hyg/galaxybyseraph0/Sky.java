package org.hyg.galaxybyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by shiny on 2018-03-02.
 */

public class Sky {

    // 화면크기
    private int m_scrW, m_scrH;

    // 이미지
    private final int IMG_COUNT = 3;
    private Bitmap[] m_bmpSkys = new Bitmap[3];
    private int m_currentIdx, m_nextIdx;

    // 스크롤속도, 이미지 오프셋
    private float m_speed = 200f;
    private float m_offset = 0;

    // 현재 이미지
    public Bitmap m_bmp;

    public Sky(Context context, int width, int height){
        m_scrW = width;
        m_scrH = height;

        setBitmap(context);
    }

    /**
     * 위에서 아래로 연결되어 이동하는 두 개의 이미지
     *  : 현재 이미지와 다음 이미지를 오프셋만큼 이동한다.
     *  : 현재 이미지가 모두 아래로 이동되었으면
     *    다음 이미지를 현재 이미지로, 그 다음 이미지를 다음 이미지로 설정하여 이동을 반복함
     */
    public void moveToNext(){
        m_offset += m_speed * DTime.DeltaTime;

        if(m_offset > m_scrH){
            m_offset -= m_scrH;

            m_currentIdx = MathF.getRepeatIdx(m_currentIdx, IMG_COUNT);
        }

        m_nextIdx = m_currentIdx + 1;
        if(m_nextIdx >= IMG_COUNT) { m_nextIdx = 0; }
    }

    /**
     * 현재 이미지와 다음 이미지를 연결하여 그린다.
     * @param canvas
     */
    public void draw(Canvas canvas){
        canvas.drawBitmap(m_bmpSkys[m_currentIdx], 0, m_offset, null);
        canvas.drawBitmap(m_bmpSkys[m_nextIdx], 0, m_offset - m_scrH, null);
    }

    /**
     * 이미지 배열 초기화
     * @param context
     */
    public void setBitmap(Context context) {
        Bitmap bmp;
        for(int i=0; i<IMG_COUNT; i++){
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.sky0 + i);
            m_bmpSkys[i] = Bitmap.createScaledBitmap(bmp, m_scrW, m_scrH, true);
        }
    }
}

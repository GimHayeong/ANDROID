package org.hyg.floatingbubblebyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

/**
 * Created by shiny on 2018-02-20.
 */

public class Bubble {

    // 화면의 크기
    private int m_width, m_height;

    // 비눗방울 이미지
    private Bitmap m_bubble;

    public Bitmap getBubble() { return m_bubble; }
    // 비눗방울의 위치
    private int m_speedX, m_speedY;
    private int m_bubbleX, m_bubbleY, m_bubbleSize;

    public int getBubbleX() { return m_bubbleX; }

    public int getBubbleY() { return m_bubbleY; }

    public int getBubbleSize() { return m_bubbleSize; }

    public Bubble(Context context, int w, int h, int x, int y){
        m_width = w;
        m_height = h;
        m_bubbleX = x;
        m_bubbleY = y;

        // 비눗방울의 크기 랜덤하게 설정 : 50 ~ 150
        Random rnd = new Random();
        m_bubbleSize = rnd.nextInt(101) + 50;

        m_bubble = BitmapFactory.decodeResource(context.getResources(), R.drawable.bubble);
        m_bubble = Bitmap.createScaledBitmap(m_bubble, m_bubbleSize * 2, m_bubbleSize * 2, true);

        // 비눗방울의 속도 : 1 ~ 5
        m_speedX = rnd.nextInt(5) + 1;
        m_speedY = rnd.nextInt(5) + 1;

        if(m_bubbleX < m_bubbleSize){
            m_bubbleX += m_bubbleSize ;
        } else if(m_bubbleX > m_width - m_bubbleSize){
            m_bubbleX -= m_bubbleSize;
        }

        if(m_bubbleY < m_bubbleSize){
            m_bubbleY += m_bubbleSize;
        } else if(m_bubbleY > m_height - m_bubbleSize){
            m_bubbleY -= m_bubbleSize;
        }

        // 비눗방울의 이동방향
        m_speedX = rnd.nextInt(2) == 0 ? m_speedX : -m_speedX;
        m_speedY = rnd.nextInt(2) == 0 ? m_speedY : -m_speedY;
    }

    public void moveToNext(){
        m_bubbleX += m_speedX;
        m_bubbleY += m_speedY;

        if(m_bubbleX < m_bubbleSize || m_bubbleX > m_width - m_bubbleSize ){
            m_speedX = -m_speedX;
            m_bubbleX += m_speedX;
        }

        if(m_bubbleY < m_bubbleSize || m_bubbleY > m_height - m_bubbleSize){
            m_speedY = -m_speedY;
            m_bubbleY += m_speedY;
        }
    }
}

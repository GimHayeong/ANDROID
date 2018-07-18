package org.hyg.floatingbubblebyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import java.util.Random;

/**
 * Created by shiny on 2018-02-21.
 */

public class SmallBubble {
    // 화면 크기
    private int m_width, m_height;

    // 이동방향 벡터
    private PointF m_direction = new PointF();
    // 속도
    private int m_speed;
    // 수명
    private float m_lifeTime;

    private float m_bubbleX, m_bubbleY;
    private int m_bubbleSize;
    // 투명도
    private int m_alpha = 255;
    // 소멸여부
    private boolean m_isDead;
    // 비눗방울 이미지
    private Bitmap m_bubble;

    public float getBubbleX() { return m_bubbleX; }
    public float getBubbleY() { return m_bubbleY; }
    public int getBubbleSize() {return m_bubbleSize; }
    public int getBubbleAlpha() { return m_alpha; }
    public boolean getIsDead() { return m_isDead; }
    public Bitmap getBubble() { return m_bubble; }

    public SmallBubble(Context context, int width, int height, float x, float y){
        m_width = width;
        m_height = height;
        m_bubbleX = x;
        m_bubbleY = y;

        Random rnd = new Random();

        // 300 ~ 500 픽셀
        m_speed = rnd.nextInt(201) + 300;
        // 1 ~ 1.5 초
        m_lifeTime = (rnd.nextInt(6) + 10) / 10f;
        // 이동방향
        double rds = Math.toRadians(rnd.nextInt(360));

        m_direction.set((float)Math.cos(rds)*m_speed
                , (float)-Math.sin(rds)*m_speed);

        // 비눗방울 크기 10 ~ 20
        m_bubbleSize = rnd.nextInt(11) + 10;

        // 이미지 번호 0 ~ 5
        int idx = rnd.nextInt(6);
        m_bubble = BitmapFactory.decodeResource(context.getResources(), R.drawable.b0 + idx);
        m_bubble = Bitmap.createScaledBitmap(m_bubble, m_bubbleSize * 2, m_bubbleSize * 2, true);
    }

    public void moveToNext(){
        m_bubbleX += m_direction.x * BurstTime.deltaTime;
        m_bubbleY += m_direction.y * BurstTime.deltaTime;

        // 수명과 알파값 감소
        m_lifeTime -= BurstTime.deltaTime;
        if(m_lifeTime < 0){
            m_alpha = Math.max(0, m_alpha - 5);
        }

        // 수명을 다하거나 화면을 벗어나면 삭제표시
        if(m_alpha == 0 || m_bubbleX < -m_bubbleSize || m_bubbleX > m_width + m_bubbleSize || m_bubbleY < -m_bubbleSize || m_bubbleY > m_height + m_bubbleSize){
            m_isDead = true;
        }
    }
}

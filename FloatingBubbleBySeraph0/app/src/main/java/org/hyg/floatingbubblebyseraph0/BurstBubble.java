package org.hyg.floatingbubblebyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import org.hyg.floatingbubblebyseraph0.R;

import java.util.Random;

/**
 * Created by shiny on 2018-02-21.
 */

public class BurstBubble {

    private Context m_context;

    // 화면의 크기
    private int m_width, m_height;

    // 이동속도
    private int m_speed;

    // 이동방향 벡터
    private PointF m_direction = new PointF();

    private Random m_rnd = new Random();

    // 현재시간 notoTime
    //private long m_currentTime;

    // 비눗방울의 위치
    private float m_bubbleX, m_bubbleY;

    // 비눗방울의 크기
    private int m_bubbleSize;

    // 비눗방울 이미지
    private Bitmap m_bubble;

    // 터진 비눗방울 표시(삭제대상)
    private boolean m_isDead;

    public float getBubbleX(){ return m_bubbleX; }
    public float getBubbleY() { return m_bubbleY; }
    public int getBubbleSize() { return m_bubbleSize; }
    public Bitmap getBubble() { return m_bubble; }
    public boolean getIsDead() { return m_isDead; }


    public BurstBubble(Context context, int width, int height){
        m_width = width;
        m_height = height;
        m_context = context;

        // 50 ~ 120
        m_bubbleSize = m_rnd.nextInt(71) + 50;

        m_bubble = BitmapFactory.decodeResource(context.getResources(), R.drawable.bubble);
        m_bubble = Bitmap.createScaledBitmap(m_bubble, getBubbleSize() * 2, getBubbleSize() *2, true);

        initBubble();
    }

    private void initBubble() {
        // 150 ~ 200 픽셀
        m_speed = m_rnd.nextInt(51) + 150;

        // 0 ~ 360 도
        double rds = Math.toRadians(m_rnd.nextInt(360));

        // 수학과 단말기의 y축은 반대이므로 -y
        m_direction.set((float)Math.cos(rds)*m_speed, (float)-Math.sin(rds)*m_speed);

        // 초기위치 랜덤설정: 화면의 경계에 걸치지 않는 범위
        m_bubbleX = m_rnd.nextInt(m_width - getBubbleSize() * 4) + getBubbleSize() * 2;
        m_bubbleY = m_rnd.nextInt(m_height - getBubbleSize() * 4) + getBubbleSize() * 2;

        //m_currentTime = System.nanoTime();
    }

    public void moveToNext(){

        m_bubbleX += m_direction.x * BurstTime.deltaTime;
        m_bubbleY += m_direction.y * BurstTime.deltaTime;

        if(m_bubbleX < m_bubbleSize || m_bubbleX > m_width - m_bubbleSize){
            m_direction.x = -m_direction.x;
            m_bubbleX += m_direction.x * BurstTime.deltaTime;
        }

        if(m_bubbleY < m_bubbleSize || m_bubbleY > m_height - m_bubbleSize){
            m_direction.y = -m_direction.y;
            m_bubbleY += m_direction.y * BurstTime.deltaTime;
        }
    }

    /**
    public void moveToNext(){
        // 이전프레임으로부터의 경과시간
        float deltaTime;

        deltaTime = (System.nanoTime() - m_currentTime) / 1000000000f;
        m_currentTime = System.nanoTime();

        m_bubbleX += m_direction.x * deltaTime;
        m_bubbleY += m_direction.y * deltaTime;

        if(m_bubbleX < m_bubbleSize || m_bubbleX > m_width - m_bubbleSize){
            m_direction.x = -m_direction.x;
            m_bubbleX += m_direction.x * deltaTime;
        }

        if(m_bubbleY < m_bubbleSize || m_bubbleY > m_height - m_bubbleSize){
            m_direction.y = -m_direction.y;
            m_bubbleY += m_direction.y * deltaTime;
        }
    }
    **/

    // 원의 공식으로 비눗방울의 내부여부 확인
    public boolean hitTest(float px, float py){
        boolean hit = false;
        m_isDead = (m_bubbleX - px) * (m_bubbleX - px) + (m_bubbleY - py) * (m_bubbleY - py) < m_bubbleSize * m_bubbleSize;

        if (m_isDead) {
            // 25 ~ 30
            int cnt = m_rnd.nextInt(6) + 25;

            // 스레드 공유자원 동기화
            synchronized (GameView.SmallBubbleList) {
                for (int i = 0; i < cnt; i++) {
                    GameView.SmallBubbleList.add(new SmallBubble(m_context, m_width, m_height, m_bubbleX, m_bubbleY));
                }
            }
        }

        return m_isDead;
    }

}

package org.hyg.floatingbubblebyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    private GameThread m_thread;

    private Context m_context;
    private Random m_rnd = new Random();

    private Bitmap m_bmpBack;
    private int m_width, m_height;

    private ArrayList<Bubble> m_bubbleList = new ArrayList<Bubble>();

    // 스레드로부터 안전한 ArrayList
    private List<BurstBubble> m_burstBubbleList = Collections.synchronizedList(new ArrayList<BurstBubble>());
    static public List<SmallBubble> SmallBubbleList = Collections.synchronizedList(new ArrayList<SmallBubble>());

    private Paint m_smallBubblePaint = new Paint();

    Handler m_hdlMoveBubble = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            moveBubble();
            invalidate();
            m_hdlMoveBubble.sendEmptyMessageDelayed(0, 10);
        }
    };

    Handler m_hdlMoveBurstBubble = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            BurstTime.updateTime();

            makeBurstBubble();
            moveBurstBubble();
            removeBurstBubble();
            invalidate();
            m_hdlMoveBurstBubble.sendEmptyMessageDelayed(0, 0);
        }
    };

    public class GameThread extends Thread {
        // 스레드 계속실행 여부
        public boolean CanRun = true;

        @Override
        public void run() {
            while(this.CanRun) {
                try {
                    BurstTime.updateTime();

                    makeBurstBubble();
                    moveBurstBubble();
                    removeBurstBubble();

                    // 메인스레드의 invalidate() 요청
                    postInvalidate();

                    // 10/1000 초 지연
                    sleep(10);

                } catch (Exception ex){
                    // Do nothing ...
                }
            }
        }
    }

    // 역순으로 터진 비눗방울 제거
    private void removeBurstBubble() {
        // 스레드 공유자원 동기화
        synchronized (m_burstBubbleList) {
            for (int i = m_burstBubbleList.size() - 1; i >= 0; i--) {
                if (m_burstBubbleList.get(i).getIsDead()) {
                    m_burstBubbleList.remove(i);
                }
            }
        }

        // 스레드 공유자원 동기화
        synchronized (SmallBubbleList) {
            for (int i = SmallBubbleList.size() - 1; i >= 0; i--) {
                if (SmallBubbleList.get(i).getIsDead()) {
                    SmallBubbleList.remove(i);
                }
            }
        }
    }

    // 20개 이내의 비눗방울을 매 프레임마다 8/1000 의 확률로 생성
    private void makeBurstBubble() {
        // 스레드 공유자원 동기화
        synchronized (m_burstBubbleList) {
            if (m_burstBubbleList.size() < 20 && m_rnd.nextInt(1000) < 8) {
                m_burstBubbleList.add(new BurstBubble(m_context, m_width, m_height));
            }
        }
    }

    private void moveBurstBubble() {
        // 스레드 공유자원 동기화
        synchronized (m_burstBubbleList) {
            for (BurstBubble itm : m_burstBubbleList) {
                itm.moveToNext();
                ;
            }
        }

        // 스레드 공유자원 동기화
        synchronized (SmallBubbleList) {
            for (SmallBubble itm : SmallBubbleList) {
                itm.moveToNext();
            }
        }
    }

    private void moveBubble() {
        for(Bubble itm : m_bubbleList){
            itm.moveToNext();
        }
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(m_bmpBack, 0, 0, null);

        for(Bubble itm : m_bubbleList){
            canvas.drawBitmap(itm.getBubble()
                           , itm.getBubbleX() - itm.getBubbleSize()
                           , itm.getBubbleY() - itm.getBubbleSize()
                           , null);
        }

        // 스레드 공유자원 동기화
        synchronized (m_burstBubbleList) {
            for (BurstBubble itm : m_burstBubbleList) {
                canvas.drawBitmap(itm.getBubble()
                        , itm.getBubbleX() - itm.getBubbleSize()
                        , itm.getBubbleY() - itm.getBubbleSize()
                        , null);
            }
        }

        // 스레드 공유자원 동기화
        synchronized (SmallBubbleList) {
            for (SmallBubble itm : SmallBubbleList) {
                m_smallBubblePaint.setAlpha(itm.getBubbleAlpha());
                canvas.drawBitmap(itm.getBubble()
                        , itm.getBubbleX() - itm.getBubbleSize()
                        , itm.getBubbleY() - itm.getBubbleSize()
                        , m_smallBubblePaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //makeBubble((int)event.getX(), (int)event.getY());

            hitTest((int)event.getX(), (int)event.getY());
        }

        return true;
    }

    // 비눗방울을 클릭하면 터뜨리고 제거한다.
    private void hitTest(int x, int y) {
        for(BurstBubble itm : m_burstBubbleList){
            if(itm.hitTest(x, y)){
                break;
            }
        }
    }

    // 클릭한 위치에 비눗방울 생성
    private void makeBubble(int x, int y) {
        m_bubbleList.add(new Bubble(m_context, m_width, m_height, x, y));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.m_width = w;
        this.m_height = h;

        m_bmpBack = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
        m_bmpBack = Bitmap.createScaledBitmap(m_bmpBack, w, h, true);

        // 랜덤 생성 버블
        //m_hdlMoveBubble.sendEmptyMessageDelayed(0, 10);

        // 파편 버블
        //m_hdlMoveBurstBubble.sendEmptyMessageDelayed(0, 10);

        if(m_thread == null){
            m_thread = new GameThread();
            m_thread.start();
        }
    }

    // View가 종료될 때 스레드 종료
    @Override
    protected void onDetachedFromWindow() {

        m_thread.CanRun = false;

        super.onDetachedFromWindow();
    }
}

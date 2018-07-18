package org.hyg.spiderbyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    private Context m_context;
    private GameThread m_thread;

    private Bitmap m_bgImage;
    private int m_width, m_height;

    private Spider m_spider;
    static public List<Poison> PoisonList;

    /*================= 나비 추가관련 변수 */
    // 득점, 실점, 명중률
    private int m_score = 0;
    private int m_lose = 0;
    private float m_probability;
    // 점수 표시용 펜
    private Paint m_patScore = new Paint();
    static public List<Butterfly> ButterflyList;
    /* 나비 추가관련 변수 =================*/


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;
        CommonResources.init(context);

        m_patScore.setTextSize(60);
        m_patScore.setColor(Color.BLACK);
        m_patScore.setFakeBoldText(true);

        PoisonList = Collections.synchronizedList(new ArrayList<Poison>());
        ButterflyList = Collections.synchronizedList(new ArrayList<Butterfly>());

        m_bgImage = BitmapFactory.decodeResource(getResources(), R.drawable.back);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(m_bgImage, 0, 0, null);

        synchronized (ButterflyList){
            for(Butterfly itm : ButterflyList){
                canvas.drawBitmap(itm.getButterfly()
                                , itm.getX() - itm.getWidth()
                                , itm.getY() - itm.getHeight()
                                , null);
            }
        }

        synchronized (PoisonList){
            for(Poison itm : PoisonList){
                canvas.drawBitmap(itm.getPoison()
                               , itm.getX() - itm.getSize()
                               , itm.getY() - itm.getSize()
                               , null);
            }
        }

        canvas.drawBitmap(m_spider.getSpider()
                       , m_spider.getX() - m_spider.getWidth()
                       , m_spider.getY() - m_spider.getHeight()
                       , null);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_width = w;
        m_height = h;

        m_bgImage = Bitmap.createScaledBitmap(m_bgImage, w, h, true);

        m_spider = new Spider(w, h);

        if(m_thread == null){
            m_thread = new GameThread();
            m_thread.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        m_thread.CanRun = false;
        super.onDetachedFromWindow();
    }

    /**
     * 버튼을 누르고 있는동안 거미 이동
     * 버튼을 놓으면 거미의 이동 정지
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            m_spider.getIntoAction(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            m_spider.stopAction();
        }

        return true;
    }

    private void addButterfly(){
        synchronized (ButterflyList){
            if(ButterflyList.size() < 8){
                ButterflyList.add(new Butterfly(m_width, m_height));
            }
        }
    }


    private void moveObject(){
        synchronized (ButterflyList){
            for(Butterfly itm : ButterflyList){
                itm.moveToNext();
            }
        }

        m_spider.moveToNext();

        synchronized (PoisonList){
            for(Poison itm : PoisonList){
                itm.moveToNext();
            }
        }
    }

    private void removeObject(){
        synchronized (ButterflyList){
            for(int i=ButterflyList.size() - 1; i>=0; i--){
                if(ButterflyList.get(i).getIsDestructed()){
                    m_lose++;
                    m_score += ButterflyList.get(i).getScore();

                    ButterflyList.get(i).init();
                }
            }
        }

        synchronized (PoisonList){
            for(int i=PoisonList.size() - 1; i>=0; i--){
                if(PoisonList.get(i).getIsDestructed()){
                    PoisonList.remove(i);
                }
            }
        }
    }



    class GameThread extends Thread{
        public boolean CanRun = true;

        @Override
        public void run() {
            while(CanRun){
                try{
                    DTime.updateTime();

                    addButterfly();
                    moveObject();
                    removeObject();

                    postInvalidate();
                    sleep(10);

                } catch (Exception ex){
                    // Do something...

                    CanRun = false;
                }
            }
        }
    }
}

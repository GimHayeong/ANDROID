package org.hyg.destoryalienbyseraph0;

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

import java.util.Random;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    private Context m_context;
    private GameThread m_thread;
    private Random m_rnd = new Random();
    private CommonResources m_cmRes;

    private Bitmap m_bgImage;
    private int m_width, m_height;

    static private XWing m_xwing;
    static public XWing getXWing() { return m_xwing; }


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;

        m_cmRes.init(context);

        m_bgImage = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(m_bgImage, 0, 0, null);

        synchronized (m_cmRes.LaserList){
            for(Laser itm : m_cmRes.LaserList){
                canvas.drawBitmap(itm.getLaser()
                                , itm.getX() - itm.getWidth()
                                , itm.getY() - itm.getHeight()
                                , null);
            }
        }

        synchronized (m_cmRes.TorpedoList){
            for(Torpedo itm : m_cmRes.TorpedoList){
                canvas.rotate(itm.getAngle(), itm.getX(), itm.getY());
                canvas.drawBitmap(itm.getTorpedo()
                                , itm.getX() - itm.getWidth()
                                , itm.getY() - itm.getHeight()
                                , null);
                canvas.rotate(-itm.getAngle(), itm.getX(), itm.getY());
            }
        }

        synchronized (m_cmRes.AlienList){
            for(Alien itm : m_cmRes.AlienList){
                canvas.drawBitmap(itm.getAlien()
                                , itm.getX() - itm.getWidth()
                                , itm.getY() - itm.getHeight()
                                , null);
            }
        }

        synchronized (m_cmRes.ExplosionList){
            for(Explosion itm : m_cmRes.ExplosionList){
                canvas.drawBitmap(itm.getExplosion()
                                , itm.getX() - itm.getWidth()
                                , itm.getY() - itm.getHeight()
                                , null);
            }
        }

        canvas.drawBitmap(m_xwing.getXWing()
                       , m_xwing.getX() - m_xwing.getWidth()
                       , m_xwing.getY() - m_xwing.getHeight()
                       , null);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_width = w;
        m_height = h;

        m_bgImage = Bitmap.createScaledBitmap(m_bgImage, w, h, true);

        m_xwing = new XWing(w, h);

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            m_xwing.getIntoAction(event.getX(), event.getY());
        }

        return true;
    }


    /**
     * 에일리언 생성
     *  : 에일리언의 수는 6개 이내로 제한한다.
     *    호출하는 곳에서 동기화하므로 이 함수는 동기화하지 않아도 됨
     */
    private void addAlien(){
        if(m_cmRes.AlienList.size() < 6 && m_rnd.nextInt(1000) < 1){
            m_cmRes.addAlien(m_width, m_height);
        }
    }

    /**
     * 이동
     */
    private void moveObject() {
        m_xwing.moveToNext();

        m_cmRes.moveObject();
    }

    /**
     * 제거
     */
    private void removeObject() {
        m_cmRes.removeObject();
    }



    class GameThread extends Thread {
        public boolean CanRun = true;

        @Override
        public void run() {
            while(CanRun){
                try{

                    DTime.updateTime();

                    addAlien();
                    moveObject();
                    removeObject();

                    postInvalidate();
                    sleep(10);

                } catch(Exception ex){
                    // Do something ...
                    CanRun = false;
                }
            }
        }
    }
}

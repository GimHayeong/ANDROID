package org.hyg.xwingbyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.wifi.p2p.WifiP2pManager;
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

    private Xwing m_xwing;
    static public List<Laser> LaserList;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;
        CommonResources.init(context);
        m_bgImage = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(m_bgImage, 0, 0, null);

        synchronized (LaserList){
            for(Laser itm : LaserList){
                canvas.drawBitmap(itm.getLaser()
                               , itm.getX() - itm.getWidth()
                               , itm.getY() - itm.getHeight()
                               , null);
            }
        }

        canvas.drawBitmap(m_xwing.getXwing()
                       , m_xwing.getPos().x - m_xwing.getWidth()
                       , m_xwing.getPos().y - m_xwing.getHeight()
                       , null);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_width = w;
        m_height = h;

        m_bgImage = Bitmap.createScaledBitmap(m_bgImage, m_width, m_height, true);

        m_xwing = new Xwing(w, h);
        LaserList = Collections.synchronizedList(new ArrayList<Laser>());

        if(m_thread == null) {
            m_thread = new GameThread();
            m_thread.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            m_xwing.getIntoAction(event.getX(), event.getY());
        }

        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        m_thread.CanRun = false;
        super.onDetachedFromWindow();
    }


    private void moveObject() {
        m_xwing.moveToNext();

        synchronized (LaserList){
            for(Laser itm : LaserList){
                itm.moveToNext();
            }
        }
    }

    private void removeObject() {
        synchronized (LaserList){
            for(int i=LaserList.size() - 1; i>=0; i--){
                if(LaserList.get(i).getIsDestruction()){
                    LaserList.remove(i);
                }
            }
        }
    }




    class GameThread extends Thread {
        public boolean CanRun = true;

        @Override
        public void run() {
            while(CanRun){
                try{

                    DTime.updateTime();

                    moveObject();
                    removeObject();

                    postInvalidate();
                    sleep(10);

                } catch (Exception ex){
                    // Do something ...
                    CanRun = false;
                }
            }
        }
    }
}

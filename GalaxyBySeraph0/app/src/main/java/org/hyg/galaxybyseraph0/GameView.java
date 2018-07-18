package org.hyg.galaxybyseraph0;

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

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    private Context m_context;
    private GameThread m_thread;

    private int m_width, m_height;

    // 배경
    private Sky m_sky;

    // 전투기
    private XWing m_xwing;


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        m_sky.draw(canvas);

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

        m_sky = new Sky(m_context, w, h);
        m_xwing = new XWing(m_context, w, h);

        if(m_thread == null) {
            m_thread = new GameThread();
            m_thread.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            m_xwing.getIntoAction(event.getX());
        }

        return true;
    }



    private void moveObject(){
        m_sky.moveToNext();
        m_xwing.moveToNext();
    }



    class GameThread extends Thread {
        public boolean CanRun = true;


        @Override
        public void run() {
            while(CanRun){
                try{

                    DTime.updateTime();

                    moveObject();

                    postInvalidate();
                    sleep(10);

                } catch (Exception ex) {
                    // Do something ...

                    CanRun = false;
                }
            }
        }
    }
}

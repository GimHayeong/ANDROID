package org.hyg.butterflybyseraph0;

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
import java.util.Random;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    private Context m_context;
    private GameThread m_thread;

    private Bitmap m_bgImage;
    private int m_width, m_height;

    private Flower m_flower;
    private ArrayList<Butterfly> m_butterflyList = new ArrayList<Butterfly>();

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;

        m_bgImage = BitmapFactory.decodeResource(getResources(), R.drawable.field);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(m_bgImage, 0, 0, null);

        canvas.drawBitmap(m_flower.getFlower()
                       , m_flower.getX() - m_flower.getWidth()
                       , m_flower.getY() - m_flower.getHeight()
                       , null);

        for(Butterfly itm : m_butterflyList){
            canvas.rotate(itm.getAngle(), itm.getX(), itm.getY());
            canvas.drawBitmap(itm.getButterfly(), itm.getX() - itm.getWidth(), itm.getY() - itm.getHeight(), null);
            canvas.rotate(-itm.getAngle(), itm.getX(), itm.getY());
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_width = w;
        m_height = h;

        m_bgImage = Bitmap.createScaledBitmap(m_bgImage, w, h, true);

        m_flower = new Flower(m_context, w, h);

        makeButterfly(w, h);

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
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            //m_flower.moveToNext(event.getX(), event.getY());
            setTargetForButterfly(event.getX(), event.getY());
        } else if(event.getAction() == MotionEvent.ACTION_UP){
            //setTargetForButterfly(event.getX(), event.getY());
        }

        return true;
    }

    private void makeButterfly(int w, int h){
        m_butterflyList.clear();
        int cnt = new Random().nextInt(6) + 15;

        for(int i=0; i<cnt; i++){
            m_butterflyList.add(new Butterfly(m_context, w, h));
        }
    }


    private void moveButterfly(){
        for(Butterfly itm : m_butterflyList){
            itm.moveToNext();
        }
    }

    // 꽃다발이 이동되면 나비의 목적지 변경
    private void setTargetForButterfly(float tx, float ty){
        if(m_flower.moveToNext(tx, ty)){
            for(Butterfly itm : m_butterflyList){
                itm.setTarget(tx, ty);
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

                    moveButterfly();

                    postInvalidate();
                    sleep(10);

                } catch(Exception ex) {
                    // Do something ...
                    CanRun = false;
                }
            }
        }
    }

}

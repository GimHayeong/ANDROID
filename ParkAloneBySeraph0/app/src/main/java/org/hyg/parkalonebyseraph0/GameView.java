package org.hyg.parkalonebyseraph0;

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

    static public Boy sm_boy;

    private Context m_context;
    private GameThread m_thread;

    private Bitmap m_bgImage;
    private int m_width, m_height;

    private Random m_rnd = new Random();

    private Ball m_ball;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;

        m_bgImage = BitmapFactory.decodeResource(getResources(), R.drawable.back);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(m_bgImage, 0, 0, null);

        canvas.save();
        canvas.scale(sm_boy.getShadowScale(), sm_boy.getShadowScale(), sm_boy.getX(), sm_boy.getGroundHeight());
        canvas.drawBitmap(sm_boy.getShadow()
                       , sm_boy.getX() - sm_boy.getShadowWidth()
                       , sm_boy.getGroundHeight() - sm_boy.getShadowHeight()
                       , null);
        canvas.restore();

        canvas.save();
        canvas.scale(sm_boy.getDirection().x, 1, sm_boy.getX(), sm_boy.getY());
        canvas.drawBitmap(sm_boy.getBoy()
                , sm_boy.getX() - sm_boy.getWidth()
                , sm_boy.getY() - sm_boy.getHeight()
                , null);
        canvas.restore();

        canvas.save();
        canvas.scale(m_ball.getShadowScale(), m_ball.getShadowScale(), m_ball.getX(), m_ball.getGroundHeight());
        canvas.drawBitmap(m_ball.getShadow()
                       , m_ball.getX() - m_ball.getShadowWidth()
                       , m_ball.getGroundHeight() - m_ball.getShadowHeight()
                       , null);
        canvas.restore();

        canvas.rotate(m_ball.getAngle(), m_ball.getX(), m_ball.getY());
        canvas.drawBitmap(m_ball.getBall()
                       , m_ball.getX() - m_ball.getRadius()
                       , m_ball.getY() - m_ball.getRadius()
                       , null);
        canvas.rotate(-m_ball.getAngle(), m_ball.getX(), m_ball.getY());
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_width = w;
        m_height = h;

        m_bgImage = Bitmap.createScaledBitmap(m_bgImage, w, h, true);

        m_ball = new Ball(m_context, w, h);
        sm_boy = new Boy(m_context, w, h);

        if(m_thread == null){
            m_thread = new GameThread();
            m_thread.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            sm_boy.getIntoAction(event.getX(), event.getY());
        }

        return true;
    }


    @Override
    protected void onDetachedFromWindow() {
        m_thread.CanRun = false;
        super.onDetachedFromWindow();
    }


    private void moveObject(){
        sm_boy.moveToNext();
        m_ball.moveToNext();
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

                } catch(Exception ex) {
                    // Do something ...

                    CanRun = false;
                }
            }
        }
    }
}

package org.hyg.bounceballbyseraph0;

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

    // 동기화 대상 공저장소
    private List<Ball> BallList;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;
        CommonBall.set(context);
        BallList = Collections.synchronizedList(new ArrayList<Ball>());
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(m_bgImage, 0, 0, null);

        synchronized (BallList){
            for(Ball itm : BallList){
                canvas.rotate(itm.getBallAngle(), itm.getBallX(), itm.getBallY());
                canvas.drawBitmap(itm.getBall(), itm.getBallX() - itm.getBallRadius(), itm.getBallY() - itm.getBallRadius(), null);
                canvas.rotate(-itm.getBallAngle(), itm.getBallX(), itm.getBallY());
            }
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_width = w;
        m_height = h;

        m_bgImage = BitmapFactory.decodeResource(m_context.getResources(), R.drawable.field);
        m_bgImage = Bitmap.createScaledBitmap(m_bgImage, w, h, true);

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
            makeBall(event.getX(), event.getY());
        }
        return true;
    }



    private synchronized void makeBall(float x, float y) {
        BallList.add(new Ball(m_width, m_height, x, y));
    }

    class GameThread extends Thread {
        public boolean CanRun = true;

        @Override
        public void run() {
            while(CanRun){
                try{

                    BallTime.updateTime();

                    moveBall();
                    removeBall();

                    postInvalidate();

                    sleep(10);

                } catch (Exception ex){
                    // Do something ...
                }
            }
        }
    }

    private synchronized void moveBall() {
        for(Ball itm : BallList){
            itm.moveToNext();
        }
    }

    private synchronized void removeBall(){
        for(int i = BallList.size() - 1; i>=0; i--){
            if(BallList.get(i).getIsDead()){
                BallList.remove(i);
            }
        }
    }

    public void initGame(){
        synchronized (BallList){
            BallList.clear();
        }

        invalidate();
    }

}

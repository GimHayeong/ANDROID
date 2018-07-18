package org.hyg.rocketbyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: 해당도에 따라 이미지확대금지하니 화면중간까지는 포물선이 예상대로 그려지며 로켓이 이동하지만 이후에는 정확하게 인식이 안됨
 */
public class GameView extends View {

    private GameThread m_thread;
    private Context m_context;
    private Bitmap m_bgImage;
    private int m_width, m_height;

    private Rocket m_rocket;
    // 로켓 발사여부
    private boolean m_isLaunch = false;


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;
        m_bgImage = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
    }

    public void initGame(){

    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(m_bgImage, 0, 0, null);

        canvas.rotate(m_rocket.getAngle(), m_rocket.getX(), m_rocket.getY());
        canvas.drawBitmap(m_rocket.getRocket()
                        , m_rocket.getX() - m_rocket.getWidth()
                        , m_rocket.getY() - m_rocket.getHeight()
                        , null);
        canvas.rotate(-m_rocket.getAngle(), m_rocket.getX(), m_rocket.getY());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_width = w;
        m_height = h;

        m_bgImage = Bitmap.createScaledBitmap(m_bgImage, w, h, true);

        m_rocket = new Rocket(m_context, w, h);

        if (m_thread == null){
            m_thread = new GameThread();
            m_thread.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!m_isLaunch
           && event.getAction() == MotionEvent.ACTION_DOWN){

            //showSnackbar(event.getX(), event.getY());
            m_rocket.launch(event.getX(), event.getY());
            m_isLaunch = true;
        }

        return true;
    }

    private void showSnackbar(float x, float y){
        Snackbar.make(super.getRootView(), "x: " + (int)x + ", y: " + (int)y, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }


    @Override
    protected void onDetachedFromWindow() {

        m_thread.CanRun = false;

        super.onDetachedFromWindow();
    }





    private void moveRocket(){
        if(m_isLaunch){
            m_isLaunch = m_rocket.moveToNext();
        }
    }



    class GameThread extends Thread {
        public boolean CanRun = true;

        @Override
        public void run() {
            while(CanRun){
                try{

                    DTime.updateTime();

                    moveRocket();

                    postInvalidate();
                    sleep(10);

                } catch (Exception ex){
                    // Do something ...
                }
            }
        }
    }
}

package org.hyg.redrabbitbyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
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

    // 배경이미지, 크기
    private Bitmap m_bgImage;
    private int m_width, m_height;

    // 배경하늘
    private RectF m_bgSky = new RectF();
    private Paint m_patSky = new Paint();

    // 구름
    private Cloud[] m_clouds = new Cloud[2];

    // 토끼
    private Rabbit m_rabbit;



    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;

        m_bgImage = BitmapFactory.decodeResource(getResources(), R.drawable.field);


        init();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_width = w;
        m_height = h;

        m_bgImage = Bitmap.createScaledBitmap(m_bgImage, w, (int)(h * 0.7f), true);

        setSkyGradient();

        setCloud();

        m_rabbit = new Rabbit(m_context, w, h);

        if(m_thread == null) {
            m_thread = new GameThread();
            m_thread.start();
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawRect(m_bgSky, m_patSky);

        canvas.drawBitmap(m_bgImage, 0, m_height * 0.3f, null);

        for(int i=0; i<m_clouds.length; i++){
            canvas.drawBitmap(m_clouds[i].getCloud()
                            , m_clouds[i].getX() - m_clouds[i].getWidth()
                            , m_clouds[i].getY() - m_clouds[i].getHeight()
                            , null);
        }

        canvas.drawBitmap(m_rabbit.getShadow()
                        , m_rabbit.getX() - m_rabbit.getShadowWidth()
                        , m_rabbit.getGroundHeight() - m_rabbit.getShadowHeight()
                        , null);

        canvas.scale(m_rabbit.getDirection(), 1, m_rabbit.getX(), m_rabbit.getY());
        canvas.drawBitmap(m_rabbit.getRabbit()
                        , m_rabbit.getX() - m_rabbit.getWidth()
                        , m_rabbit.getY() - m_rabbit.getHeight()
                        , null);
        canvas.scale(-m_rabbit.getDirection(), 1, m_rabbit.getX(), m_rabbit.getY());

    }

    /**
     * 터치하면 터치한 지점으로 토끼 이동
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            m_rabbit.getIntoAction(event.getX());
        }

        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        m_thread.CanRun = false;
        super.onDetachedFromWindow();
    }

    /**
     * 배경하늘 그라디언트를 만든다.
     */
    private void setSkyGradient() {
        LinearGradient lgSky = new LinearGradient(0, 0
                                                , 0, m_width * 0.7f
                                                , 0xffa8ddff
                                                , 0x40a8ddff
                                                , Shader.TileMode.CLAMP);
        m_patSky.setStyle(Paint.Style.FILL);
        m_patSky.setShader(lgSky);
        m_bgSky.set(0, 0, m_width, m_height * 0.6f);
    }

    /**
     * 두 개의 구름 초기화
     */
    private void setCloud(){
        m_clouds[0] = new Cloud(m_context, m_width, m_height);
        m_clouds[1] = new Cloud(m_context, m_width, m_height, R.drawable.clould2, 75, m_width * 0.8f, m_height * 0.3f);
    }

    /**
     * 초기화
     */
    private void init() {
    }


    /**
     * 구름과 토끼 애니메이션
     */
    private void moveObject() {

        for(int i=0; i<m_clouds.length; i++){
            m_clouds[i].moveToNext();
        }

        m_rabbit.moveToNext();
    }





    class GameThread extends Thread{
        public boolean CanRun = true;

        @Override
        public void run() {
            while(CanRun){
                try{
                    DTime.updateTime();

                    moveObject();

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

package org.hyg.jumpingboybyseraph0;

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

import org.xml.sax.DTDHandler;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    private Context m_context;
    private GameThread m_thread;
    private Bitmap m_bgImage;
    private int m_scrnWt, m_scrnHt;
    private Boy m_boy;


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;
        m_bgImage = BitmapFactory.decodeResource(getResources(), R.drawable.field);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_scrnWt = w;
        m_scrnHt = h;

        m_bgImage = Bitmap.createScaledBitmap(m_bgImage, w, h, true);

        m_boy = new Boy(m_context, w, h);

        if (m_thread == null){
            m_thread = new GameThread();
            m_thread.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(m_bgImage, 0, 0, null);

        canvas.save();

        canvas.scale(m_boy.getSadowScale(),  m_boy.getSadowScale(), m_boy.getX(), m_boy.getGroundHeight());
        canvas.drawBitmap(m_boy.getShadow()
                       , m_boy.getX() - m_boy.getShadowWidth()
                       , m_boy.getGroundHeight() - m_boy.getShadowHeight()
                       , null);

        canvas.restore();

        canvas.drawBitmap(m_boy.getBoy()
                       , m_boy.getX() - m_boy.getWidth()
                       , m_boy.getY() - m_boy.getHeight()
                       , null);

    }


    @Override
    protected void onDetachedFromWindow() {
        m_thread.CanRun = false;
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            m_boy.startJump();
        }

        return true;
    }



    private void moveBoy(){
        m_boy.moveToNext();
    }



    class GameThread extends Thread{
        public boolean CanRun = true;

        @Override
        public void run() {
            while(CanRun){
                try{

                    DTime.updateTime();

                    moveBoy();

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

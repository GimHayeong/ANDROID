package org.hyg.ancientboybyseraph0;

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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    private Context m_context;
    private GameThread m_thread;

    private Bitmap m_bgImage;
    private int m_height;
    private int m_width;

    private Boy m_boy;

    private MyGesture m_gesture = new MyGesture();

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;
        CommonResources.init(context);

        m_bgImage = BitmapFactory.decodeResource(getResources(), R.drawable.back);

        // Gesture 설정
        final GestureDetector gestureDetector = new GestureDetector(context, m_gesture);
        setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(m_bgImage, 0, 0, null);
        canvas.save();

        canvas.scale(m_boy.getShadowScale()
                   , m_boy.getShadowScale()
                   , m_boy.getX()
                   , m_boy.getY());
        canvas.drawBitmap(m_boy.getShadow()
                       , m_boy.getX() - m_boy.getShadowWidth()
                       , m_boy.getGroundHeight() - m_boy.getHeight()
                       , null);
        canvas.restore();

        canvas.scale(m_boy.getIsRight() ? 1 : -1, 1, m_boy.getX(), m_boy.getGroundHeight());
        canvas.drawBitmap(m_boy.getBoy()
                       , m_boy.getX() - m_boy.getWidth()
                       , m_boy.getY() - m_boy.getHeight()
                       , null);
        canvas.scale(m_boy.getIsRight() ? -1 : 1, 1, m_boy.getX(), m_boy.getGroundHeight());
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_width = w;
        m_height = h;

        m_bgImage = Bitmap.createScaledBitmap(m_bgImage, w, h, true);

        m_boy = new Boy(w, h);

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

    public class MyGesture extends GestureDetector.SimpleOnGestureListener{

        /**
         * SCROLL (DRAG)
         * @param e1
         * @param e2
         * @param distanceX : 스크롤 방향 : distanceX <= 0 이면 Right, distanceX > 0 이면 Left
         * @param distanceY
         * @return
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            m_boy.run(distanceX);
            return true;
        }

        /**
         * DOUBLE TAB
         * 더블탭하면 점프
         * @param e
         * @return
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {

            m_boy.jump();
            return true;
        }

        /**
         * ACTION_DOWN
         * @param e
         * @return
         */
        @Override
        public boolean onDown(MotionEvent e) {

            m_boy.stop(e.getX(), e.getY());
            return true;
        }
    }




    private void moveObject(){
        m_boy.moveToNext();
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

                } catch (Exception ex){
                    // Do something ...

                    CanRun = false;
                }

            }
        }
    }





}

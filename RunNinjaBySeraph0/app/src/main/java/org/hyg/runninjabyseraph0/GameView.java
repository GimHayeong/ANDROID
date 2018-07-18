package org.hyg.runninjabyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.MotionEventCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    private Context m_context;
    private GameThread m_thread;

    // 화면크기
    private int m_width, m_height;


    //static private Ninja m_ninja;
    //static public Ninja getNinja() { return m_ninja; }
    static private NinjaT m_ninja;
    static public NinjaT getNinja() { return m_ninja; }
    // 좌우이동 버튼, 점프 버튼
    private PressButton[] m_pbButtons = new PressButton[3];
    private Bitmap[] m_bmpButtons = new Bitmap[3];


    private Field[] m_fields = new Field[3];

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        for(int i=0; i<m_fields.length; i++){
            m_fields[i].draw(canvas);
        }

        //drawNinja(canvas);
        drawNinjaT(canvas);
    }

    /**
     * 닌자 그리기1 : 화면 터치로 동작을 제어하는 닌자
     * @param canvas
     */
    private void drawNinja(Canvas canvas){
        canvas.scale(m_ninja.getDirectionX(), 1, m_ninja.getX(), m_ninja.getY());
        canvas.drawBitmap(m_ninja.getNinja(), m_ninja.getX() - m_ninja.getWidth(), m_ninja.getY() - m_ninja.getHeight(), null);
        canvas.scale(-m_ninja.getDirectionX(), 1, m_ninja.getX(), m_ninja.getY());
    }

    /**
     * 닌자 그리기2 : 버튼 터치로 동작을 제어하는 닌자와 동작 제어 버튼들
     * @param canvas
     */
    private void drawNinjaT(Canvas canvas){
        canvas.save();
        if(m_ninja.getIsLeft()){
            canvas.scale(-1, 1, m_ninja.getX(), m_ninja.getY());
        }
        canvas.drawBitmap(m_ninja.getNinja(), m_ninja.getX() - m_ninja.getWidth(), m_ninja.getY() - m_ninja.getHeight(), null);
        canvas.restore();

        for(int i=0; i<m_pbButtons.length; i++){
            canvas.drawBitmap(m_pbButtons[i].getButton(), m_pbButtons[i].getX(), m_pbButtons[i].getY(), null);
        }
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_width = w;
        m_height = h;


        //m_ninja = new Ninja(m_context, w, h);
        m_ninja = getNinjaT(m_context, w, h);

        //m_fields[0] = new Field(m_context, w, h);
        //m_fields[0] = new Field(m_context, w, h, 50, R.drawable.far0, (int)(h * 0.6f), 0);
        //m_fields[1] = new Field(m_context, w, h, 220, R.drawable.near0, h / 2, h / 2);
        m_fields[0] = new Sky(m_context, w, h);
        m_fields[1] = new Far(m_context, w, h);
        m_fields[2] = new Near(m_context, w, h);

        if(m_thread == null){
            m_thread = new GameThread();
            m_thread.start();
        }
    }

    /**
     * 버튼 터치로 동작을 제어하는 닌자의 버튼들
     */
    private NinjaT getNinjaT(Context context, int w, int h) {
        m_bmpButtons[0] = BitmapFactory.decodeResource(getResources(), R.drawable.button_left);
        m_bmpButtons[1] = BitmapFactory.decodeResource(getResources(), R.drawable.button_right);
        m_bmpButtons[2] = BitmapFactory.decodeResource(getResources(), R.drawable.button_jump);

        int bw = m_bmpButtons[0].getWidth();
        int bh = m_bmpButtons[0].getHeight();
        int by = m_height - bh - 10;

        Point posLeft = new Point(10, by);
        Point posRight = new Point(bw + 20, by);
        Point posJump = new Point(m_width - bw - 10, by);

        m_pbButtons[0] = new PressButton(m_bmpButtons[0], posLeft);
        m_pbButtons[1] = new PressButton(m_bmpButtons[1], posRight);
        m_pbButtons[2] = new PressButton(m_bmpButtons[2], posJump);

        return new NinjaT(m_context, w, h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return setTouchEvent(event);

        return setMultiTouchEvent(event);
    }

    /**
     * Ninja 를 터치하거나 주변을 터치해서 점프나 이동방향을 바꿔 이동시킴
     * @param event
     */
    private boolean setTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            m_ninja.getIntocAction(event.getX(), event.getY());
        }

        return true;
    }


    /**
     * NinjaT 를 좌우,점프버튼을 터치해서 이동시킴
     * @param event
     */
    private boolean setMultiTouchEvent(MotionEvent event){
        boolean isTouch = false;
        // 호환성을 위한 라이브러리 사용
        int action = MotionEventCompat.getActionMasked(event);

        switch(action){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                isTouch = true;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                isTouch = false;
                break;

            // 다른 이벤트 무시
            default:
                return true;
        }

        // 호환성을 위한 라이브러리 사용
        // : 터치 인덱스, 터치 아이디, 터치좌표
        int ptrIdx = MotionEventCompat.getActionIndex(event);
        int tid = MotionEventCompat.getPointerId(event, ptrIdx);
        float tx = MotionEventCompat.getX(event, ptrIdx);
        float ty = MotionEventCompat.getY(event, ptrIdx);

        // 각각의 버튼에 각 버튼이 터치대상인지 통지
        m_pbButtons[0].getIntoAction(tid, isTouch, tx, ty);
        m_pbButtons[1].getIntoAction(tid, isTouch, tx, ty);
        // 버튼을 놓으면 터치로 처리
        m_pbButtons[2].getIntoAction(tid, !isTouch, tx, ty);
        m_pbButtons[0].getIntoAction(tid, !isTouch, tx, ty);
        m_pbButtons[1].getIntoAction(tid, !isTouch, tx, ty);
        m_pbButtons[2].getIntoAction(tid, isTouch, tx, ty);

        m_ninja.getIntoAction(m_pbButtons[0].getIsTouch(), m_pbButtons[1].getIsTouch(), m_pbButtons[2].getIsTouch());

        return true;
    }



    @Override
    protected void onDetachedFromWindow() {
        m_thread.CanRun = false;
        super.onDetachedFromWindow();
    }




    private void moveObject(){
        m_ninja.moveToNext();
        for(int i=0; i<m_fields.length; i++){
            m_fields[i].moveToNext();
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

                    postInvalidate();
                    sleep(10);

                } catch (Exception ex){

                    CanRun = false;
                }
            }
        }
    }
}

package org.hyg.slidingpuzzlebyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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

    // 스테이지 클리어 여부
    private boolean m_isClear = false;

    // 게임시작후 경과시간, 시간표시문자열, 미디어플레이어
    private float m_timeSpan = 0;
    private String m_strTime;
    private MediaPlayer m_player;

    // 화면 표시용 페인터
    private Paint m_patString = new Paint();
    private Paint m_patBlack = new Paint();
    private Paint m_patClear = new Paint();

    private Board m_board;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;

        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, m_width, m_height, m_patBlack);

        m_board.drawTile(canvas);

        canvas.drawText("Move Tile", m_width - 50, m_height * 0.2f, m_patString);
        canvas.drawText(m_board.getMovedTileCount() + "", m_width - 50, m_height * 0.3f, m_patString);

        canvas.drawText("Time Span", m_width - 50, m_height * 0.4f, m_patString);
        canvas.drawText(m_strTime, m_width - 50, m_height * 0.5f, m_patString);

        if(m_isClear){
            canvas.drawText("Stage Clear!", m_width / 2, m_height / 2, m_patClear);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_width = w;
        m_height = h;

        CommonResources.init(m_context, w, h);
        m_board = new Board();

        initGame();

        if(m_thread == null){
            m_thread = new GameThread();
            m_thread.start();
        }
    }

    /**
     * 스테이지 클리어가 아닐때 화면을 터치할 때, 타일을 터치했는지 확인
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!m_isClear
            && event.getAction() == MotionEvent.ACTION_DOWN){

            m_board.hitTest(event.getX(), event.getY());

            CommonResources.playSound();
        }

        return true;
    }


    @Override
    protected void onDetachedFromWindow() {
        m_thread.CanRun = false;
        super.onDetachedFromWindow();
    }

    /**
     * 출력용 페인터 초기화
     */
    private void init(){
        m_patString.setTextSize(60);
        m_patString.setColor(0xff000080);
        m_patString.setTypeface(Typeface.DEFAULT_BOLD);
        m_patString.setTextAlign(Paint.Align.RIGHT);

        m_patBlack.setStyle(Paint.Style.FILL);

        m_patClear.setTextSize(120);
        m_patClear.setColor(0xff000080);
        m_patClear.setTypeface(Typeface.DEFAULT_BOLD);
        m_patClear.setTextAlign(Paint.Align.CENTER);

        m_player = MediaPlayer.create(m_context, R.raw.rondo);
        m_player.setLooping(true);

        setTimeSpan();
    }

    /**
     * 게임 초기화
     */
    private void initGame(){
        LinearGradient shader = new LinearGradient(0, 0, 0, m_height
                                                , 0xFFa8dda0
                                                , 0x40a8dda0
                                                , Shader.TileMode.CLAMP);

        m_patBlack.setShader(shader);

        if(Settings.IsMusic) { m_player.start(); }

        m_board.initBoard();
    }



    private void moveObject(){
        m_board.moveToNext();
    }

    /**
     * 스테이지 클리어여부
     */
    private void isClearAll() {
        m_isClear = m_board.getIsClearAll();
    }

    /**
     * 지연시간동안 TouchEvent 무시
     */
    private void setIgnoreTouchEvent(){

    }

    /**
     * 경과시간 누적
     */
    private void setTimeSpan(){
        if(!m_isClear){
            m_timeSpan += DTime.DeltaTime;
        }

        int hour = (int)m_timeSpan / 3600;
        int min = (int)m_timeSpan % 3600 / 60;
        float sec = (float)Math.round(m_timeSpan % 60 * 10) / 10;

        m_strTime = String.format("%02d:%02d:%4.1f", hour, min, sec);
    }

    private void removeObject() {
        // TODO:
    }


    class GameThread extends Thread {
        public boolean CanRun = true;

        @Override
        public void run() {
            while(CanRun){
                try{

                    DTime.updateTime();

                    moveObject();
                    isClearAll();
                    setIgnoreTouchEvent();
                    removeObject();
                    setTimeSpan();

                    postInvalidate();
                    sleep(10);

                } catch (Exception ex) {
                    CanRun = false;
                }
            }
        }
    }
}

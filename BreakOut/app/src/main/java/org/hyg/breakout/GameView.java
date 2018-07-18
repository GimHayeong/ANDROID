package org.hyg.breakout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    private Context m_context;
    private GameThread m_thread;

    private int m_width, m_height;
    static private Bitmap m_bgImage;

    // 남은 공 갯수, 스테이지 인덱스
    static private int m_restBallCnt;
    static private int m_stageIdx;
    static public int getStage() { return m_stageIdx; }
    static public void setStage() { m_stageIdx++; }

    // 데모모드 여부, 이전프레임으로부터의 경과시간
    static private boolean m_isDemo;
    static public boolean getIsDemo() { return m_isDemo; }
    static private float m_aniSpan;

    // 점수
    static private int m_score;
    static public int getScore() { return m_score; }
    static public void setScore(int val) { m_score += val; }
    static private int m_hitCnt;
    static public int getHitCount() { return m_hitCnt; }
    static public void setHitCount() { m_hitCnt++; }
    static private String m_strStage, m_strScore, m_strHit, m_strMsg;
    static private DecimalFormat m_dFormat = new DecimalFormat("#,##0");

    // 문자열 출력 페인터, 외곽선 문자열 출력 페인터
    private Paint m_patScore = new Paint();
    private Paint m_patStroke = new Paint();

    // 미디어 플레이어
    private MediaPlayer m_player;

    static private Paddle m_paddle;
    static public Paddle getPaddle() { return m_paddle; }
    static private Ball m_ball;
    static public Ball getBall() { return m_ball; }
    static public List<Block> BlockList = Collections.synchronizedList(new ArrayList<Block>());




    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;

        init();
    }

    static public void setScoreFormat(){
        m_strScore = String.format("Score : %s", m_dFormat.format(m_score));
        m_strHit = String.format("Hit : %d", m_hitCnt);
        m_strStage = String.format("Stage : %d", m_stageIdx + 1);
    }

    /**
     * 스테이지 초기화
     */
    static public void initStage(){
        m_bgImage = CommonResources.getBack()[m_stageIdx % 4];
        m_paddle.init(m_stageIdx);

        BlockList.clear();
        Stage.initStage(m_stageIdx);

        m_ball.reset();
        setIgnoreTouchEvent(0.5f);
    }

    /**
     * 게임오버인지 체크하여 게임오버이면 공을 패들위로 이동하여 초기화하고 공을 발사한다.
     */
    static public void setGameOver() {
        if(--m_restBallCnt >= 0) { return; }

        m_isDemo = true;
        m_ball.reset();
        m_ball.getIntoAction(m_ball.getX(), m_ball.getY());

        setIgnoreTouchEvent(0.5f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(m_bgImage, 0, 0, null);

        drawOutline(canvas, Paint.Align.LEFT, m_strScore, 20, 80);
        drawOutline(canvas, Paint.Align.CENTER, m_strHit, m_width / 2, 80);
        drawOutline(canvas, Paint.Align.RIGHT, m_strStage, m_width - 20, 80);

        int bw = CommonResources.getBallSize() + 10;
        for(int i=1; i<m_restBallCnt; i++){
            canvas.drawBitmap(CommonResources.getBallIcon()
                           , i * bw
                           , m_height - bw * 1.5f
                           , null);
        }

        canvas.drawBitmap(m_paddle.getPaddle()
                       , m_paddle.getX() - m_paddle.getWidth()
                       , m_paddle.getY() - m_paddle.getHeight()
                       , null);

        canvas.drawBitmap(m_ball.getBall()
                        , m_ball.getX() - m_ball.getRadius()
                        , m_ball.getY() - m_ball.getRadius()
                        , null);

        synchronized (BlockList){
            for(Block itm : BlockList){
                canvas.drawBitmap(itm.getBlock()
                                , itm.getX() - itm.getWidth()
                                , itm.getY() - itm.getHeight()
                                , null);
            }
        }

        if(m_isDemo){
            m_patScore.setTextSize(90);
            m_patStroke.setTextSize(90);
            float y = m_height * 0.4f;
            for(String str : m_strMsg.split("\n")){
                drawOutline(canvas, Paint.Align.CENTER, str, m_width / 2, y);
            }
            m_patScore.setTextSize(60);
            m_patStroke.setTextSize(60);
        }
    }

    private void drawOutline(Canvas canvas, Paint.Align align, String str, float x, float y) {
        m_patScore.setTextAlign(align);
        m_patStroke.setTextAlign(align);
        canvas.drawText(str, x, y, m_patScore);
        canvas.drawText(str, x, y, m_patStroke);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_width = w;
        m_height = h;

        initGame();

        initStage();


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

    /**
     * Touch 이벤트를 사용하지 않고
     * 스레드에서 경과시간을 두고 반복하도록 구현함
     *  : 지연시간동안 TouchEvent 무시
     *  : Demo모드일때 화면을 터치하면 게임 다시 시작
     *  : 공 주변을 터치하면 공의 이동방향 설정
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(m_aniSpan > 0) { return true; }

        if(m_isDemo){
            m_isDemo = false;
            init();
            initGame();
            initStage();

            return true;
        }

        float x = event.getX();
        float y = event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(m_ball.getIntoAction(x, y)) { return true; }
        }

        boolean isPress = (event.getAction() != MotionEvent.ACTION_UP);
        m_paddle.getIntoAction(isPress, x);

        return true;
    }



    /**
     * 초기화
     */
    private void init() {
        m_patScore.setTextSize(60);
        m_patScore.setColor(0xff000080);
        m_patScore.setTypeface(Typeface.DEFAULT_BOLD);

        m_patStroke.setTextSize(60);
        m_patStroke.setColor(Color.WHITE);
        m_patStroke.setTypeface(Typeface.DEFAULT_BOLD);
        m_patStroke.setTextAlign(Paint.Align.CENTER);
        m_patStroke.setStyle(Paint.Style.STROKE);
        m_patStroke.setStrokeWidth(10);

        m_player = MediaPlayer.create(m_context, R.raw.rondo);
        m_player.setLooping(true);
    }

    /**
     * 게임 초기화
     */
    private void initGame(){
        m_stageIdx = 0;
        m_restBallCnt = 2;
        m_isDemo = false;
        m_score = 0;
        m_hitCnt = 0;
        m_strMsg = "계속하시겠습니까?\n[Touch] 다시시작\n[Back Key] 종료";

        if(Settings.getIsMusic()) { m_player.start(); }

        setScoreFormat();

        CommonResources.set(m_context, m_width, m_height);

        m_paddle = new Paddle(m_width, m_height);
        m_ball = new Ball(m_width, m_height);
    }

    /**
     * 지연시간동안 TouchEvent 무시
     * @param delayTime : 지연시간
     */
    private static void setIgnoreTouchEvent(float delayTime) {
        m_aniSpan = delayTime;
    }






    private void moveObject() {
        m_paddle.moveToNext();
        m_ball.moveToNext();
    }

    private void removeObject() {
        synchronized (BlockList){
            for(int i=BlockList.size() - 1; i>=0; i--){
                if(BlockList.get(i).getIsDestructed()){
                    BlockList.remove(i);
                }
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
                    m_aniSpan -= DTime.DeltaTime;

                    moveObject();
                    removeObject();


                    postInvalidate();
                    sleep(10);

                } catch (Exception ex) {
                    CanRun = false;
                }
            }
        }
    }
}

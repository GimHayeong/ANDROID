package org.hyg.huntingbirdbyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
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

    // 배경화면
    private Bitmap m_bgImage;
    private int m_width, m_height;

    // 배경음악
    private MediaPlayer m_bgPlayer;
    private SoundPool m_sound;
    private int m_soundIdx;

    // 점수
    private int m_scoreHit = 0;
    private int m_scoreMiss = 0;

    // 참새 생성 시간
    private float m_timer = 0;
    private Paint m_scorePaint = new Paint();

    private List<Sparrow> SparrowList;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_context = context;

        //== 공용리소스 대체로 추가
        CommonBird.set(context, R.drawable.sparrow, 6);

        init();
    }

    private void init() {
        // 동기화 가능 ArrayList 초기화
        SparrowList = Collections.synchronizedList(new ArrayList<Sparrow>());

        m_bgPlayer = MediaPlayer.create(m_context, R.raw.rondo);
        m_bgPlayer.setLooping(true);
        m_bgPlayer.start();

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            m_sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 1);
        } else {
            AudioAttributes audioAttrs = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            m_sound = new SoundPool.Builder()
                    .setAudioAttributes(audioAttrs)
                    .setMaxStreams(5)
                    .build();
        }

        m_soundIdx = m_sound.load(m_context, R.raw.fire, 1);

        m_scorePaint.setTextSize(60);
        m_scorePaint.setColor(Color.WHITE);
    }

    // fab 버튼을 클릭하여 게임을 초기화한다.
    public void initGame() {

        synchronized (SparrowList){
            SparrowList.clear();
        }

        m_scoreHit = m_scoreMiss = 0;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(m_bgImage, 0, 0, null);

        synchronized (SparrowList){
            for(Sparrow itm : SparrowList){
                canvas.rotate(itm.getAngle(), itm.getBirdX(), itm.getBirdY());
                canvas.drawBitmap(itm.getBird()
                               , itm.getBirdX() - itm.getBirdWidth()
                               , itm.getBirdY() - itm.getBirdHeight()
                               , null);
                canvas.rotate(-itm.getAngle(), itm.getBirdX(), itm.getBirdY());
            }
        }

        m_scorePaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Hit : " + m_scoreHit
                      , 100
                      , 100
                      , m_scorePaint);

        m_scorePaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Miss : " + m_scoreMiss
                      , m_width - 100
                      , 100
                      , m_scorePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        m_width = w;
        m_height = h;

        m_bgImage = BitmapFactory.decodeResource(getResources(), R.drawable.back);
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
            fireBullet(event.getX(), event.getY());
        }

        return true;
    }

    private void makeSparrow(){
        m_timer -= BirdTime.deltaTime;

        if(m_timer <= 0){
            m_timer = 0.5f;
            synchronized (SparrowList){
                //== 공용리소스 대체로 Sparrow 생성자의 파라메터 변경
                SparrowList.add(new Sparrow(m_width, m_height));
            }
        }
    }

    private void moveSparrow(){
        synchronized (SparrowList){
            for(Sparrow itm : SparrowList){
                itm.moveToNext();
            }
        }
    }

    private void removeSparrow(){
        synchronized (SparrowList){
            for(int i = SparrowList.size() - 1; i>=0; i--){
                if(SparrowList.get(i).getIsDead()){
                    SparrowList.remove(i);
                }
            }
        }
    }

    // 화면을 클릭(터치)하여 해당 지점으로 총알을 발사한다.
    private void fireBullet(float x, float y){
        boolean isHit = false;

        m_sound.play(m_soundIdx, 1, 1, 1, 0, 1);

        for(Sparrow itm : SparrowList){
            if(itm.hitTest(x, y)){
                isHit = true;
                break;
            }
        }

        if(isHit) { m_scoreHit++; } else { m_scoreMiss++; }

    }

    class GameThread extends Thread{
        public boolean CanRun = true;

        @Override
        public void run() {
            while(CanRun){
                try{

                    BirdTime.updateTime();

                    makeSparrow();
                    moveSparrow();
                    removeSparrow();
                    postInvalidate();

                    sleep(10);

                } catch (Exception ex) {
                    // Do something ...
                }
            }
        }
    }
}

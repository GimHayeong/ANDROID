package org.hyg.breakout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;

/**
 * Created by shiny on 2018-03-05.
 */

public class CommonResources {

    // Paddle
    static private Bitmap[] m_bmpPaddles = new Bitmap[3];
    static public Bitmap[] getPaddles() { return m_bmpPaddles; }

    // Ball, Ball Icon 이미지, 크기
    static private Bitmap m_bmpBall, m_bmpBallIcon;
    static public Bitmap getBall() { return m_bmpBall; }
    static public Bitmap getBallIcon() { return m_bmpBallIcon; }
    static private int m_br;
    static public int getBallSize() { return m_br; }

    // Block 이미지, 크기
    static private Bitmap[] m_bmpBlocks = new Bitmap[3];
    static public Bitmap[] getBlock() { return m_bmpBlocks; }
    static private int m_bw, m_bh;
    static public int getBlockWidth() { return m_bw; }
    static public int getBlockHeight() { return m_bh; }

    // 배경 이미지
    static private Bitmap[] m_bmpBacks = new Bitmap[4];
    static public Bitmap[] getBack() { return m_bmpBacks; }

    // 사운드, 사운드 아이디
    static private SoundPool m_spSound;
    static private int[] m_sndIds = new int[5];

    // 진동
    static private Vibrator m_vib;

    static public void set(Context context, int width, int height){
        initPaddle(context);
        initBall(context);
        initBlock(context, width, height);
        initBackground(context, width, height);
        initSound(context);
    }

    /**
     * 사운드 및 진동 재생
     * @param kind
     */
    static public void playSound(int kind){
        if(Settings.getIsSound()){
            m_spSound.play(m_sndIds[kind], 0.9f, 0.9f, 1, 0, 1);
        }

        if(Settings.getIsVib()){
            // AndroidManifests.xml 파일에 추가 필요<use-permission android:name="android.permission.VIBRATE"/>
            m_vib.vibrate(30);
        }
    }

    /**
     * 패들이미지 초기화
     * @param context
     */
    private static void initPaddle(Context context) {
        for(int i=0; i<m_bmpPaddles.length; i++){
            m_bmpPaddles[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.paddle1 + i);
        }
    }

    /**
     * 볼 이미지 초기화
     * @param context
     */
    private static void initBall(Context context) {
        m_bmpBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
        m_br = m_bmpBall.getWidth() / 2;

        m_bmpBallIcon = Bitmap.createScaledBitmap(m_bmpBall, m_br, m_br, true);
    }

    /**
     * 블럭 이미지 초기화
     * @param context
     * @param width : 화면크기
     * @param height : 화면크기
     */
    private static void initBlock(Context context, int width, int height) {
        m_bw = width / 6;
        m_bh = height / 20;

        Bitmap bmp;
        for(int i=0; i<m_bmpBlocks.length; i++){
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.block1 + i);
            m_bmpBlocks[i] = Bitmap.createScaledBitmap(bmp, m_bw, m_bh, true);
        }

        m_bw /= 2;
        m_bh /= 2;
    }

    /**
     * 배경 이미지 초기화
     * @param context
     * @param width : 화면크기
     * @param height : 화면크기
     */
    private static void initBackground(Context context, int width, int height) {
        Bitmap bmp;
        for(int i=0; i<m_bmpBacks.length; i++){
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.back1 + i);
            m_bmpBacks[i] = Bitmap.createScaledBitmap(bmp, width, height, true);
        }
    }

    /**
     * 사운드 및 진동 초기화
     * @param context
     */
    private static void initSound(Context context) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            m_spSound = new SoundPool(5, AudioManager.STREAM_MUSIC, 1);
        } else {
            AudioAttributes attrs = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            m_spSound = new SoundPool.Builder()
                    .setAudioAttributes(attrs)
                    .setMaxStreams(5)
                    .build();
        }

        for(int i=0; i<m_sndIds.length; i++){
            m_sndIds[i] = m_spSound.load(context, R.raw.sound1 + i, 1);
        }

        m_vib = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }


}

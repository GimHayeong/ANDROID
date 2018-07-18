package org.hyg.spiderbyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.Random;

/**
 * Created by shiny on 2018-02-24.
 */

public class CommonResources {

    static private Bitmap[] m_bmpSpiders = new Bitmap[5];
    static public Bitmap[] getSpider() { return m_bmpSpiders; }
    static private int m_sw, m_sh;
    static public int getSpiderWidth() { return m_sw; }
    static public int getSpiderHeight() { return m_sh; }

    // 독액 이미지, 반지름
    static private Bitmap m_bmpPoison;
    static public Bitmap getPoison() { return m_bmpPoison; }
    static private int m_pr;
    static public int getPoisonSize() { return m_pr; }
    //static private int m_pw, m_ph;
    //static public int getPoisonWidth() { return m_pw; }
    //static public int getPoisonHeight() { return m_ph; }

    static private SoundPool m_spbSound;
    static private int m_sudPoison;

    /*================= 나비 추가관련 변수 */
    // 나비 종류, 나비이미지, 나비 포획시 사운드
    static public final int BUTTERFLY_KIND = 6;
    static private Bitmap[][] m_bmpButterflies = new Bitmap[BUTTERFLY_KIND][10];
    static public Bitmap[][] getButterfly() { return m_bmpButterflies; }
    static private int m_sndCapture;
    static private int m_bw, m_bh;
    static public int getButterflyWidth() { return m_bw; }
    static public int getButterflyHeight() { return m_bh; }
    /* 나비 추가관련 변수 =================*/

    // 리소스 초기화
    static public void init(Context context){
        initSpider(context);
        initPoison(context);
        initButterfly(context);
        initSound(context);
    }

    /**
     * 거미 이미지 초기화
     *  : 한 줄에 5개 동작의 이미지가 연결된 비트맵을 5개로 나누어 초기화
     * @param context
     */
    static private void initSpider(Context context){
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.spider);

        m_sw = bmp.getWidth() / 5;
        m_sh = bmp.getHeight();

        for(int i=0; i<5; i++){
            m_bmpSpiders[i] = Bitmap.createBitmap(bmp, m_sw * i, 0, m_sw, m_sh);
        }

        m_sw /= 2;
        m_sh /= 2;
    }

    /**
     * 레이저 이미지 초기화
     * @param context
     */
    static private void initPoison(Context context){
        m_bmpPoison = BitmapFactory.decodeResource(context.getResources(), R.drawable.poison);

        m_pr = m_bmpPoison.getWidth() / 2;
    }

    /**
     * 나비 이미지 초기화
     *  : 한 줄에 10개 동작의 이미지가 연결된 비트맵을 10개로 나누어 초기화
     * @param context
     */
    static private void initButterfly(Context context){
        Bitmap bmpOrigin = BitmapFactory.decodeResource(context.getResources(), R.drawable.butterfly);

        int ow =  bmpOrigin.getWidth();
        int oh = bmpOrigin.getHeight();

        Random rnd = new Random();

        // 알파값을 가진 표현색상이 풍부한 비트맵객체(8888)
        // , 알파값을 가진 표현색이 적은 비트맵객체(4444)
        // , 알파값 없고 표현색이 적은(565)
        Bitmap bmpAlpha;

        // 랜덤색상
        int color;

        // 랜덤색상필터
        Paint patColorFilter;

        // 10개 동작이 연결된 이미지의 분할된 1개 이미지 넓이
        int sw;

        Canvas cvs;

        // 종류별로 랜덤한 색상필터가 적용된 나비 이미지 초기화
        for(int i=0; i<BUTTERFLY_KIND; i++){
            bmpAlpha = Bitmap.createBitmap(ow, oh, Bitmap.Config.ARGB_4444);
            patColorFilter = new Paint();

            // 랜덤색상필터
            color = rnd.nextInt(0x808080) + 0x808080;
            ColorFilter filter = new LightingColorFilter(color, 0x404040);
            patColorFilter.setColorFilter(filter);

            cvs = new Canvas(bmpAlpha);
            cvs.drawBitmap(bmpOrigin, 0, 0, patColorFilter);

            sw = ow / 10;
            for(int j=0; j<10; j++){
                m_bmpButterflies[i][j] = Bitmap.createBitmap(bmpAlpha, sw * j, 0, sw, oh);
            }
        }

        // m_bw = (ow / 10) / 2;
        m_bw = ow / 20;
        m_bh = oh / 2;
    }

    // 레이저 사운드 초기화
    static private void initSound(Context context){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            m_spbSound = new SoundPool(5, AudioManager.STREAM_MUSIC, 1);
        } else {
            AudioAttributes audioAttrs = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            m_spbSound = new SoundPool.Builder()
                    .setAudioAttributes(audioAttrs)
                    .setMaxStreams(5)
                    .build();
        }

        m_sudPoison = m_spbSound.load(context, R.raw.poison, 1);
        m_sndCapture = m_spbSound.load(context, R.raw.capture, 1);
    }

    // 사운드 재생
    static public void playSound(String kind) {
        switch(kind){
            case "Poison":
                m_spbSound.play(m_sudPoison, 1, 1, 1, 0, 1);
                break;
            case "Capture":
                m_spbSound.play(m_sndCapture, 1, 1, 1, 0, 1);
                break;
        }


    }
}

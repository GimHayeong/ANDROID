package org.hyg.slidingpuzzlebyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;

/**
 * Created by shiny on 2018-03-07.
 */

public class CommonResources {
    static int MarginHeight, MarginWidth;

    // 타일 배열, 타일 크기
    static private Bitmap[] m_bmpTiles;
    static public Bitmap[] getTile() { return m_bmpTiles; }
    static private int m_tw;
    static public int getTileWidth() { return m_tw; }

    // 액자, 액자 좌표
    static private Bitmap m_bmpFrame;
    static public Bitmap getFrame() { return m_bmpFrame; }
    static private int m_fx, m_fy;
    static public int getXFrame() { return m_fx; }
    static public int getYFrame() { return m_fy; }

    static private SoundPool m_spSound;
    static private int m_sndId;
    static private Vibrator m_vib;

    static public void init(Context context, int w, int h){
        initTile(context, w, h);
        initFrame(context, w, h);
        initSound(context);
    }

    static public void playSound(){
        if(Settings.IsSound){
            m_spSound.play(m_sndId, 0.9f, 0.9f, 1, 0, 1);
        }

        if(Settings.IsVib){
            m_vib.vibrate(30);
        }
    }

    /**
     * n x n 타일 초기화
     *  : 마지막 타일은 공백이므로 만들지 않은(tn * tn - 1)
     * @param context
     * @param w : 화면 크기
     * @param h : 화면 크기
     */
    private static void initTile(Context context, int w, int h) {
        int tn = Settings.Size;
        m_bmpTiles = new Bitmap[tn * tn];

        //퍼즐 오른쪽에 마진이 남아 임의의 50을 더해서 화면을 맞춤(width, height 크기 인식 문제인듯...)
        MarginWidth = (w - h) / 2 + 50;
        MarginHeight = h / 10;

        m_tw = (h - MarginHeight * 2) / tn;

        Bitmap bmp;
        for(int i=0; i<tn * tn - 1; i++){
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.tile01 + i);
            m_bmpTiles[i] = Bitmap.createScaledBitmap(bmp, m_tw, m_tw, true);
        }
    }

    /**
     * 프레임 초기화
     * @param context
     * @param w : 화면 크기
     * @param h : 화면 크기
     */
    private static void initFrame(Context context, int w, int h) {
        m_bmpFrame = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame);
        m_bmpFrame = Bitmap.createScaledBitmap(m_bmpFrame
                                            , w - MarginWidth * 2
                                            , h - MarginHeight
                                            , true);

        m_fx = MarginWidth - MarginHeight / 2;
        m_fy = MarginHeight / 2;
    }

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

        m_sndId = m_spSound.load(context, R.raw.sound1, 1);
        m_vib = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }




}

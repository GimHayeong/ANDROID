package org.hyg.alienbyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by shiny on 2018-02-24.
 */

public class CommonResources {

    static private Bitmap m_bmpAlien;
    static public Bitmap getAlien() { return m_bmpAlien; }
    static private int m_aw, m_ah;
    static public int getAlienWidth() { return m_aw; }
    static public int getAlienHeight() { return m_ah; }

    static private Bitmap m_bmpLaser;
    static public Bitmap getLaser() { return m_bmpLaser; }
    static private int m_lw, m_lh;
    static public int getLaserWidth() { return m_lw; }
    static public int getLaserHeight() { return m_lh; }

    static private SoundPool m_spbSound;
    static private int m_sudLaser;


    // 리소스 초기화
    static public void init(Context context){
        initAlien(context);
        initLaser(context);
        initSound(context);
    }

    // 에어리언 이미지 초기화
    static private void initAlien(Context context){
        m_bmpAlien = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien);

        m_aw = m_bmpAlien.getWidth() / 2;
        m_ah = m_bmpAlien.getHeight() / 2;
    }

    // 레이저 이미지 초기화
    static private void initLaser(Context context){
        m_bmpLaser = BitmapFactory.decodeResource(context.getResources(), R.drawable.laser);

        m_lw = m_bmpLaser.getWidth();
        m_lh = m_bmpLaser.getHeight();
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

        m_sudLaser = m_spbSound.load(context, R.raw.laser, 1);
    }

    // 사운드 재생
    static public void playSound() {
        m_spbSound.play(m_sudLaser, 1, 1, 1, 0, 1);
    }
}

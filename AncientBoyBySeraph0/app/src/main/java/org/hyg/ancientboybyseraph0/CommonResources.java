package org.hyg.ancientboybyseraph0;

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

    static private Bitmap[][] m_bmpBoys = new Bitmap[3][5];
    static public Bitmap[][] getBoys() { return m_bmpBoys; }
    static private int m_bw, m_bh;
    static public int getBoyWidth() { return m_bw; }
    static public int getBoyHeight() { return m_bh; }

    static private Bitmap m_bmpShadow;
    static public Bitmap getShadow() { return m_bmpShadow; }
    static private int m_sw, m_sh;
    static public int getShadowWidth() { return m_sw; }
    static public int getShadowHeight() { return m_sh; }


    // 리소스 초기화
    static public void init(Context context){
        initBoy(context);
        initPoison(context);
    }

    // 소년 이미지 초기화
    static private void initBoy(Context context){
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy);

        m_bw = bmp.getWidth() / 5;
        m_bh = bmp.getHeight() / 3;

        for(int i=0; i<3; i++){
            for(int j=0; j<5; j++) {
                m_bmpBoys[i][j] = Bitmap.createBitmap(bmp, m_bw * j, m_bh * i, m_bw, m_bh);
            }
        }

        m_bw /= 2;
        m_bh /= 2;

        m_sw = m_bw / 2;
        m_sh = m_bh / 4;
    }

    // 그림자 이미지 초기화
    static private void initPoison(Context context){
        m_bmpShadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.shadow);
        m_bmpShadow = Bitmap.createScaledBitmap(m_bmpShadow, m_sw * 2, m_sh * 2, true);
    }



    /*
    //static private SoundPool m_spbSound;
    //static private int m_sudPoison;

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

    }


    // 사운드 재생
    static public void playSound() {
        m_spbSound.play(m_sudPoison, 1, 1, 1, 0, 1);
    }
    */
}

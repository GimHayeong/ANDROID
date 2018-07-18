package org.hyg.destoryalienbyseraph0;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by shiny on 2018-02-24.
 */

public class CommonResources {

    // X-Wing
    static private Bitmap[] m_bmpXWings = new Bitmap[2];
    static public Bitmap[] getXWing() { return m_bmpXWings; }
    static private int m_xw, m_xh;
    static public int getXWingWidth() { return m_xw; }
    static public int getXWingHeight() { return m_xh; }

    // Alien
    static private Bitmap m_bmpAlien;
    static public Bitmap getAlien() { return m_bmpAlien; }
    static private int m_aw, m_ah;
    static public int getAlienWidth() { return m_aw; }
    static public int getAlienHeight() { return m_ah; }

    // Laser
    static private Bitmap m_bmpLaser;
    static public Bitmap getLaser() { return m_bmpLaser; }
    static private int m_lw, m_lh;
    static public int getLaserWidth() { return m_lw; }
    static public int getLaserHeight() { return m_lh; }

    // Torpedo
    static private Bitmap m_bmpTorpedo;
    static public Bitmap getTorpedo() { return m_bmpTorpedo; }
    static private int m_tw, m_th;
    static public int getTorpedoWidth() { return m_tw; }
    static public int getTorpedoHeight() { return m_th; }

    // Explosion
    static private Bitmap[] m_bmpExplosions = new Bitmap[25];
    static public Bitmap[] getExplosion() { return m_bmpExplosions; }
    static private int m_ew, m_eh;
    static public int getExplosionWidth() { return m_ew; }
    static public int getExplosionHeight() { return m_eh; }

    static private SoundPool m_spbSound;
    static private int m_sudLaser;
    static private int m_sndBigExp;
    static private int m_sndSmallExp;

    /**
     * Laser, Torpedo, Alien, Explosion 클래스 객체를 공용 리소스에서 관리
     */
    static public List<Laser> LaserList = Collections.synchronizedList(new ArrayList<Laser>());
    static public List<Torpedo> TorpedoList = Collections.synchronizedList(new ArrayList<Torpedo>());
    static public List<Alien> AlienList = Collections.synchronizedList(new ArrayList<Alien>());
    static public List<Explosion> ExplosionList = Collections.synchronizedList(new ArrayList<Explosion>());


    // 리소스 초기화
    static public void init(Context context){
        initXWing(context);
        initAlien(context);
        initLaser(context);
        initTorpedo(context);
        initExplosion(context);
        initSound(context);
    }

    /**
     * 전투기 이미지 초기화
     *  : 깜빡이는 동작을 위해 ColorFilter 를 씌운 이미지를 하나 더 만들어 초기화
     * @param context
     */
    static private void initXWing(Context context){
        m_bmpXWings[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.xwing);

        m_xw = m_bmpXWings[0].getWidth() / 2;
        m_xh = m_bmpXWings[0].getHeight() / 2;

        ColorFilter redFilter = new LightingColorFilter(0xff0000, 0x404040);
        Paint patFilter = new Paint();
        patFilter.setColorFilter(redFilter);

        m_bmpXWings[1] = Bitmap.createBitmap(m_xw * 2, m_xh * 2, Bitmap.Config.ARGB_8888);
        Canvas cvs = new Canvas(m_bmpXWings[1]);
        cvs.drawBitmap(m_bmpXWings[1], 0, 0, patFilter);
    }

    /**
     * 에일리언 이미지 초기화
     * @param context
     */
    static private void initAlien(Context context){
        m_bmpAlien = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien);

        m_aw = m_bmpAlien.getWidth() / 2;
        m_ah = m_bmpAlien.getHeight() / 2;
    }

    /**
     * 레이저 이미지 초기화
     * @param context
     */
    static private void initLaser(Context context){
        m_bmpLaser = BitmapFactory.decodeResource(context.getResources(), R.drawable.laser);

        m_lw = m_bmpLaser.getWidth() / 2;
        m_lh = m_bmpLaser.getHeight() / 2;
    }

    /**
     * 레이저 추가
     * @param width : 화면크기 넓이
     * @param height : 화면크기 높이
     * @param x : 레이저 초기위치 x좌표
     * @param y : 레이저 초기위치 y좌표
     */
    static public void addLaser(int width, int height, float x, float y){
        synchronized (LaserList){
            LaserList.add(new Laser(width, height, x, y));
        }
    }

    /**
     * 에일리언 추가
     * @param width : 화면크기 넓이
     * @param height : 화면크기 높이
     */
    static public void addAlien(int width, int height){
        synchronized (AlienList){
            AlienList.add(new Alien(width, height));
        }
    }

    /**
     * 어뢰 추가
     * @param width : 화면크기 넓이
     * @param height : 화면크기 높이
     * @param x : 어뢰 초기위치 x좌표
     * @param y : 어뢰 초기위치 y좌표
     */
    static public void addTorpedo(int width, int height, float x, float y){
        synchronized (TorpedoList){
            TorpedoList.add(new Torpedo(width, height, x, y));
        }
    }

    static public void addExplosion(float x, float y, String kind){
        synchronized (ExplosionList){
            ExplosionList.add(new Explosion(x, y, kind));
        }
    }

    /**
     * 객체 이동
     */
    static public void moveObject(){
        synchronized (LaserList){
            for(Laser itm : LaserList){
                itm.moveToNext();
            }
        }

        synchronized (AlienList){
            for(Alien itm : AlienList){
                itm.moveToNext();
            }
        }

        synchronized (TorpedoList){
            for(Torpedo itm : TorpedoList){
                itm.moveToNext();
            }
        }

        synchronized (ExplosionList){
            for(Explosion itm : ExplosionList){
                itm.moveToNext();
            }
        }
    }


    /**
     * 소멸대상 객체 제거
     */
    static public void removeObject() {
        synchronized (LaserList){
            for(int i=LaserList.size() - 1; i>=0; i--){
                if(LaserList.get(i).getIsDestructed()){
                    LaserList.remove(i);
                }
            }
        }

        synchronized (TorpedoList){
            for(int i=TorpedoList.size() - 1; i>=0; i--){
                if(TorpedoList.get(i).getIsDestructed()){
                    TorpedoList.remove(i);
                }
            }
        }

        synchronized (ExplosionList){
            for(int i=ExplosionList.size() - 1; i>=0; i--){
                if(ExplosionList.get(i).getIsDestructed()){
                    ExplosionList.remove(i);
                }
            }
        }
    }


    /**
     * 어뢰 이미지 초기화
     * @param context
     */
    static private void initTorpedo(Context context){
        m_bmpTorpedo = BitmapFactory.decodeResource(context.getResources(), R.drawable.torpedo);

        m_tw = m_bmpTorpedo.getWidth() / 2;
        m_th = m_bmpTorpedo.getHeight() / 2;
    }

    /**
     * 폭발 이미지 초기화
     *  : 한 줄에 5개씩 5줄의 동작의 이미지가 연결된 비트맵을 25개로 나누고 크기가 작으므로 2배 확대하여 초기화
     * @param context
     */
    static private void initExplosion(Context context){
        Bitmap bmpOrigin = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion);

        m_ew = bmpOrigin.getWidth() / 5;
        m_eh = bmpOrigin.getHeight() / 5;

        int idx = 0;
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++) {
                m_bmpExplosions[idx] = Bitmap.createBitmap(bmpOrigin
                                                        , m_ew * j
                                                        , 0 * i
                                                        , m_ew
                                                        , m_eh);
                m_bmpExplosions[idx] = Bitmap.createScaledBitmap(m_bmpExplosions[idx]
                                                              , m_ew * 2
                                                              , m_eh * 2
                                                              , true);
                idx++;
            }
        }

        m_ew /= 2;
        m_eh /= 2;
    }


    /**
     * 사운드 초기화
     * @param context
     */
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
        m_sndBigExp = m_spbSound.load(context, R.raw.big_explosion, 1);
        m_sndSmallExp = m_spbSound.load(context, R.raw.small_explosion, 1);
    }

    // 사운드 재생
    static public void playSound(String kind) {
        switch(kind){
            case "Laser":
                m_spbSound.play(m_sudLaser, 1, 1, 1, 0, 1);
                break;

            case "Big":
                m_spbSound.play(m_sndBigExp, 1, 1, 1, 0, 1);
                break;

            case "Small":
                m_spbSound.play(m_sndSmallExp, 1, 1, 1, 0, 1);
                break;
        }
    }
}

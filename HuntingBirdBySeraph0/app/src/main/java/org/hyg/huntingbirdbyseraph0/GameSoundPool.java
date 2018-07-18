package org.hyg.huntingbirdbyseraph0;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by shiny on 2018-02-21.
 */

public class GameSoundPool {

    static SoundPool Sound;

    public GameSoundPool(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){

            // 롤리팝 이전 버전인 경우
            Sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 1);

        } else {

            // 롤리팝 이후 버전인 경우
            AudioAttributes audioAttr = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            Sound = new SoundPool.Builder()
                    .setAudioAttributes(audioAttr)
                    .setMaxStreams(5)
                    .build();

        }
    }

    /**
     * @soundId : 오디오 리소스 아이디
     * @leftVolumn : 왼쪽 볼륨(0 ~ 1)
     * @rightVolumn : 오른쪽 볼륨(0 ~ 1)
     * @priority : 우선순위
     * @loop : 반복횟수 (-1: 무한반복)
     * @rate : 재생속도 (0.5 ~ 2)
     **/
    public void playSound(int soundId, float leftVolumn, float rightVolumn, int priority, int loop, float rate){
        Sound.play(soundId, leftVolumn, rightVolumn, priority, loop, rate);
    }
}

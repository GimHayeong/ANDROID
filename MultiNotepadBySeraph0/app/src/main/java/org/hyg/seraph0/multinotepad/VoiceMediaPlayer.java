package org.hyg.seraph0.multinotepad;

import android.media.MediaPlayer;

/**
 * Created by shiny on 2018-04-20.
 */

public class VoiceMediaPlayer {
    private static MediaPlayer mPlayer = null;

    public static MediaPlayer getMediaPlayerInstance() {
        if(mPlayer == null) {
            mPlayer = new MediaPlayer();
        }

        return mPlayer;
    }
}

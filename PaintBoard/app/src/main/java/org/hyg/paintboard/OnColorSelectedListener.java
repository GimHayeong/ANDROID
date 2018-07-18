package org.hyg.paintboard;


import android.net.sip.SipAudioCall;

/**
 * Created by shiny on 2018-03-21.
 */

public interface OnColorSelectedListener {
    void onColorSelected(int color);

    /*
    public static interface OnColorSelectedListener {
        void onColorSelected(int color);
    }
    public static OnColorSelectedListener SelectedListener;
    public void setOnColorSelectedListener(OnColorSelectedListener listener){
        SelectedListener = listener;
    }*/
}

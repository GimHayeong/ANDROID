package org.hyg.seraph0.multinotepad;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * 동영상 재생
 */
public class VideoPlayActivity extends AppCompatActivity {

    private final String TAG = "[MEMO-VDO-PLAY]";

    private VideoView mVideo;
    private String mUri;

    //TODO:
    private Handler mHandler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_video_playing);

        init();
    }

    private void initWindowLandscape() {
        final Window win = getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void init() {

        mVideo = (VideoView)findViewById(R.id.viwVideo);

        Intent intent = getIntent();
        mUri = intent.getStringExtra(BasicInfo.KEY_URI_VIDEO);
        //mUri = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        mVideo.setVideoPath(mUri);

        MediaController controller = new MediaController(VideoPlayActivity.this);
        mVideo.setMediaController(controller);
        mVideo.start();
    }
}

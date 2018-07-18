package org.hyg.audioplayerbyseraph0;

import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class TheaterMainActivity extends AppCompatActivity {

    private final String TAG = "Theater";
    private final static String RECORED_FILE = "video_recorded";
    private MediaPlayer mPlayer;
    private MediaRecorder mRecorder;

    private SurfaceHolder mSHolder;
    private String mCurtFileName;

    /**
     * 동영상 녹화 시작/중지 버튼
     */
    private Button mBtnStart, mBtnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_main);

        initVideo();
    }

    /**
     *   - 서피스뷰 객체 생성/설정 : 카메라 미리보기 기능
     *
     */
    private void initVideo() {

        /**
         * 카메라 미리보기 SurfaceView 객체 생성 및 설정
         */
        SurfaceView sfvPreview = new SurfaceView(this);
        mSHolder = sfvPreview.getHolder();
        mSHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        /**
         * 레이아웃에 SurfaceView 추가
         */
        FrameLayout frame = (FrameLayout)findViewById(R.id.frmVideo);
        frame.addView(sfvPreview);

        mBtnStart = (Button)findViewById(R.id.btnStartRecording);
        mBtnStop = (Button)findViewById(R.id.btnStopRecording);
        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnStartRecording:
                        startRecording();
                        break;

                    case R.id.btnStopRecording:
                        stopRecording();
                        break;
                }
            }
        };
        mBtnStart.setOnClickListener(OnButtonClick);
        mBtnStop.setOnClickListener(OnButtonClick);
    }

    /**
     * 동영상 녹화 버튼 클릭 이벤트
     */
    private void startRecording() {
        try{

            /**
             * MediaRecorder 객체 생성 및 설정
             */
            if (mRecorder == null) { mRecorder = new MediaRecorder(); }

            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            mCurtFileName = getFileName();
            mRecorder.setOutputFile(mCurtFileName);
            mRecorder.setPreviewDisplay(mSHolder.getSurface());

            /**
             * 녹화 시작
             */
            mRecorder.prepare();
            mRecorder.start();

        } catch (Exception ex) {
            Log.e(TAG, "Exception: ", ex);

            mRecorder.release();
            mRecorder = null;
        }
    }

    /**
     * 동영상 녹화 중지 버튼 클릭 이벤트
     */
    private void stopRecording() {
        if(mRecorder == null) { return; }

        /**
         * 리소스 해제
         */
        mRecorder.stop();
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;

        ContentValues params = new ContentValues(10);

        params.put(MediaStore.MediaColumns.TITLE, "RecordedVideo");
        params.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
        params.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
        params.put(MediaStore.Audio.Media.ALBUM, "video Album");
        params.put(MediaStore.Audio.Media.ARTIST, "Moon");
        params.put(MediaStore.Audio.Media.DISPLAY_NAME, "Recorded Video");
        params.put(MediaStore.Audio.Media.DATA, mCurtFileName);

        /**
         * 녹화된 파일을 내용제공자를 통해 동영상 목록으로 저장
         */
        Uri videoUri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, params);

        if(videoUri == null) {
            Log.d(TAG, "failed insert video.");
            return;
        }

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, videoUri));

    }

    private String getFileName() {
        return "/sdcard/" + RECORED_FILE + ".mp4";
    }
}

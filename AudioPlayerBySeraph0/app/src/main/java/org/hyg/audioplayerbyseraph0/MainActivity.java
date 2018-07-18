package org.hyg.audioplayerbyseraph0;

import android.content.ContentValues;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "AUDIO-PLAYER";
    private Button mBtnPlay, mBtnNext, mBtnStop;
    private TextView mTxtTitle;
    static final String AUDIO_URL = "http://sites.google.com/site/ubiaccessmobile/sample_audio.amr";
    private MediaPlayer mPlayer;
    private int mPlaybackPosition = 0;
    private boolean mIsPaused = false;
    private void setIsPaused(boolean isPaused) {
        mIsPaused = isPaused;
        if(isPaused) {
            mBtnPlay.setText(R.string.pause_button_text);
        } else {
            mBtnPlay.setText(R.string.play_button_text);
        }
        mBtnStop.setEnabled(!isPaused);
        mBtnStop.setClickable(!isPaused);
    }


    /**
     * 녹음기
     */
    private final static String RECORDED_FILE = "/sdcard/recorded.mp4";
    private Button mBtnRecording, mBtnPlayRecording;
    private boolean mIsRecording = false, mIsPlayTheRecording = false;
    private void setIsRecording(boolean value) { mIsRecording = value;}
    private void setIsPlayRecord(boolean value) { mIsPlayTheRecording = value; }
    private MediaPlayer mPlayerRecoding;
    private MediaRecorder mRecorder;


    /**
     * 동영상 재생
     */
    private final String VIDEO_URL = "http://sites.google.com/site/ubiaccessmobile/sample_video.mp4";
    private VideoView mVdoMovie;
    private Button mBtnPlayMovie, mBtnVolUp, mBtnVolDn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initAudioPlayer();

        initRecorder();

        //initMoviePlayer();
    }

    /**
     * [동영상 재생] 관련 위젯 초기화
     * VideView 를 통해 동영상 재생
     */
    private void initMoviePlayer() {
        mBtnPlayMovie = (Button)findViewById(R.id.btnPlayVideo);
        mBtnVolUp = (Button)findViewById(R.id.btnVolUp);
        mBtnVolDn = (Button)findViewById(R.id.btnVolDn);
        mVdoMovie = (VideoView)findViewById(R.id.vdoMovie);

        initMovie(VIDEO_URL);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnPlayVideo:
                        mVdoMovie.start();
                        break;

                    case R.id.btnVolUp:
                        setVolumeForMovie(1);
                        break;

                    case R.id.btnVolDn:
                        setVolumeForMovie(-1);
                        break;
                }
            }
        };
        mBtnPlayMovie.setOnClickListener(OnButtonClick);
        mBtnVolUp.setOnClickListener(OnButtonClick);
        mBtnVolDn.setOnClickListener(OnButtonClick);
    }

    /**
     * [동영상 음량 증/감]
     * 음량(0 ~ 10) 증감
     * @param i : 증/감 값
     */
    private void setVolumeForMovie(int i) {
        AudioManager manager = (AudioManager)getSystemService(AUDIO_SERVICE);

        final int maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volume = manager.getStreamVolume(AudioManager.STREAM_MUSIC) + i;
        if(volume > maxVolume) { volume = maxVolume; }

        manager.setStreamVolume(AudioManager.STREAM_MUSIC
                              , volume + i
                              , AudioManager.FLAG_SHOW_UI);
    }

    /**
     * [동영상 음소거]
     * 최대볼륨 설정/취소
     * @param isMute : 음소거 여부
     */
    private void setMuteForMovie(boolean isMute) {
        AudioManager manager = (AudioManager)getSystemService(AUDIO_SERVICE);

        final int maxVolume = (isMute) ? 0 : manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        manager.setStreamVolume(AudioManager.STREAM_MUSIC
                              , maxVolume
                              , AudioManager.FLAG_SHOW_UI);
    }


    /**
     * [동영상 재생 설정]
     *   - MediaController 생성
     *   - VideoView 객체에 연결
     *   - 재생할 동영상 URL 설정
     *   - VideoView 에 포커스를 두고 재생 대기
     * @param videoUrl
     */
    private void initMovie(String videoUrl) {
        MediaController controller = new MediaController(this);
        mVdoMovie.setMediaController(controller);
        mVdoMovie.setVideoURI(Uri.parse(videoUrl));
        mVdoMovie.requestFocus();
    }


    /**
     * [녹음기] 관련 위젯 초기화
     */
    private void initRecorder() {
        mBtnRecording = (Button)findViewById(R.id.btnRecording);
        mBtnPlayRecording = (Button)findViewById(R.id.btnPlayRecording);


        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnRecording:
                        recording(RECORDED_FILE);
                        break;

                    case R.id.btnPlayRecording:
                        playRecording(RECORDED_FILE);
                        break;
                }
            }
        };
        mBtnRecording.setOnClickListener(OnButtonClick);
    }

    /**
     * [녹음/취소] 토글 버튼 클릭 이벤트 처리
     * @param recordedFile
     */
    private void recording(String recordedFile) {
        if(!mIsRecording) {
            startRecording(recordedFile);
        } else {
            stopRecording();
        }

        mIsRecording = !mIsRecording;
        mIsPlayTheRecording = false;
    }

    /**
     * [녹음 시작]
     *   - 음성 녹음을 위한 MediaRecorder 객체 생성
     *   - 녹음파일형식 등 녹음 관련 정보 설정
     *   - prepare() 메서드로 녹음준비
     *   - start() 메서드로 녹음시작
     * @param recordedFile
     */
    private void startRecording(String recordedFile) {
        if(mRecorder != null){
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }

        mRecorder = new MediaRecorder();

        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mRecorder.setOutputFile(recordedFile);

        try{
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) { Log.e(TAG, "Recording: ", e); }

    }

    /**
     * [녹음 종료 및 파일 저장]
     *   - 녹음 중지 및 리소스 해제
     *   - 녹음 파일을 ContentResolver (내용 제공자)를 통해 녹음 목록으로 저장
     */
    private void stopRecording() {
        if(mRecorder == null) { return; }

        /**
         * 녹음 중지, 리소스 해제
         */
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        ContentValues params = new ContentValues();
        params.put(MediaStore.Audio.Media.ALBUM, "Audio Album");
        params.put(MediaStore.Audio.Media.ARTIST, "Moon");
        params.put(MediaStore.Audio.Media.DISPLAY_NAME, "Recorded Audio");
        params.put(MediaStore.Audio.Media.IS_RINGTONE, 1);
        params.put(MediaStore.Audio.Media.IS_MUSIC, 1);
        params.put(MediaStore.Audio.Media.DATA, RECORDED_FILE);
        params.put(MediaStore.MediaColumns.TITLE, "Recorded");
        params.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis()/1000);
        params.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp4");

        /**
         * 녹음 목록으로 저장
         */
        Uri audioUri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, params);

        if(audioUri == null) { Log.d(TAG, "failed insert audio."); }
    }

    /**
     * [녹음재생/중지] 버튼 클릭 이벤트 처리
     * @param recordedFile
     */
    private void playRecording(String recordedFile) {
        if(!mIsPlayTheRecording){
            startPlay(RECORDED_FILE, "");
        } else {
            pausePlay();
        }

        mIsPlayTheRecording = !mIsPlayTheRecording;
        mIsRecording = false;
    }

    private void initAudioPlayer() {

        mBtnPlay = (Button)findViewById(R.id.btnPlay);
        mBtnNext = (Button)findViewById(R.id.btnNext);
        mBtnStop = (Button)findViewById(R.id.btnStop);
        mTxtTitle = (TextView)findViewById(R.id.txtTitle);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnPlay:
                        startPlay(AUDIO_URL, "I like ...");
                        break;

                    case R.id.btnNext:
                        break;

                    case R.id.btnStop:
                        pausePlay();
                        break;
                }
            }
        };
        mBtnPlay.setOnClickListener(OnButtonClick);
        mBtnNext.setOnClickListener(OnButtonClick);
        mBtnStop.setOnClickListener(OnButtonClick);
    }

    /**
     * 재생 버튼 클릭 이벤트
     * @param audioUrl
     */
    private void startPlay(String audioUrl, String audioTitle) {
        try {
            playAudio(audioUrl, audioTitle);
        } catch (Exception ex) { Log.d(TAG, "Failed play audio." + ex.getMessage()); }
    }

    /**
     * 일시중지 버튼 클릭 이벤트
     */
    private void pausePlay() {
        try {
            mPlayer.stop();
            mPlaybackPosition = 0;
            setIsPaused(false);
        } catch (Exception ex) { Log.d(TAG, "Failed stop audio." + ex.getMessage()); }
    }


    /**
     *
     * @param audioUrl
     * @throws Exception
     */
    private void playAudio(String audioUrl, String audioTitle) throws Exception {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(audioUrl);
            mPlayer.prepare();
            mPlayer.start();
            mTxtTitle.setText(audioTitle);
            setIsPaused(false);
            MediaPlayer.OnCompletionListener OnCompletedPlay = new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mTxtTitle.setText("");
                    setIsPaused(false);
                }
            };
            mPlayer.setOnCompletionListener(OnCompletedPlay);
        } else {
            if(mIsPaused) {
                mPlayer.start();
                mPlayer.seekTo(mPlaybackPosition);
            } else {
                mPlaybackPosition = mPlayer.getCurrentPosition();
                mPlayer.pause();
            }
            setIsPaused(!mIsPaused);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        turnOffAudioPlayer();
    }

    private void turnOffAudioPlayer() {
        if(mPlayer != null) {
            try{
                mPlayer.release();
                setIsPaused(false);
            } catch (Exception ex) { Log.d(TAG, "Failed turn off audio." + ex.getMessage()); }
        }
    }


    /**
     * 액티비티 중지시 녹음 리소스 해제
     */
    @Override
    protected void onPause() {
        if(mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        super.onPause();
    }

    /**
     * 액티비티 재시작시 MediaRecorder 객체 생성 및 설정
     */
    @Override
    protected void onResume() {
        super.onResume();

        mRecorder = new MediaRecorder();
    }
}

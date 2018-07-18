package org.hyg.seraph0.multinotepad;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.hyg.seraph0.multinotepad.common.TitleBitmapButton;

import java.io.IOException;

/**
 * 음성 재생
 */
public class VoicePlayActivity_BACKUP extends AppCompatActivity {

    private static final String TAG = "[MEMO-VOICE-PLAY]";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private boolean mPermissionToRecordAccepted = false;
    private boolean mPermissionToReadExStorageAccepted = false;
    private String[] mPermissions = { Manifest.permission.RECORD_AUDIO };

    private TitleBitmapButton mBtnStartNStop;
    private ProgressBar mBarProgress;
    private TextView mTxtPlayingTime, mTxtTotalTime;

    private MediaPlayer mPlayer = null;
    private void setPlayer(float value) {
        if(mIsPlaying) {
            stop();
            mTime = 0;
            mPlayer = new MediaPlayer();
        }

        try {
            mPlayer.setDataSource(mUri);
            //TODO: prepare() -> prepareAsync() 했으나 완전하지 않음
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mPlayer.start();
                    mBtnStartNStop.setBackground(R.drawable.btn_voice_pause);
                    //TODO: post() -> postDelyed() 로 변경
                    mHandler.postDelayed(mRunUpdateTimer, 1000);
                }
            });
            mPlayer.prepareAsync();
            mBarProgress.setMax(mPlayer.getDuration() / 1000);
            mTime = new Float(value / 1000.0f).intValue();
            mBarProgress.setProgress(mTime);
            //mPlayer.start();
            //mBtnStartNStop.setBackground(R.drawable.btn_voice_pause);
            //mHandler.post(mRunUpdateTimer);

        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "IllegalArgumentException: " + ex.getMessage());
            mPlayer = null;
        } catch (IOException ex) {
            Log.d(TAG, "IOException: " + ex.getMessage());
            mPlayer = null;
        } catch (Exception ex) {
            Log.d(TAG, "Exception: " + ex.getMessage());
            mPlayer = null;
        } finally {
            Log.d(TAG, (mPlayer == null ? "Player is null." : "Player is not null."));
            Log.d(TAG, (mIsPlaying ? "Player is play." : "Player is stop."));
            if(mPlayer == null) { mIsPlaying = false; }
        }
    }

    private VoiceRecordingActivity.RemainingTimeCalculator mRemainTimeCalculator;
    private int mTime, mCurtTime;
    private boolean mIsPlaying, mIsHolding;

    private String mUri;
    private final Handler mHandler = new Handler();
    Runnable mRunUpdateTimer = new Runnable() {
        public boolean CanRun = true;
        @Override
        public void run() {
            while(CanRun) {
                if(mIsPlaying && mTime <= (mPlayer.getDuration() / 1000)) {
                    if(mTime > 0) {
                        mBarProgress.incrementProgressBy(1);
                    }

                    updateUI();
                } else {
                    mBarProgress.setProgress(mBarProgress.getMax());
                    if(mBarProgress.getProgress() == mBarProgress.getMax()) {
                        mBtnStartNStop.setBackground(R.drawable.btn_voice_play);
                        mIsPlaying = mIsHolding = false;

                        stop();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_playing);
        
        setTitle(R.string.audio_play_title);

        init();
    }



    private void init() {
        Intent intent = getIntent();
        mUri = intent.getStringExtra(BasicInfo.KEY_URI_VOICE);

        mIsPlaying = true;
        mIsHolding = false;
        mCurtTime = 0;
        
        mBtnStartNStop = (TitleBitmapButton)findViewById(R.id.btnStartNStop);
        mBtnStartNStop.setBackground(R.drawable.btn_voice_pause);

        mBarProgress = (ProgressBar)findViewById(R.id.barProgress);
        mTxtPlayingTime = (TextView)findViewById(R.id.txtPlayingTime);
        mTxtTotalTime = (TextView)findViewById(R.id.txtTotalPlayingTime);
        startPlayback(mUri);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.btnStartNStop:
                        if(mPlayer != null && mIsPlaying && !mIsHolding) {
                            mIsHolding = false;
                            mPlayer.pause();
                            mBtnStartNStop.setBackground(R.drawable.btn_voice_play);
                            mIsHolding = true;
                            mCurtTime = mBarProgress.getProgress();
                            mHandler.removeCallbacks(mRunUpdateTimer);
                        } else if(mPlayer != null && mIsHolding) {
                            mIsPlaying = true;
                            mPlayer.start();
                            mBtnStartNStop.setBackground(R.drawable.btn_voice_pause);

                            mIsHolding = false;
                            mBarProgress.setProgress(mCurtTime);
                            mHandler.postDelayed(mRunUpdateTimer, 1000);
                        } else {
                            mIsPlaying = true;
                            mBarProgress.setProgress(0);
                            startPlayback(mUri);
                        }

                        break;

                    case R.id.btnClose:
                        stop();
                        finish();
                        break;
                }
            }
        };
        mBtnStartNStop.setOnClickListener(OnButtonClick);
        ((TitleBitmapButton)findViewById(R.id.btnClose)).setOnClickListener(OnButtonClick);

        View.OnTouchListener OnProgressClick = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int action = event.getAction();
                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        final int barW = mBarProgress.getWidth();
                        final float curtX = event.getX();
                        final float curtOffset = curtX / (float)barW;

                        if(curtOffset > 0.0f && curtOffset < 1.0f) {
                            if(mPlayer != null) {
                                final int barOffset = new Float(curtOffset * 100).intValue();
                                final float offset = barOffset * ((float)mPlayer.getDuration());

                                setPlayer(offset);
                            }
                        }

                        break;

                    default:
                        break;
                }
                return true;
            }
        };
        mBarProgress.setOnTouchListener(OnProgressClick);
    }

    /**
     * 재생중지 및 관련 UI 업데이트
     */
    private void stop() {
        if(mPlayer != null) { stopPlayback(); }

        mIsPlaying = false;
        mBtnStartNStop.setBackground(R.drawable.btn_voice_play);
        mBarProgress.setProgress(0);
        mHandler.removeCallbacks(mRunUpdateTimer);
        mTxtPlayingTime.setText("00:00");
    }

    /**
     * 이전 재생시점부터 재생 시작
     * @param uri
     */
    private void startPlayback(String uri) {
        if(uri == null || uri.equals("")) { return; }
        mUri = uri;
        setPlayer(mPlayer == null ? 0 : mPlayer.getDuration());
    }

    /**
     * 재생 중지
     */
    private void stopPlayback() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    /**
     * Handler 에서 UI 를 업데이트 할때
     */
    public void updateUI() {
        //updateTimerView(mTxtPlayingTime);
    }

    /**
     * 시간 형식에 맞춰 TextView 에 표시
     * @param view
     */
    private void updateTimerView(TextView view) {
        view.setText(String.format("%02d:%02d", mTime / 60, mTime % 60));
        mTime++;
        mHandler.postDelayed(mRunUpdateTimer, 1000);
    }

    /**
     * Backspace 눌렀을때
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        stop();
        finish();
    }

    /**
     * Activity 일시정지 했을 때
     */
    @Override
    protected void onPause() {
        super.onPause();

        mHandler.removeCallbacks(mRunUpdateTimer);
    }






    /*
    private void checkPermission(){
        Log.d(TAG, "VoicePlayActivity.class onCreate()");
        //TODO: 동적 권한 체크 추가 했으나 동일한 오류 메시지와 다운 현상
        //W/generic: type=1400 audit(0.0:31): avc: denied { read } for path="/storage/emulated/0/memo/voice/1524049472839" dev="sdcardfs" ino=98385 scontext=u:r:mediaextractor:s0 tcontext=u:object_r:sdcardfs:s0 tclass=file permissive=0
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                mPermissionToRecordAccepted = true;
            } else {
                Log.d(TAG, "undefined permission.RECORD_AUDIO at manifest. need to request permission to user");

                ActivityCompat.requestPermissions(this, mPermissions, REQUEST_RECORD_AUDIO_PERMISSION);
            }
        }

        //TODO: 동적 권한 체크 추가 했으나 동일한 오류 메시지와 다운 현상
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                mPermissionToReadExStorageAccepted = true;
            } else {
                Log.d(TAG, "undefined permission.READ_EXTERNAL_STORAGE at manifest. need to request permission to user");
                ActivityCompat.requestPermissions(this, mPermissions, REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }

    //TODO: 동적 권한 체크 추가 했으나 동일한 오류 메시지와 다운 현상
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "granted permission.RECORD_AUDIO by user");
                    mPermissionToRecordAccepted = true;
                } else {
                    Log.d(TAG, "denied permission.RECORD_AUDIO by user");
                }
                break;

            case REQUEST_READ_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPermissionToReadExStorageAccepted = true;
                } else {
                    Log.d(TAG, "denied permission.READ_EXTERNAL_STORAGE by user");
                }
                break;
        }

        return;
    }
    */
}

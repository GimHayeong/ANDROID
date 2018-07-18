package org.hyg.seraph0.multinotepad;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.hyg.seraph0.multinotepad.common.TitleBitmapButton;

import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 * 음성 재생
 */
public class VoicePlayActivity extends AppCompatActivity {

    private static final String TAG = "[MEMO-VOICE-PLAY]";

    private TitleBitmapButton mBtnStartNStop;
    private ProgressBar mBarProgress;
    private TextView mTxtPlayingTime, mTxtTotalTime;
    private final int RES_PLAY_BUTTON = R.drawable.btn_voice_play;
    private final int RES_PAUSE_BUTTON = R.drawable.btn_voice_pause;
    /**
     * 초기 : mIsPlaying = false, mIsHolding = false;
     * 재생 : mIsPlaying = true, mIsHolding = false;
     * 일시정지 : mIsPlaying = false, mIsHolding = true;
     */
    private boolean mIsPlaying = false, mIsHolding = false;
    private int mTime, mCurtTime, mTotalTime;
    private String mUri;


    private MediaPlayer mPlayer = null;
    private void setPlayer(int value) {


        if(mPlayer == null) {
            mTime = 0;
            mPlayer = new MediaPlayer();
        } else if(mPlayer != null && mIsPlaying) {
            stop();
        }

        try {

            mPlayer.setDataSource(mUri);
            mPlayer.prepare();
            mTotalTime = (mPlayer.getDuration() + 500) / 1000;
            mTime = value;
            mTxtTotalTime.setText(String.format("%02d:%02d", mTotalTime / 60, mTotalTime % 60));

        } catch (Exception ex) {

            mPlayer = null;
            mIsHolding = false;
            mIsPlaying = false;

        }
    }

    //TODO: Handler -> AsyncTask
    private BackgroundAsyncTask mBgAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_playing);
        
        setTitle(R.string.audio_play_title);

        init();
    }



    private void init() {
        Intent intent = getIntent();
        //mUri = intent.getStringExtra(BasicInfo.KEY_URI_VOICE);
        mUri = "http://sites.google.com/site/ubiaccessmobile/sample_audio.amr";

        mIsPlaying = true;
        mIsHolding = false;
        mTime = mCurtTime = 0;
        
        mBtnStartNStop = (TitleBitmapButton)findViewById(R.id.btnStartNStop);
        mBtnStartNStop.setBackground(RES_PLAY_BUTTON);

        mBarProgress = (ProgressBar)findViewById(R.id.barProgress);
        mTxtPlayingTime = (TextView)findViewById(R.id.txtPlayingTime);
        mTxtTotalTime = (TextView)findViewById(R.id.txtTotalPlayingTime);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.btnStartNStop:
                        if(mPlayer != null && (mIsPlaying || mIsHolding)) {

                            if(mIsPlaying && !mIsHolding) {

                                // 재생중인 음악 일시정지
                                mCurtTime = mTime;
                                pause(mCurtTime);

                                notifyAsyncTask();

                            } else if(mIsHolding) {

                                // 일시정지한 시점부터 음악 재생
                                mTime = mCurtTime;
                                start(mTime);

                                notifyAsyncTask();
                            }

                        } else {

                            // 새로운 음악 재생
                            mTime = mCurtTime = 0;
                            start(mTime);

                            notifyAsyncTask();
                        }

                        break;

                    case R.id.btnClose:

                        closePlayer();

                        break;
                }
            }
        };
        mBtnStartNStop.setOnClickListener(OnButtonClick);
        ((TitleBitmapButton)findViewById(R.id.btnClose)).setOnClickListener(OnButtonClick);

        View.OnTouchListener OnProgressClick = new View.OnTouchListener() {
            /**
             * Progressbar 를 터치한 지점부터 시작시점 구하기 : curtOffset: 0.49327093 ==> barOffset : 49 ==> offset: 5282.2 ==> mTime: 4
             *  - curtOffset : 터치한 지점이 해당하는 0 ~ 1 사이의 바에서의 위치값
             *             final float curtOffset = curtX / (float)barW;
             *  - barOffset: 소수점이하 3번째 자리에서 절사 후 0 ~ 100 % 값으로 변환
             *             final int barOffset = new Float(curtOffset * 100).intValue();
             *  - offset : 터치한 지점이 해당하는 바의 위치 비율만큼 재생한 길이(ms)
             *             final float offset = barOffset * ((float)mPlayer.getDuration()) / 100;
             *  - mTime : 재생 시점에 해당하는 재생한 길이를 초로 변환(백의 자리에서 절사)하여 0 ~ 10까지의 값으로 변환
             *            (Progressbar 의 최대값으로 재생 최대길이를 ms 반올림(+500)하여 설정하였으므로 터치지점에 해당하는 값에 -500)
             *            mTime = ((int)offset - 500) / 1000;
             *
             * @param view
             * @param event
             * @return
             */
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int action = event.getAction();
                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        final int barW = mBarProgress.getWidth();
                        final float curtX = event.getX();
                        final float curtOffset = curtX / (float)barW;

                        if(curtOffset > 0.0f && curtOffset < 1.0f) {
                            final int barOffset = new Float(curtOffset * 100).intValue();
                            if (mPlayer != null) {
                                final float offset = barOffset * ((float) mPlayer.getDuration()) / 100;

                                mTime = ((int)offset - 500) / 1000;
                            } else {
                                mTime = barOffset / 10;
                            }
                            start(mTime);
                            notifyAsyncTask();
                            mBtnStartNStop.invalidate();
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

    private void notifyAsyncTask() {
        if(mPlayer == null) { return; }

        if(mBgAsyncTask == null) {
            mBgAsyncTask = new BackgroundAsyncTask();
            mBgAsyncTask.execute(mTotalTime);
        } else {
            synchronized (mBgAsyncTask) {
                mBgAsyncTask.notify();
            }
        }
    }

    private void start(Integer seek) {
        if(mPlayer == null) { setPlayer(seek); }

        startPlayback(seek);
        mPlayer.start();
        mBtnStartNStop.setBackground(RES_PAUSE_BUTTON);
    }

    /**
     * 재생중지 및 관련 UI 업데이트
     */
    private void stop() {
        if(mPlayer != null) {
            mPlayer.stop();
            stopPlayback();
        }
        mBtnStartNStop.setBackground(RES_PLAY_BUTTON);
    }

    private void pause(Integer seek) {
        if(mPlayer != null) {
            mPlayer.pause();
            pausePlayback();
        }
        mBtnStartNStop.setBackground(RES_PLAY_BUTTON);
    }

    /**
     * 재생 시작
     * @param uri
     */
    private void startPlayback(String uri) {
        if(uri == null || uri.equals("")) { return; }

        mUri = uri;
        startPlayback(0);
    }

    /**
     * 재생 시작
     * @param seek
     */
    private void startPlayback(Integer seek) {
        Log.d(TAG, "startPlayback seek" + seek);
        mPlayer.seekTo(seek * 1000);
        mIsPlaying = true;
        mIsHolding = false;
        mTime = seek;
        mCurtTime = 0;
    }

    /**
     * 재생 정지후 트랙의 처음으로 이동하여 처음부터 재생할 준비완료
     */
    private void stopPlayback() {
        mIsPlaying = mIsHolding = false;
        mTime = mCurtTime = 0;
        try {
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.seekTo(0);
        mPlayer.release();
    }

    /**
     * 일시정지
     */
    private void pausePlayback() {
        mIsPlaying = false;
        mIsHolding = true;
    }



    /**
     * Backspace 눌렀을때
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        closePlayer();
    }

    private void closePlayer() {
        mCurtTime = mTime;
        pause(mCurtTime);

        if(mPlayer != null) { mPlayer.stop(); mPlayer.release(); mPlayer = null; }
        finish();
    }

    /**
     * Activity 일시정지 했을 때
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");

        mCurtTime = mTime;
        pause(mTime);
        notifyAsyncTask();
    }






    /**
     * 핸들러 대신 AsyncTask 클래스를 상속하여 스레드를 위한 코드와 UI 접근 코드를 한꺼번에 처리 가능
     *  - 제네릭 인자 1 params type : doInBackground() 메서드에서 전달받는, execute() 메서드가 호출될 때 전달한 인자 타입
     *  - 제네릭 인자 2 progresses type : onProgressUpdate() 메서드에서 전달받는, publishProgress() 메서드가 호출될 때 전달한 인자 타입
     *  - 제네릭 인자 3 result type : onPostExecute() 메서드에서 전달받는, doInBackground() 메서드의 리턴 값의 타입
     */
    private class BackgroundAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        /**
         * 메인 스레드에서 실행되는 코드 (1. 백그라운드 작업 전 호출)
         *  - 초기화 작업
         */
        @Override
        protected void onPreExecute() {

            mCurtTime = mBarProgress.getProgress();
            mBarProgress.setMax(mTotalTime);
            mBarProgress.setProgress(mTime);
        }


        /**
         * 별도 생성 스레드에서 실행되어야 할 코드 (2. 백그라운드 작업)
         *  -(주의) UI 변경 처리 하면 안됨 -> publishProgress() 로 값을 전달하여 UI 변경
         * @param params : - execute() 메서드 호출시 전달한 인자
         * @return result : onPostExecute() 함수로 전달할 값.
         */
        @Override
        protected Integer doInBackground(Integer... params) {
            final int maxProgress = params[0];
            publishProgress(100, maxProgress);

            while(!isCancelled() && mIsPlaying){
                if(mTime > mBarProgress.getMax()) { publishProgress(300, 0); break; }

                publishProgress(200, ++mTime);

                delayJob();
            }

            return maxProgress;
        }

        private void delayJob(){
            delayJob(1000);
        }
        private void delayJob(int i) {
            try{
                synchronized (this) {
                    wait(i);
                }
            } catch (InterruptedException e) { /* ... */ }
        }




        /**
         * 메인 스레드에서 실행되는 코드 (3. 백그라운드 작업 진행상태에 따른 UI 반영위해 호출)
         *  - 백그라운드 작업 중간에 publishProgress() 메서드로 호출
         * @param progresses
         */
        @Override
        protected void onProgressUpdate(Integer... progresses) {

            switch (progresses[0]) {
                case 100:
                    mBarProgress.setMax(progresses[1]);
                    break;

                case 200:
                    mBarProgress.setProgress(progresses[1]);
                    updateUI();
                    break;

                case 300:
                    mBarProgress.setProgress(progresses[1]);
                    mIsPlaying = mIsHolding = false;
                    mTime = mCurtTime = 0;
                    mBtnStartNStop.setBackground(RES_PLAY_BUTTON);
                    mBtnStartNStop.postInvalidate();
                    mTxtPlayingTime.setText("00:00");
                    break;
            }
        }

        /**
         * onProgressUpdate() 에서 UI 변경시 호출
         */
        public void updateUI() {

            updateTimerView(mTxtPlayingTime);
        }

        /**
         * 시간 형식에 맞춰 TextView 에 표시
         * @param view
         */
        private void updateTimerView(TextView view) {
            view.setText(String.format("%02d:%02d", mTime / 60, mTime % 60));
        }

        /**
         * 메인 스레드에서 실행되는 코드 (4-A. 백그라운드 작업 종료후 호출)
         *  - 메모리 리소스 해제 등 작업
         *  - 메인 스레드에서 execute() -> ... -> onPostExecute()
         * @param result : 백그라운드 작업의 결과. doInBackground() 메서드로부터 전달받은 값
         */
        @Override
        protected void onPostExecute(Integer result) {

            mBgAsyncTask = null;
        }

        /**
         * 메인 스레드에서 실행되는 코드 (4-B. 백그라운드 작업 종료후 호출)
         *  - 메모리 리소스 해제 등 작업
         *  - 메인 스레드에서 bgAskTask.cancel(true) -> ... -> onCancelled()
         */
        @Override
        protected void onCancelled() {
            mBarProgress.setProgress(0);
            mBgAsyncTask = null;
        }
    }
}

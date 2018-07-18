package org.hyg.seraph0.multinotepad;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;

/**
 * Created by shiny on 2018-04-11.
 */

public class VoiceRecorder implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private final String TAG = "[MEMO-VOICE-RCD]";

    private static final String SAMPLE_PREFIX = "recording";
    private static final String SAMPLE_PATH_KEY = "sample_path";
    private static final String SAMPLE_LENGTH_KEY = "sample_length";
    private static final String TEMP_STORAGE = "/data/data/org.hyg.seraph0.multinotepad/voice";

    public static final int IDE_STATE = 0, RECORDING_STATE = 1, PLAYING_STATE = 2;
    private int mState = IDE_STATE;
    public int getState() { return mState; }
    private void setState(int value) {
        if(value == mState) { return; }

        mState = value;
        setStateChanged(value);
    }

    private void setStateChanged(int value) {
        if(mOnStateChangedListener != null) {
            mOnStateChangedListener.onStateChanged(value);
        }
    }

    public static final int NO_ERROR = 0, SDCARD_ACCESS_ERROR = 1, INTERNAL_ERROR = 2, IN_CALL_RECORD_ERROR = 3;
    private void setError(int value) {
        if(mOnStateChangedListener != null) {
            mOnStateChangedListener.onError(value);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        startPlayback();
    }

    /**
     * 레코더를 사용하는 클래스에서 상태변경이나 에러발생시 구현할 이벤트 리스너 인터페이스
     */
    public interface OnStateChangedListener {
        public void onStateChanged(int state);
        public void onError(int error);
    }
    private OnStateChangedListener mOnStateChangedListener = null;
    public void setOnStateChangedListener(OnStateChangedListener value) {
        mOnStateChangedListener = value;
    }

    private long mSampleStartTime = 0;
    public int getProgress() {
        if(mState == RECORDING_STATE || mState == PLAYING_STATE) {
            return (int)((System.currentTimeMillis()- mSampleStartTime) / 1000 );
        }

        return 0;
    }
    private int mSampleLength = 0;
    public int getSampleLength() { return mSampleLength; }
    private File mSampleFile = null;
    public File getSampleFile() { return mSampleFile; }
    private void setSampleFile(String value){
        File folder = new File(BasicInfo.FOLDER_VOICE);
        if(!folder.canWrite()) { folder = new File(TEMP_STORAGE); }
        if(!folder.isDirectory()) { folder.mkdirs(); }

        try{
            mSampleFile = File.createTempFile(SAMPLE_PREFIX, value, folder);
        } catch (IOException e) {
            setError(SDCARD_ACCESS_ERROR);
            mSampleFile = null;
        }
    }

    private MediaRecorder mRecorder = null;
    private void setRecorder(int value) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(value);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(mSampleFile.getAbsolutePath());

        try{
            mRecorder.prepare();
        } catch (IOException e) {
            setError(INTERNAL_ERROR);
            resetRecorder();
        }
    }

    private void resetRecorder() {
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
    }

    /**
     * MediaPlayer 가 안전하게 작동되도록 OnPrepareListener 를 상속받아 구현하고
     * prepare() -> prepareAsync() 로 변경
     */
    private MediaPlayer mPlayer = null;
    private void setPlayer(File value){
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(value.getAbsolutePath());
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);
            mPlayer.setOnPreparedListener(this);
            mPlayer.prepareAsync();
        } catch (IllegalArgumentException ex) {
            setError(INTERNAL_ERROR);
            mPlayer = null;
        } catch (IOException e) {
            setError(SDCARD_ACCESS_ERROR);
            mPlayer = null;
        }
    }

    public VoiceRecorder()  {}


    /**
     * 녹음상태 저장
     * @param recorderState
     */
    public void saveState(Bundle recorderState) {
        recorderState.putString(SAMPLE_PATH_KEY, mSampleFile.getAbsolutePath());
        recorderState.putInt(SAMPLE_LENGTH_KEY, mSampleLength);
    }

    /**
     * 녹음작업 초기화
     * @param recorderState
     */
    public void restoreState(Bundle recorderState) {
        final String path = recorderState.getString(SAMPLE_PATH_KEY);
        if(path == null) { return; }

        final int length = recorderState.getInt(SAMPLE_LENGTH_KEY, -1);
        if(length == -1) { return; }

        File file = new File(path);
        if(!file.exists()) { return; }

        if(mSampleFile != null
                && mSampleFile.getAbsolutePath().compareTo(file.getAbsolutePath()) == 0) { return; }

        delete();

        mSampleFile = file;
        mSampleLength = length;

        setStateChanged(IDE_STATE);
    }

    /**
     * 녹음을 중지하고 녹음파일 초기화(녹음파일 삭제)
     */
    private void delete() {
        stop();

        if(mSampleFile != null) { mSampleFile.delete(); }

        mSampleFile = null;
        mSampleLength = 0;

        setStateChanged(IDE_STATE);
    }

    /**
     * 녹음을 중지하고 녹음파일 초기화(녹음파일 재사용)
     */
    private void clear() {
        stop();

        mSampleLength = 0;

        setStateChanged(IDE_STATE);
    }

    public void startRecording(int format, String extension, Context context) {
        stop();

        setSampleFile(extension);
        if(mSampleFile == null) { return; }

        setRecorder(format);
        if(mRecorder == null) { return; }

        try{
            mRecorder.start();
        } catch (RuntimeException ex) {
            resetRecorder();
            return;
        }

        mSampleLength = (int)((System.currentTimeMillis() - mSampleStartTime) / 1000);
        setState(RECORDING_STATE);
    }

    public void startPlayback() {
        stop();

        setPlayer(mSampleFile);
        if(mPlayer == null) { return; }

        try {
            mPlayer.start();
        } catch (Exception ex) {
            setError(INTERNAL_ERROR);
            return;
        }

        mSampleStartTime = System.currentTimeMillis();
        setState(PLAYING_STATE);
    }

    /**
     * 레코드의 최대진폭
     *
     *  - AudioSource.MIC 일때 소리가 민감하게 반응할 경우,
     *    AudioSource.VOICE_RECOGNITION 으로 설정하면
     *    단말의 mic 부분에 직접 소리가 들어오는 경우를 제외하면 나머지는 작은 값 반환함
     * @return
     */
    public int getMaxAmplitude() {
        if(mState != RECORDING_STATE) { return 0; }

        return mRecorder.getMaxAmplitude();
    }

    /**
     *
     * @param mediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stop();
    }

    /**
     * 녹음 중지
     */
    public void stop() {
        stopRecording();
        stopPlayback();
    }

    /**
     * 녹음을 중지하고 초기화
     */
    private void stopRecording() {
        if(mRecorder == null) { return; }

        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        mSampleLength = (int)((System.currentTimeMillis() - mSampleStartTime) / 1000);
        setState(IDE_STATE);
    }

    /**
     * 플레이어를 종료하고 초기화
     */
    private void stopPlayback() {
        if(mPlayer == null) { return; }

        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;

        setState(IDE_STATE);
    }

    /**
     *
     * @param mediaPlayer : 오류가 발생한 MediaPlayer
     * @param what : 발생한 오류 유형
     *        - 1: MEDIA_ERROR_UNKNOWN
     *        - 100: MEDIA_ERROR_SERVER_DIED
     * @param extra : 오류 관련 추가 코드
     *        - (-1004) : MEDIA_ERROR_IO
     *        - (-1007) : MEDIA_ERROR_MALFORMED
     *        - (-1010) : MEDIA_ERROR_UNSUPPORTED
     *        - (-110) : MEDIA_ERROR_TIMEOUT
     *        - MEDIA_ERROR_SYSTEM : 하위 수준 시스템 오류
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        try{
            stop();
        } catch (Exception ex) { ex.printStackTrace(); }

        return false;
    }


}

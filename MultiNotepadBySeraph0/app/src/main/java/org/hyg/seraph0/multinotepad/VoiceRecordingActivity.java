package org.hyg.seraph0.multinotepad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.hyg.seraph0.multinotepad.common.TitleBitmapButton;
import org.w3c.dom.Text;

import java.io.File;

/**
 * 음성 녹음
 */
public class VoiceRecordingActivity extends AppCompatActivity implements VoiceRecorder.OnStateChangedListener {

    private static final String TAG = "[MEMO-VOICE-REC]";
    private RemainingTimeCalculator mRemainingTimeCalculator;

    private static final int WARNING_INSERT_SDCARD = 1001;
    private static final int WARNING_DISK_SPACE_FULL = 1002;

    private static final int RECORDING_START = 1013;
    private static final int RECORDING_IDLE = 0;
    private static final int RECORDING_RUNNING = 1;

    private static final String AUDIO_3GPP = "audio/3gpp";
    private static final String AUDIO_AMR = "audio/amr";
    private static final String AUDIO_ANY = "audio/*";
    private static final String ANY_ANY = "*/*";
    private String mRequestedType = AUDIO_AMR;

    private static final int BITRATE_AMR = 5900;
    private static final int BITRATE_3GPP = 5900;

    private static final String STATE_FILE_NAME = "sound_recorder.state";
    private static final String RECORDER_STATE_KEY = "recorder_state";
    private static final String SAMPLE_INTERRUPTED_KEY = "sample_interrupted";
    private static final String MAX_FILE_SIZE_KEY = "max_file_size";

    private VoiceRecorder mRecorder;
    private TextView mTxtRecordingTime;
    private TitleBitmapButton mBtnStartNStop, mBtnCancel;

    private boolean mIsRecording;
    private boolean mIsInterrupted = false;
    private long mMaxFileSize = -1;
    private PowerManager.WakeLock mWakeLock;
    private long mRecordingTime;
    private String mTimerFormat;
    private int mRecordingState = RECORDING_IDLE;

    private Handler mHandler = new Handler();
    Runnable mUpdateTimer = new Runnable() {
        @Override
        public void run() {
            updateTimerView(mTxtRecordingTime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_voice_recording);

        init();
        init(savedInstanceState);
    }


    private void init() {
        mIsRecording = false;

        mTxtRecordingTime = (TextView)findViewById(R.id.txtRecordingTime);
        mTxtRecordingTime.setText("00:00");

        mBtnStartNStop = (TitleBitmapButton)findViewById(R.id.btnStartNStop);
        mBtnStartNStop.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.btn_voice_record, 0, 0);

        mBtnCancel = (TitleBitmapButton)findViewById(R.id.btnCancel);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btnStartNStop:
                        if(mIsRecording) {
                            stopRecording();
                        } else {
                            startRecording();
                        }

                        mIsRecording = !mIsRecording;

                        break;

                    case R.id.btnCancel:
                        if(mIsRecording) { mRecorder.stop(); }
                        finish();

                        break;
                }
            }
        };
        mBtnStartNStop.setOnClickListener(OnButtonClick);
        mBtnCancel.setOnClickListener(OnButtonClick);
    }

    private void init(Bundle savedInstanceState) {
        mRecorder = new VoiceRecorder();
        mRecorder.setOnStateChangedListener(this);
        mRemainingTimeCalculator = new RemainingTimeCalculator();

        PowerManager manager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "mediaGalaxy");

        if(savedInstanceState != null) {
            Bundle recorderState = savedInstanceState.getBundle(RECORDER_STATE_KEY);
            if(recorderState != null) {
                mRecorder.restoreState(recorderState);
                mIsInterrupted = recorderState.getBoolean(SAMPLE_INTERRUPTED_KEY, false);
                mMaxFileSize = recorderState.getLong(MAX_FILE_SIZE_KEY, -1);
            }
        }

        mTimerFormat = "%02d:%02d";
        updateUI(mTxtRecordingTime);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        updateUI(mTxtRecordingTime);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mRecorder.getSampleLength() == 0) { return; }

        Bundle recorderState = new Bundle();
        mRecorder.saveState(recorderState);
        recorderState.putBoolean(SAMPLE_INTERRUPTED_KEY, mIsInterrupted);
        recorderState.putLong(MAX_FILE_SIZE_KEY, mMaxFileSize);

        outState.putBundle(RECORDER_STATE_KEY, recorderState);
    }

    @Override
    public void onStateChanged(int state) {
        if(state == VoiceRecorder.PLAYING_STATE || state == VoiceRecorder.RECORDING_STATE) {
            mIsInterrupted = false;
        }

        if(state ==  VoiceRecorder.RECORDING_STATE) {
            mWakeLock.acquire();
        } else {
            if(mWakeLock.isHeld()) { mWakeLock.release(); }
        }

        updateUI(mTxtRecordingTime);
    }

    @Override
    public void onError(int error) {
        showContentDialog(error);
    }


    /**
     * showDialog() 대체
     * @param dialogId
     */
    private void showContentDialog(int dialogId) {
        AlertDialog.Builder builder = null;

        switch (dialogId) {
            case VoiceRecorder.SDCARD_ACCESS_ERROR:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.no_sdcard_message);
                break;

            case VoiceRecorder.IN_CALL_RECORD_ERROR:
            case VoiceRecorder.INTERNAL_ERROR:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.error_internal_message);
                break;

            case WARNING_INSERT_SDCARD:
                break;

            case WARNING_DISK_SPACE_FULL:
                break;

            default:
                break;
        }

        if(builder != null) {
            builder.setPositiveButton(R.string.confirm_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            builder.setCancelable(false);

            builder.show();
        }
    }

    public void updateUI(View view) {
        switch(view.getId()) {
            case R.id.txtRecordingTime:
                updateTimerView((TextView)view);
                break;
        }
    }

    private void updateTimerView(TextView txtView) {
        Resources res = getResources();
        int state = mRecorder.getState();
        boolean ongoing = (state == VoiceRecorder.RECORDING_STATE || state == VoiceRecorder.PLAYING_STATE);
        mRecordingTime = (ongoing) ? mRecorder.getProgress() : mRecorder.getSampleLength();;

        txtView.setText(String.format(mTimerFormat, mRecordingTime / 60, mRecordingTime % 60));

        if(state != VoiceRecorder.PLAYING_STATE) {
            updateRemainingTime();
        }

        if(ongoing) { mHandler.postDelayed(mUpdateTimer, 1000); }
    }

    private void updateRemainingTime() {
        long time = mRemainingTimeCalculator.getRemainingTime();

        if(time <= 0) {
            mIsInterrupted = true;

            int limit = mRemainingTimeCalculator.getCurrentLowerLimit();
            switch (limit) {
                case RemainingTimeCalculator.DISK_SPACE_LIMIT:
                    break;

                case RemainingTimeCalculator.FILE_SIZE_LIMIT:
                    break;

                default:
                    break;
            }

            mRecorder.stop();
            return;
        } else if(time < 60) {
            Log.d(TAG, String.format("%d min available", time));
        } else if(time < 540) {
            Log.d(TAG, String.format("%d s available", time / 60 + 1));
        }
    }

    private void startRecording() {
        mRemainingTimeCalculator.reset();

        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            showContentDialog(WARNING_INSERT_SDCARD);
            startRecording(true);
        } else if (!mRemainingTimeCalculator.isAvailableDiskSpace()) {
            mIsInterrupted = true;
            showContentDialog(WARNING_DISK_SPACE_FULL);
        } else {
            startRecording(false);
        }

        mRecordingState = RECORDING_RUNNING;

        mTxtRecordingTime.setText("00:00");

        mBtnStartNStop.setText(R.string.audio_recording_start_title);
        mBtnStartNStop.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.btn_voice_stop, 0, 0);
    }

    private void startRecording(boolean isInterrupted) {
        if(isInterrupted) { mIsInterrupted = isInterrupted; }

        stopAudioPlayback();
        if(AUDIO_AMR.equals(mRequestedType)) {
            mRemainingTimeCalculator.setBitRate(BITRATE_AMR);
            mRecorder.startRecording(MediaRecorder.OutputFormat.AMR_NB, ".amr", this);
        } else if(AUDIO_3GPP.equals(BITRATE_3GPP)) {
            mRemainingTimeCalculator.setBitRate(BITRATE_3GPP);
            mRecorder.startRecording(MediaRecorder.OutputFormat.THREE_GPP, ".3gpp", this);
        } else {
            throw new IllegalArgumentException("Invalid output file type requested.");
        }

        if(mMaxFileSize != -1) {
            mRemainingTimeCalculator.setRecordingFile(mRecorder.getSampleFile(), mMaxFileSize);
        }
    }

    private void stopRecording() {
        mRecorder.stop();

        mBtnStartNStop.setText(R.string.audio_recording_start_title);
        mBtnStartNStop.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.btn_voice_record, 0, 0);

        File file = mRecorder.getSampleFile();
        saveRecording(file);

        mRecordingState = RECORDING_IDLE;
    }

    private void saveRecording(File file) {
        checkFolder();

        try{
            file.renameTo(new File(BasicInfo.FOLDER_VOICE + BasicInfo.FILENAME_RECORDED));
            setResult(RESULT_OK);
        } catch (Exception ex) { Log.e(TAG, "Exception in storing recording.", ex); }

        if(file != null) {
            Log.d(TAG, "Recording file saved to " + file.getName());
        }
    }

    private void stopAudioPlayback() {
        Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "pause");

        sendBroadcast(intent);
    }

    private void checkFolder() {
        File folder = new File(BasicInfo.FOLDER_VOICE);
        if(!folder.isDirectory()) { folder.mkdirs(); }
    }

    /***************************************************************************************
     *
     */
    class RemainingTimeCalculator {
        public static final int UNKNOWN_LIMIT = 0;
        public static final int FILE_SIZE_LIMIT = 1;
        public static final int DISK_SPACE_LIMIT = 2;
        private int m_curtLowerLimit = UNKNOWN_LIMIT;
        public int getCurrentLowerLimit() { return m_curtLowerLimit; }

        private File m_sdCardDirectory;

        private File m_recordingFile;
        private void setRecordingFile(File value) { m_recordingFile = value; }
        private long m_maxBytes;
        private void setMaxBytes(long value) { m_maxBytes = value; }
        private int m_bytesPerSecond = 5900 / 8;
        public void setBitRate(int value) { m_bytesPerSecond = value / 8; }
        private long m_blocksChangedTime;
        private long m_lastBlocks;
        private long m_fileSizeChangedTime;
        private long m_lastFileSize;

        public RemainingTimeCalculator() {
            m_sdCardDirectory = Environment.getExternalStorageDirectory();
        }

        public void setRecordingFile(File recordingFile, long maxBytes) {
            setRecordingFile(recordingFile);
            setMaxBytes(maxBytes);
        }

        public long getRemainingTime() {
            StatFs fs = new StatFs(m_sdCardDirectory.getAbsolutePath());
            long blocks = fs.getAvailableBlocksLong();
            long blockSize = fs.getBlockSizeLong();
            long now = System.currentTimeMillis();

            if(m_blocksChangedTime == -1 || blocks != m_lastBlocks) {
                m_blocksChangedTime = now;
                m_lastBlocks = blocks;
            }

            long resultBlock = m_lastBlocks * blockSize / m_bytesPerSecond;
            resultBlock -= (now - m_blocksChangedTime) / 1000;

            if(m_recordingFile == null) {
                m_curtLowerLimit = DISK_SPACE_LIMIT;
                return resultBlock;
            }

            m_recordingFile = new File(m_recordingFile.getAbsolutePath());
            long fileSize = m_recordingFile.length();
            if(m_fileSizeChangedTime == -1 || fileSize != m_lastFileSize) {
                m_fileSizeChangedTime = now;
                m_lastFileSize = fileSize;
            }

            long resultFile = (m_maxBytes - fileSize) / m_bytesPerSecond;
            resultFile -= (now - m_fileSizeChangedTime) / 1000;
            resultFile -= 1;

            m_curtLowerLimit = (resultBlock < resultFile) ? DISK_SPACE_LIMIT : FILE_SIZE_LIMIT;

            return Math.min(resultBlock, resultFile);
        }

        /**
         * 파일 시스템에서 비어있고 응용프로그램에서 사용할 수 있는 블록 수
         * @return
         */
        public boolean isAvailableDiskSpace() {
            StatFs fs = new StatFs(m_sdCardDirectory.getAbsolutePath());

            return fs.getAvailableBlocks() > 1;
        }


        public void reset() {
            m_curtLowerLimit = UNKNOWN_LIMIT;
            m_blocksChangedTime = -1;
            m_fileSizeChangedTime = -1;
        }



    }
}

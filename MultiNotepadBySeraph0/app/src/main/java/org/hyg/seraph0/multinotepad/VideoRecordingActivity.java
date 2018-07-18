package org.hyg.seraph0.multinotepad;


import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.hyg.seraph0.multinotepad.common.TitleBitmapButton;

import java.io.File;
import java.io.IOException;


/**
 * 2018.04.26.
 * 동영상 녹화
 *  - invalid surface view 에러 메시지 => xml 에서 FrameLayout 의 layout_weight='1'을 누락하여 발생(해결)
 *  - Start 버튼을 누르면 녹화 시작 -> Stop 버튼을 누르면 녹화 종료 -> [Back] 버튼 터치하면 녹화 화면 종료
 */
public class VideoRecordingActivity extends AppCompatActivity {

    private final String TAG = "[MEMO-VDO-REC]";

    private TitleBitmapButton mBtnStartNStop;
    private boolean mIsStarted = false;
    private FrameLayout mLatFrame;

    private MediaRecorder mRecorder;
    private CameraSurfaceView mSurfaceView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_video_recording);

        init();
    }

    private void initWindowLandscape() {
        final Window win = getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void init() {
        mLatFrame = (FrameLayout)findViewById(R.id.latVideo);
        mSurfaceView = new CameraSurfaceView(getApplicationContext());
        mLatFrame.addView(mSurfaceView);

        mBtnStartNStop = (TitleBitmapButton)findViewById(R.id.btnStartNStop);
        mBtnStartNStop.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.btn_voice_record, 0, 0);
        mBtnStartNStop.setText(R.string.video_recording_start_title);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.btnStartNStop:

                        if(mIsStarted) {

                            mRecorder.stop();
                            releaseRecorder();

                            mBtnStartNStop.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.btn_voice_record, 0, 0);
                            mBtnStartNStop.setText(R.string.video_recording_start_title);
                            mIsStarted = false;
                            setResult(RESULT_OK);

                        } else {

                            mSurfaceView.stopPreview();
                            if(prepareVideoRecorder()) {
                                mRecorder.start();
                                mBtnStartNStop.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.btn_voice_stop, 0, 0);
                                mBtnStartNStop.setText(R.string.video_recording_stop_title);
                                mIsStarted = true;
                            } else {
                                releaseRecorder();
                            }
                        }

                        break;

                    default:
                        break;
                }
            }
        };
        mBtnStartNStop.setOnClickListener(OnButtonClick);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean prepareVideoRecorder() {
        checkFolder(BasicInfo.FOLDER_VIDEO);

        if(mRecorder == null) {
            mRecorder = new MediaRecorder();
        } else  {
            mRecorder.reset();
        }

        //Step1: unlock and set camera to MediaRecorder

        //Step2: set sources
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        //Step3: set a CamcorderProfile
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mRecorder.setProfile((CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH)));
            mRecorder.setVideoFrameRate(30);

            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.FROYO) {
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            }
        }

        //Step4: set output file
        mRecorder.setOutputFile(BasicInfo.FOLDER_VIDEO + BasicInfo.FILENAME_RECORDED);

        //Step5: set the preview output
        mRecorder.setPreviewDisplay(mSurfaceView.getSurface());

        try{
            mRecorder.prepare();
            return true;
        } catch (IllegalStateException ex) {
            releaseRecorder();
            Log.d(TAG, " >>> prepareVideoRecorder() - IllegalState-EX: " + ex.getMessage());
        } catch (IOException ex) {
            releaseRecorder();
            Log.d(TAG, " >>> prepareVideoRecorder() - IO-EX: " + ex.getMessage());
        } catch (Exception ex) {
            releaseRecorder();
            Log.d(TAG, " >>> prepareVideoRecorder() - EX" + ex.getMessage());
        }

        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(mIsStarted) {
                Toast.makeText(getApplicationContext(), R.string.video_recording_message, Toast.LENGTH_LONG).show();
            } else {
                releaseRecorder();
                finish();
            }
            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseRecorder();
    }



    private void checkFolder(String folder) {
        File dir = new File(folder);
        if(!dir.isDirectory()) { dir.mkdirs(); }
    }



    /**
     * 카메라 사용가능 여부
     * @param context
     * @return
     */
    private boolean hasCameraHardware(Context context) {
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public void releaseRecorder() {
        if (mRecorder == null) { return; }
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
    }



}

package org.hyg.seraph0.multinotepad;


import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
 * 2018.04.26
 * 동영상 녹화
 * 카메라 lock/unlock 을 이용한 비디오 레코딩 소스
 */
public class VDORecordingActivity extends AppCompatActivity {

    private final String TAG = "[MEMO-VDO-REC]";

    private TitleBitmapButton mBtnStartNStop;
    private boolean mIsStarted = false;
    private FrameLayout mLatFrame;

    private MediaRecorder mRecorder;
    private VideoSurfaceView mSurfaceView;
    private Camera mCamera;
    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception ex) { }

        if(camera == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            for(int i=0; i< Camera.getNumberOfCameras(); i++) {
                try{
                    camera = Camera.open(i);
                    break;
                } catch (RuntimeException ex) { }
            }
        }

        return camera;
    }




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
        mCamera = getCameraInstance();
        mSurfaceView = new VideoSurfaceView(getApplicationContext(), mCamera);
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
                            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                                mCamera.lock();
                            }

                            setResult(RESULT_OK);
                            mBtnStartNStop.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.btn_voice_record, 0, 0);
                            mBtnStartNStop.setText(R.string.video_recording_start_title);
                            mIsStarted = false;

                        } else {

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
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mCamera.unlock();
            mRecorder.setCamera(mCamera);
        }


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
                finish();
            }

            return true;
        }

        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseRecorder();
        releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        releaseRecorder();
    }



    private void checkFolder(String folder) {
        File dir = new File(folder);
        if(!dir.isDirectory() || !dir.exists()) { dir.mkdirs(); }
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
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mCamera.lock();
        }
    }

    private void releaseCamera() {
        if(mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


}

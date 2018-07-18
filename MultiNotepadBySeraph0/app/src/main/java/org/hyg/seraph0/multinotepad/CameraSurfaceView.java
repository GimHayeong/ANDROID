package org.hyg.seraph0.multinotepad;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.IOException;

/**
 * Created by shiny on 2018-04-10.
 * 메인 스레드가 Canvas 대신 사진을 그릴 SurfaceView(가상 메모리 화면 뷰) 구현
 * 메인 스레드가 Surface 변화를 감지해 Thread 로 그리기 허용 여부를 전달할 SurfaceHolder.Callback 인터페이스 구현
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "[MEMO-SURFACE]";

    private Context mContext;
    private SurfaceHolder mHolder = null;
    private Camera mCamera = null;


    private MediaRecorder mRecorder = null;
    private void setRecorder(String value) {
        if(mRecorder == null) {
            mRecorder = new MediaRecorder();
        } else  {
            mRecorder.reset();
        }

        mRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        mRecorder.setVideoFrameRate(30);
        mRecorder.setOutputFile(value);
        mRecorder.setPreviewDisplay(getSurface());
        if(mRecorder != null) {
            try {
                // TODO: MediaRecorderJNI: Application lost the surface
                // java.io.IOException: invalid preview surface at android.media.MediaRecorder._prepare(Native Method)
                mRecorder.prepare();
            } catch (IllegalStateException ex) {
                Log.e(TAG, "setRecorder() - IllegalState-EX: ", ex);
            } catch (IOException ex) {
                Log.e(TAG, "setRecorder() - IO-EX: ", ex);
            } catch (Exception ex) {
                Log.e(TAG, "setRecorder() - EX", ex);
            }
        }
    }

    public Surface getSurface() { return mHolder.getSurface(); }


    public CameraSurfaceView(Context context) {
        super(context);

        mContext = context;

        init();
    }

    private void init() {

        mHolder = getHolder();
        mHolder.addCallback(this);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

    }


    /**
     * Surface 가 처음 생성된 후 호출. (그리기 권한 허용됨)
     * @param holder : 시스템 표면의 변화가 생길 때마다 호출되는 콜백메서드
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        openCamera();
    }

    /**
     *
     */
    private void openCamera() {
        mCamera = Camera.open();
        try{
            mCamera.setPreviewDisplay(mHolder);

        } catch (Exception ex) { Log.e(TAG, "openCamera()", ex); }
    }

    /**
     * Surface 의 색상이나 포맷이 변경되었을 때.
     * @param holder : 시스템 표면의 변화가 생길 때마다 호출되는 콜백메서드
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        mCamera.startPreview();
    }


    /**
     * Surface 파괴 직전 호출됨. (그리기 권한 취소됨)
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreview();
    }

    public void stopPreview() {
        if(mCamera == null) { return; }

        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    /**
     * 사진찍기 앱 실행
     * @param jpegHandler : 사진찍은 후 호출되는 콜백메서드
     * @return
     */
    public boolean capture(Camera.PictureCallback jpegHandler) {
        if(mCamera != null) {
            mCamera.takePicture(null, null, jpegHandler);
            return true;
        }

        return false;
    }


    //TODO:
    public boolean startRecording() {
        boolean isStarted = false;
        checkFolder();
        stopPreview();

        String uri = BasicInfo.FOLDER_VIDEO + BasicInfo.FILENAME_RECORDED;

        setRecorder(uri);

        try {

            //TODO: MediaRecorder: start called in an invalid state: 4
            // java.lang.IllegalStateException at android.media.MediaRecorder.start(Native Method)
            mRecorder.start();
            isStarted = true;
        } catch (Exception ex) {
            Log.e(TAG, "startRecording()", ex);
        }

        return isStarted;
    }

    public boolean stopRecording() {
        boolean isStarted = true;

        mRecorder.stop();
        isStarted = false;
        initRecorder();

        return isStarted;
    }

    public void initRecorder() {
        if (mRecorder == null) { return; }
        mRecorder.release();
        mRecorder = null;
    }

    private void checkFolder() {
        File folder = new File(BasicInfo.FOLDER_VIDEO);
        if(!folder.isDirectory()) { folder.mkdirs(); }
    }

    /**
     * 유효한(존재하는) 파일 여부
     * @param filename
     * @return
     */
    private boolean IsValidFile(String filename) {
        File file = new File(filename);
        return file.exists();
    }

}

package org.hyg.seraph0.media.capture.intent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by shiny on 2018-04-05.
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Context mContext;
    private SurfaceHolder mHolder;

    private CameraDevice mCameraDevice;
    private Camera mCamera;
    private MediaRecorder mRecorder;

    private boolean mIsRecoding = false;


    /**
     * SurfaceView Constructor
     * @param context
     * @param attrs
     */
    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        initSurfaceHolder();

        initCamera();
    }

    /**
     *  - 1. SurfaceHolder 생성
     *  - 2. Callback 함수 등록
     *  - 3. 쓰레드 생성
     */
    private void initSurfaceHolder() {

        mHolder = getHolder();
        mHolder.addCallback(this);

    }

    private void initCamera() {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /**
     * SurfaceView 가 만들어질 때 호출됨
     *  - 스레드 시작
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        try {
            setCamera();
        } catch (IOException ex) { }


    }

    private void setCamera() throws IOException {
        if(mCamera == null) {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }
    }

    /**
     * SurfaceView 의 크기가 바뀔 때 호출됨
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        resetCamera();
    }

    /**
     *  - View 가 존재하지 않으면 리턴
     *  - 뷰를 중지하고 잠시 대기
     *  - 카메라 재설정
     *  - 뷰를 재생성
     */
    private void resetCamera() {
        if(mHolder.getSurface() == null) { return; }

        try {
            mCamera.stopPreview();
        } catch (Exception ex) { }

        Camera.Parameters params = mCamera.getParameters();
        List<String> focusModes = params.getSupportedFocusModes();
        if(focusModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
            params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        }
        mCamera.setParameters(params);

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception ex) { }
    }

    /**
     * SurfaceView 가 종료될 때 호출됨
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        destroyCamera();
    }

    private void destroyCamera() {
        if(mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

}

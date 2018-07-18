package org.hyg.audioplayerbyseraph0;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by shiny on 2018-03-30.
 * SurfaceView 상속 및 Callback 인터페이스 구현 클래스
 * 카메라(사진) 미리보기
 *
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera = null;

    public CameraSurfaceView(Context context) {
        super(context);

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    /**
     * 사진찍기
     *  - takePicture() 메서드를 호출하여 사진촬영
     * @param pictureCallback
     * @return
     */
    public boolean capture(Camera.PictureCallback pictureCallback) {
        if(mCamera == null) { return false; }

        mCamera.takePicture(null, null, (Camera.PictureCallback) mHolder);
        return true;
    }

    /**
     *  - 서피스뷰가 만들어질때 카메라 객체 참조
     *  - 미리보기 화면으로 홀더 객체 설정
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mCamera = Camera.open();

        try{
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) { Log.e("CameraSurfaceView", "failed to set camera preview.", e); }
    }

    /**
     * 화면 크기가 바뀌는 시점에 미리보기 시작
     * @param surfaceHolder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        mCamera.startPreview();
    }

    /**
     * 서피스뷰가 사라질때 미리보기 중지
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.startPreview();
        mCamera.release();
        mCamera = null;
    }
}

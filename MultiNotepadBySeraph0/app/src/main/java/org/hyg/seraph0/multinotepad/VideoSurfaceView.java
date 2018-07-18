package org.hyg.seraph0.multinotepad;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.hyg.seraph0.multinotepad.db.MediaRecord;

import java.io.File;
import java.io.IOException;

/**
 * Created by shiny on 2018-04-10.
 * 메인 스레드가 Canvas 대신 사진을 그릴 SurfaceView(가상 메모리 화면 뷰) 구현
 * 메인 스레드가 Surface 변화를 감지해 Thread 로 그리기 허용 여부를 전달할 SurfaceHolder.Callback 인터페이스 구현
 */

public class VideoSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "[MEMO-SURFACE]";

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public Surface getSurface() { return mHolder.getSurface(); }

    public VideoSurfaceView(Context context, Camera camera) {
        super(context);

        init(context, camera);
    }

    private void init(Context context, Camera camera) {

        mCamera = camera;

        mHolder = getHolder();
        mHolder.addCallback(this);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        setFocusable(true);

        try{
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "failed setting camera preview: " + e.getMessage());
        }
    }




    /**
     * Surface 가 처음 생성된 후 호출. (그리기 권한 허용됨)
     * @param holder : 시스템 표면의 변화가 생길 때마다 호출되는 콜백메서드
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, " ===== >>>>> surfaceCreated() <<<<< =======" + (holder.getSurface() != null ? "not null" : "null"));
        Log.d(TAG, " ===== >>>>> surfaceCreated() <<<<< =======" + (holder.getSurface() != null && holder.getSurface().isValid() ? "valid" : "invalid"));

        mHolder = holder;

        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "failed setting camera preview: " + e.getMessage());
        }

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

        if(mHolder.getSurface() == null) { return; }

        try{
            mCamera.stopPreview();
        } catch (Exception ex) { }

        try{
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception ex) { Log.d(TAG, "failed starting camera preview: " + ex.getMessage()); }

    }


    /**
     * Surface 파괴 직전 호출됨. (Activity 에서 호출)
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //nothing ...
    }


}

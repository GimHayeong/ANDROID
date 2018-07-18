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
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by shiny on 2018-04-05.
 */

public class CaptureSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private Context mContext;
    private SurfaceHolder mHolder;
    private Thread mThread;




    /**
     * SurfaceView Constructor
     * @param context
     * @param attrs
     */
    public CaptureSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        initSurfaceHolder();


    }

    /**
     *  - 1. SurfaceHolder 생성
     *  - 2. Callback 함수 등록
     *  - 3. 쓰레드 생성
     */
    private void initSurfaceHolder() {

        mHolder = getHolder();
        mHolder.addCallback(this);

        mThread = new CaptureSurfaceThread();

    }



    /**
     * SurfaceView 가 만들어질 때 호출됨
     *  - 스레드 시작
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mThread.start();

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

    }


    /**
     * SurfaceView 가 종료될 때 호출됨
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //destroyCamera();
    }




    /**********************************************************************************************
     *
     *********************************************************************************************/
    class CaptureSurfaceThread extends Thread {

        private Bitmap m_bitmap;
        private boolean m_isRun = true;


        /**
         * Thread Constructor
         */
        public CaptureSurfaceThread() {
            displayBitmap();
        }

        /**
         *
         */
        private void displayBitmap() {

            m_bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher_background);

        }

        /**
         * 화면 크기에 맞추기
         */
        private void displayScaledBitmap() {
            Display defaultDisplay = ((WindowManager)mContext.getSystemService(mContext.WINDOW_SERVICE)).getDefaultDisplay();

            int width = defaultDisplay.getWidth();
            int height = defaultDisplay.getHeight();
            m_bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher_background);
            m_bitmap = Bitmap.createScaledBitmap(m_bitmap, width, height, true);
        }


        /**
         * 스레드에서 반복 실행할 부분
         */
        @Override
        public void run() {
            Canvas canvas = null;
            while(m_isRun) {
                canvas = mHolder.lockCanvas();
                try {
                    synchronized (mHolder) {
                        canvas.drawBitmap(m_bitmap, 0, 0, null);
                    }
                } finally {
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }


}

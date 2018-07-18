package org.hyg.seraph0.multinotepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import org.hyg.seraph0.multinotepad.common.TitleBitmapButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 사진 찍기 / 삭제
 */
public class PhotoCaptureActivity extends AppCompatActivity {
    private static final String TAG = "[MEMO-CAPTURE]";
    /**
     * Canvas 대신 사진을 그릴 Surface (가상 메모리)
     */
    CameraSurfaceView mCamera;
    FrameLayout mLatFrame;

    private boolean mIsClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_capture);
        init();
    }

    private void init() {
        mCamera = new CameraSurfaceView(getApplicationContext());
        mLatFrame = (FrameLayout)findViewById(R.id.frmCapture);
        mLatFrame.addView(mCamera);

        TitleBitmapButton mBtnCapture = (TitleBitmapButton)findViewById(R.id.btnCapture);
        mBtnCapture.setBackground(R.drawable.btn_camera_capture_normal, R.drawable.btn_camera_capture_click);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btnCapture:
                        if(!mIsClicked) {
                            mIsClicked = true;
                            mCamera.capture(new CameraPictureCallback());
                        }
                        break;

                    default:
                        break;
                }
            }
        };
        mBtnCapture.setOnClickListener(OnButtonClick);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_CAMERA) {
            mCamera.capture(new CameraPictureCallback());
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_BACK) {
            mCamera.stopPreview();
            finish();
            return true;
        }

        return false;
    }

    /****************************************************************************************
     * 사진을 찍은 후 호출되는 콜백메서드 인터페이스 구현
     *  - 사진을 png 파일로 저장 후, 부모 액티비티의 onActivityResult() 로 성공메시지를 갖고 귀환
     */
    class CameraPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            int width = 480;
            int height = 360;

            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            bmp = Bitmap.createScaledBitmap(bmp, width, height, false);

            Matrix matrix = new Matrix();
            matrix.postRotate(0);
            bmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);

            try{
                File folder = new File(BasicInfo.FOLDER_PHOTO);
                if(!folder.isDirectory()) { folder.mkdirs(); }

                File file = new File(BasicInfo.FOLDER_PHOTO + BasicInfo.FILENAME_CAPTURED);
                if(file.exists()) { file.delete(); }

                FileOutputStream stream = new FileOutputStream(BasicInfo.FOLDER_PHOTO + BasicInfo.FILENAME_CAPTURED);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();

            } catch (Exception ex) {
                Log.e(TAG, "error in writing captured image.", ex);
                showContentDialog(BasicInfo.IMAGE_CANNOT_BE_STORED);
            }

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);

            finish();
        }
    }

    /**
     * showDialog() 대체
     * @param dialogId
     */
    private void showContentDialog(int dialogId) {
        AlertDialog.Builder builder = null;

        switch (dialogId) {
            case BasicInfo.IMAGE_CANNOT_BE_STORED:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.no_sdcard_message);
                builder.setPositiveButton(R.string.confirm_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                break;

            default:
                break;
        }

        if(builder != null) { builder.show(); }
    }
}

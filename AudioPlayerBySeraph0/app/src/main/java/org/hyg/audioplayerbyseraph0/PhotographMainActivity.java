package org.hyg.audioplayerbyseraph0;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class PhotographMainActivity extends AppCompatActivity {

    private final String TAG = "Photograph";
    private static String mImgFile = "capture.jpg";
    private Button mBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photograph_main);

        initPhotograph();
    }

    private void initPhotograph() {
        mBtnSave = (Button)findViewById(R.id.btnSave);

        FrameLayout frame = (FrameLayout)findViewById(R.id.frmPreview);

        final CameraSurfaceView sfvPreview = new CameraSurfaceView(getApplicationContext());
        frame.addView(sfvPreview);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            /**
             * 사진찍기 버튼 클릭 이벤트
             * @param view
             */
            @Override
            public void onClick(View view) {
                sfvPreview.capture(new Camera.PictureCallback() {

                    /**
                     * 사진을 찍은 후 찍은 사진 데이터를 받을 때 호출되는 콜백 메서드
                     * @param bytes : 캡처한 이미지
                     * @param camera
                     */
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        try{

                            /**
                             * 찍은 사진 데이터를 Bitmap 객체로 디코딩
                             */
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            /**
                             * 내용제공자를 통해 사진 목록에 추가
                             */
                            final String strUri = MediaStore.Images.Media.insertImage(getContentResolver()
                                                                                    , bmp
                                                                                    , "Captured Image"
                                                                                    , "Captured Image using Camera.");

                            if(strUri == null) {
                                Log.d(TAG, "failed to insert image.");
                                return;
                            }

                            Uri uri = Uri.parse(strUri);
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                            /**
                             * 카메라 미리보기 다시 시작
                             */
                            camera.startPreview();

                        } catch (Exception ex) { Log.e(TAG, "failed to insert image.", ex); }
                    }
                });
            }
        };
        mBtnSave.setOnClickListener(OnButtonClick);
    }
}

package org.hyg.audioplayerbyseraph0;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;


/**
 * 인텐트를 이용해 단말의 카메라 앱을 실행시켜 사진 찍기
 */
public class CaptureIntentMainActivity extends AppCompatActivity {

    private final String PROVIDER_NAME = ".fileprovider";
    private final String TAG = "CaptureIntent";
    private final static int REQUEST_IMAGE_CAPTURE = 672;

    private Button mBtnSnapshot;
    private ImageView mImgPreview;
    private File mFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_intent_main);

        //:: Log.d(TAG, getApplicationContext().getPackageName());
        initCapture();
    }

    private void initCapture() {
        mBtnSnapshot = (Button)findViewById(R.id.btnSnapshot);
        mImgPreview = (ImageView)findViewById(R.id.imgPreview);

        try {
            mFile = getFile();
        } catch (IOException e) { Log.e(TAG, "failed to create file.", e); }

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri;
/*
                try {
                    uri = Uri.fromFile(mFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    intent.setDataAndType(uri, "image/*");

                    Log.d(TAG, "FromFile:" + Uri.fromFile(mFile).toString());
                } catch (Exception ex) { Log.e(TAG, "error:" + ex.getMessage(), ex); }
*/
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    uri = Uri.fromFile(mFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    Log.d(TAG, "FromFile:" + Uri.fromFile(mFile).toString());
                } else {
                    try {
                        uri = GenericFileProvider.getUriForFile(CaptureIntentMainActivity.this
                                , getPackageName() + PROVIDER_NAME
                                , mFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        Log.d(TAG, "Parse:" + uri.toString());
                    } catch (Exception ex) { Log.e(TAG, ex.getMessage(), ex); }
                }


                if(intent.resolveActivity(getPackageManager()) != null) {
                    Log.d(TAG, "resolveActivity");
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        };
        mBtnSnapshot.setOnClickListener(OnButtonClick);
    }

    private File getFile() throws IOException {
        String fileName = "test.jpg";
        File dir = Environment.getExternalStorageDirectory();//getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(dir, fileName);

        Log.d(TAG, "getFile:" + file.getAbsolutePath());
        return file;
    }

    /**
     * 카메라 앱 실행 후 응답 받아 처리
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && requestCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bmp = BitmapFactory.decodeFile(mFile.getAbsolutePath(), options);

            mImgPreview.setImageBitmap(bmp);
        } else {
            Log.d(TAG, "error code:" + requestCode);
        }
    }
}

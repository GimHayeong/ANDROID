package org.hyg.seraph0.media.capture.intent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

/**
 * 인덴트를 이용해 단말의 카메라 앱을 실행시켜 사진 찍기
 */
public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1001;

    private File mFile = null;
    private ImageView mImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }



    private void init() {
        mImgView = (ImageView)findViewById(R.id.imgView);

        try{
            mFile = createFile();
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    /**
     * 외부저장소에 파일 저장
     *  : 외부저장소 : 기기에서 외부저장소를 제거할 수 있으므로, 항상 사용가능은 보장하지 않음.
     *                모든 사람이 읽을 수 있기 때문에 자신이 제어할 수 있는 범위에서, 외부의 타인도 읽기 가능.
     *                사용자가 앱을 삭제하면 getExternalFilesDir()의 디렉터리에 저장된, 해당 앱관련 파일도 시스템이 제거함.
     *                (앱을 제거후에도 저장된 파일을 사용가능하게 하려면 getExternalStoragePublicDirectory() 사용.)
     *                그러므로 액세스 제한이 필요치 않거나, 앱 또는 사용자의 컴퓨터에서 공유를 허용하는 경우에 적합.
     *  - 외부저장소 권한 취득 : <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     *  - 외부저장소 사용가능 여부 체크 : if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { ... }
     * @return
     * @throws IOException
     */
    private File createFile() throws IOException{
        String fileName = "test.jpg";
        File dir, file;

        dir = Environment.getExternalStorageDirectory();
        file = new File(dir, fileName);

        return file;
    }

    /**
     * 사진찍기 버튼 클릭 이벤트
     * @param view
     */
    public void onButtonClicked(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && requestCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            if(mFile != null) {
                Bitmap bmp = BitmapFactory.decodeFile(mFile.getAbsolutePath(), options);
                mImgView.setImageBitmap(bmp);
            } else { Log.d("RESULT", "file is null."); }
        }
    }
}

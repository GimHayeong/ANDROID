package org.hyg.seraph0.multinotepad;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.hyg.seraph0.multinotepad.common.TitleBitmapButton;
import org.w3c.dom.Text;

/**
 * 앨범에서 사진 선택
 */
public class VideoSelectionActivity extends AppCompatActivity {

    private static final String TAG = "[MEMO-VDO-ALBUM]";

    private String mUriVideoInAlbum;
    private ListView mLstVideo;
    private TextView mTxtSelectedVideo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_video_loading);

        init();
    }

    private void init() {
        mTxtSelectedVideo = (TextView)findViewById(R.id.txtSelectedVideo);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.btnOk:
                        Intent intent = getIntent();
                        if(mUriVideoInAlbum != null) {
                            intent.putExtra(BasicInfo.KEY_URI_VIDEO, mUriVideoInAlbum);
                            setResult(RESULT_OK, intent);
                        }
                        finish();
                        break;

                    case R.id.btnCancel:
                        finish();
                        break;
                }
            }
        };
        ((TitleBitmapButton)findViewById(R.id.btnOk)).setOnClickListener(OnButtonClick);
        ((TitleBitmapButton)findViewById(R.id.btnCancel)).setOnClickListener(OnButtonClick);

        mLstVideo = (ListView)findViewById(R.id.lstLoading);
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        final VideoCursorAdapter adapter = new VideoCursorAdapter(this, cursor);
        mLstVideo.setAdapter(adapter);

        mLstVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                    mUriVideoInAlbum = uri.getPath();
                    mTxtSelectedVideo.setText(((TextView)view).getText());
                    mTxtSelectedVideo.setSelected(true);
                } catch (Exception ex) { Log.d(TAG, ex.getMessage()); }
            }
        });
    }

    /**
     * 윈도우의 포커스가 변경될 때 호출
     * @param hasFocus : 포커스를 얻으면 true, 잃으면 false 전달받음
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                                    , Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath())));
        }
    }






    /****************************************************************************************
     * 데이터베이스 조회 결과를 가지고 있는 커서를 어댑터에 바인딩해 두고 어댑터뷰로 출력하기 위한 어댑터
     */
    class VideoCursorAdapter extends CursorAdapter{

        private int m_galleryItemBackground;

        public VideoCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }


        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            TextView txtVideoTitle = new TextView(context);
            txtVideoTitle.setTextColor(Color.WHITE);
            txtVideoTitle.setPadding(10, 10, 10, 10);

            return txtVideoTitle;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView txtVideoTitle = (TextView)view;

            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

            try{
                txtVideoTitle.setText(title);
            } catch (Exception ex) {}
        }
    }

}

package org.hyg.seraph0.multinotepad;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import org.hyg.seraph0.multinotepad.common.TitleBitmapButton;

import java.io.FileNotFoundException;

/**
 * 앨범에서 사진 선택
 */
public class PhotoSelectionActivity extends AppCompatActivity {

    private static final String TAG = "[MEMO-ALBUM]";
    private static int mSpacing = -45;
    private CoverFlow mGallery;

    private TextView mTxtSelectedPhoto;
    private ImageView mImgSelectedPhoto;
    private Uri mUriPhotoInAlbum;
    private Bitmap mBitmap = null;
    private void setSelectedPhoto(Uri uri) {
        try{
            mUriPhotoInAlbum = uri;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            if(mBitmap != null) {
                mBitmap.recycle();
            }

            mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
            mTxtSelectedPhoto.setVisibility(View.GONE);
            mImgSelectedPhoto.setImageBitmap(mBitmap);
            mImgSelectedPhoto.setVisibility(View.VISIBLE);
        } catch (Exception ex) { Log.d(TAG, ex.toString()); }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window win = getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_photo_selection);

        init();
    }

    private void init() {
        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.btnOk:
                        Intent intent = getIntent();
                        if(mUriPhotoInAlbum != null && mBitmap != null) {
                            intent.putExtra(BasicInfo.KEY_URI_PHOTO, mUriPhotoInAlbum);
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

        mTxtSelectedPhoto = (TextView)findViewById(R.id.txtSelectedPhoto);
        mImgSelectedPhoto = (ImageView)findViewById(R.id.imgSelectedPhoto);
        mImgSelectedPhoto.setVisibility(View.VISIBLE);

        mGallery = (CoverFlow)findViewById(R.id.glyLoadingGallery);
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                                , null
                                                , null
                                                , null
                                                , MediaStore.Images.Media.DATE_TAKEN + " DESC");
        PhotoCursorAdapter adapter = new PhotoCursorAdapter(this, cursor);
        mGallery.setAdapter(adapter);
        mGallery.setSpacing(mSpacing);
        mGallery.setSelection(-1, true);
        mGallery.setAnimationDuration(3000);
        AdapterView.OnItemClickListener OnGalleryItemClick = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                setSelectedPhoto(uri);
            }
        };
        mGallery.setOnItemClickListener(OnGalleryItemClick);
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

    /**
     * 이미지 넓이 반환
     *  - inJustDecodeBounds = true
     *     : 디코더가 객체를 생성하지도 메모리할당도 없이 로드대상 이미지 크기등의 정보 조회
     * @param fileName
     * @return
     */
    public static int getWidthOfBitmap(String fileName) {
        try{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);

            return options.outWidth;

        } catch (Exception ex) { Log.d(TAG, ex.toString()); }

        return 0;
    }


    /****************************************************************************************
     * 데이터베이스 조회 결과를 가지고 있는 커서를 어댑터에 바인딩해 두고 어댑터뷰로 출력하기 위한 어댑터
     */
    class PhotoCursorAdapter extends CursorAdapter{

        private int m_galleryItemBackground;

        public PhotoCursorAdapter(Context context, Cursor cursor) {
            this(context, cursor, true);
        }

        public PhotoCursorAdapter(Context context, Cursor cursor, boolean autoRequery) {
            super(context, cursor, autoRequery);

            TypedArray array = obtainStyledAttributes(R.styleable.Gallery1);
            m_galleryItemBackground = array.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 0);
            array.recycle();
        }



        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            ImageView img = new ImageView(context);

            int width = new Float(getResources().getDimension(R.dimen.cover_flow_item_width)).intValue();
            int height = new Float(getResources().getDimension(R.dimen.cover_flow_item_height)).intValue();

            img.setLayoutParams(new Gallery.LayoutParams(width, height));
            img.setBackgroundResource(m_galleryItemBackground);

            return img;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ImageView img = (ImageView)view;

            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            try{
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
            } catch (Exception ex) {  }

        }
    }

}

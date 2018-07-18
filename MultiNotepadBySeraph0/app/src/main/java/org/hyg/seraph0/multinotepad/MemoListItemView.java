package org.hyg.seraph0.multinotepad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by shiny on 2018-04-04.
 * ListView 의 각 아이템을 사용자가 정의한 뷰.java
 *  - [UI] memo_listitem.xml
 */

public class MemoListItemView extends LinearLayout {

    private final String TAG = "[MEMO-LST-VIEW]";

    private Context mContext;

    private ImageView mImgItemPhoto, mImgItemHandwriting, mImgItemVideoState, mImgItemVoiceState;
    private TextView mTxtItemDate, mTxtItemText;
    private String mVideoUri, mVoiceUri;
    private Bitmap mBmp;

    public void setVideoUri(String value) {
        mVideoUri = value;

        if(value == null || value.trim().length() < 1 || value.equals("-1")) {
            mImgItemVideoState.setImageResource(R.drawable.icon_video_empty);
        } else {
            mImgItemVideoState.setImageResource(R.drawable.icon_video);
        }
    }

    public void setVoiceUri(String value) {
        mVoiceUri = value;

        if(value == null || value.trim().length() < 1 || value.equals("-1")) {
            mImgItemVoiceState.setImageResource(R.drawable.icon_voice_empty);
        } else {
            mImgItemVoiceState.setImageResource(R.drawable.icon_voice);
        }
    }

    public MemoListItemView(Context context) {
        super(context);

        mContext = context;

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.memo_listitem, this, true);

        mImgItemPhoto = (ImageView)findViewById(R.id.imgItemPhoto);
        mTxtItemDate = (TextView)findViewById(R.id.txtItemDate);
        mTxtItemText = (TextView)findViewById(R.id.txtItemText);
        mImgItemHandwriting = (ImageView)findViewById(R.id.imgItemHandwriting);
        mImgItemVideoState = (ImageView)findViewById(R.id.imgItemVideoState);
        mImgItemVoiceState = (ImageView)findViewById(R.id.imgItemVoiceState);

        OnClickListener OnStateButtonClick = new OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.imgItemVideoState:
                        if(mVideoUri != null && mVideoUri.trim().length() > 0 && !mVideoUri.equals("-1")){
                            turnOnVideoPlayer();
                        }
                        break;

                    case R.id.imgItemVoiceState:
                        if(mVoiceUri != null && mVoiceUri.trim().length() > 0 && !mVoiceUri.equals("-1")){
                            turnOnVoicePlayer();
                        }
                        break;
                }
            }
        };
        mImgItemVideoState.setOnClickListener(OnStateButtonClick);
        mImgItemVoiceState.setOnClickListener(OnStateButtonClick);
    }

    private void turnOnVideoPlayer() {
        Intent intent;
        intent = new Intent(mContext, VideoPlayActivity.class);
        if(BasicInfo.isAbsoluteVideoPath(mVideoUri)) {
            intent.putExtra(BasicInfo.KEY_URI_VIDEO, BasicInfo.FOLDER_VIDEO + mVideoUri);
        } else {
            //::
            // 'content://media' 로 시작되는 URI인 경우
            intent.putExtra(BasicInfo.KEY_ID_VIDEO, mVideoUri);
        }
        mContext.startActivity(intent);
    }

    private void turnOnVoicePlayer() {
        try {
            Intent intent;
            intent = new Intent(mContext, VoicePlayActivity.class);
            intent.putExtra(BasicInfo.KEY_URI_VOICE, BasicInfo.FOLDER_VOICE + mVoiceUri);
            mContext.startActivity(intent);
        } catch (Exception ex) { Log.d(TAG, "turnOnVoicePlayer(): " + ex.getMessage()); }
    }

    /**
     *
     * @param index : 0 날짜, 1 텍스트메모, 2 손글씨메모, 3 사진 이미지데이터
     * @param data
     */
    public void setContents(int index, String data) {
        switch(index) {
            case 0:
                mTxtItemDate.setText(data);
                break;

            case 1:
                mTxtItemText.setText(data);
                break;

            case 2:
                if(data == null || data.equals("-1") || data.equals("")) {
                    mImgItemHandwriting.setImageBitmap(null);
                } else {
                    //:: 이미지 원본크기로 표시
                    //::  => mImgItemHandwriting.setImageURI(Uri.parse(BasicInfo.FOLDER_HANDWRITING + data));
                    //:: 이미지 사이즈 변경해서 표시
                    Bitmap bmp = BitmapFactory.decodeFile(BasicInfo.FOLDER_HANDWRITING + data);
                    bmp = Bitmap.createScaledBitmap(bmp, 100, 100, false);
                    mImgItemHandwriting.setImageBitmap(bmp);
                }
                break;

            case 3:
                if(data == null || data.equals("-1") || data.equals("")) {
                    mImgItemPhoto.setImageResource(R.drawable.person);
                } else {
                    if(mBmp != null) { mBmp.recycle(); }

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    mBmp = BitmapFactory.decodeFile(BasicInfo.FOLDER_PHOTO + data, options);
                    mImgItemPhoto.setImageBitmap(mBmp);
                }
                break;

            default:
                throw new IllegalArgumentException();
        }
    }

    public void setMediaContents(String videoUri, String voiceUri) {
        setVideoUri(videoUri);
        setVoiceUri(voiceUri);
    }
}

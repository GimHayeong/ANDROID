package org.hyg.seraph0.multinotepad;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import org.hyg.seraph0.multinotepad.common.TitleBitmapButton;
import org.hyg.seraph0.multinotepad.db.MediaRecord;
import org.hyg.seraph0.multinotepad.db.MemoDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//TODO: 처음 메모 등록시 동영상, 음성, 손글씨가 저장 안됨(수정시에는 됨)
public class InsertActivity extends AppCompatActivity {

    private static final String TAG = "[MEMO-NEW/MOD]";

    private TitleBitmapButton mBtnSave, mBtnDelete, mBtnCancel, mBtnDate, mBtnTime, mBtnText, mBtnHandwriting, mBtnVoice, mBtnVideo;
    private EditText mEdtMemoText;
    private ImageView mImgPhoto, mImgHandwriting;
    private Animation mAnimLeft, mAnimRight;

    private Integer mMemoId;
    private String mMemoMode,mMemoText;
    private String mMemoDate;
    private void setMemoDate(String value){
        try{
            Date date;
            if(BasicInfo.language.equals("ko")) {
                date = BasicInfo.dateNameFormat.parse(value);
            } else {
                date = BasicInfo.dateNameFormat3.parse(value);
            }
            mMemoDate = BasicInfo.dateFormat.format(date);
        } catch (ParseException e) { Log.e(TAG, "Exception in parsing date : " + value); }
    }
    private String getMemoDate() { return (mMemoDate == null || mMemoDate.trim().equals("")) ? BasicInfo.dateFormat.format(new Date()) : mMemoDate; }

    private int mPhotoId, mVideoId, mVoiceId, mHandwritingId;
    private String mPhotoUri, mVideoUri, mVoiceUri, mHandwritingUri;

    /**
     * 신규 또는 기존 데이터를 대체하기 위해 새로 촬영 등을 한 데이터의 URI
     */
    private String mTmpPhotoUri, mTmpVideoUri, mTmpVoiceUri, mTmpHandwritingUri;

    private Bitmap mBmpPhoto, mBmpHandwriting;

    private boolean mIsCapturedPhoto, mIsRecordedVideo, mIsRecordedVoice, mIsMadeHandwriting;
    private boolean mIsSavedPhoto, mIsSavedVideo, mIsSavedVoice, mIsSavedHandwriting;
    private boolean mIsCanceledPhoto, mIsCanceledVideo, mIsCanceledVoice, mIsCanceledHandwriting;

    private int mSelectedContentArray;
    private int mSelectedContentItem;
    private int mTextViewMode = 0;
    private void setTextViewMode(int value) {
        if(mTextViewMode == value) { return; }

        if(mTextViewMode == 0) {
            mImgHandwriting.setVisibility(View.VISIBLE);
            mEdtMemoText.setVisibility(View.GONE);
            mBtnText.setSelected(false);
            mBtnHandwriting.setSelected(true);
        } else if(mTextViewMode == 1){
            mImgHandwriting.setVisibility(View.GONE);
            mEdtMemoText.setVisibility(View.VISIBLE);
            mBtnText.setSelected(true);
            mBtnHandwriting.setSelected(false);
        }
        mTextViewMode = value;
    }


    private Calendar mCalendar = Calendar.getInstance();
    private void setCalendar(Date value) {
        mCalendar.setTime(value);
        int year = mCalendar.get(Calendar.YEAR);
        int monthOfYear = mCalendar.get(Calendar.MONTH);
        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);

        setCalendarDate(year, monthOfYear, dayOfMonth);
        setCalendarTime(hourOfDay, minute);
    }

    /**
     *
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    private void setCalendarDate(int year, int monthOfYear, int dayOfMonth) {
        String strMonth = convertToNumericFormatString(monthOfYear + 1);
        String strDay = convertToNumericFormatString(dayOfMonth);

        if(BasicInfo.language.equals("ko")) {
            mBtnDate.setText(year + "년 " + strMonth + "월 " + strDay + "일");
        } else {
            mBtnDate.setText(year + "-" + strMonth + "-" + strDay);
        }
    }

    /**
     *
     * @param hourOfDay
     * @param minute
     */
    private void setCalendarTime(int hourOfDay, int minute) {
        String strHour = convertToNumericFormatString(hourOfDay);
        String strMinute = convertToNumericFormatString(minute);

        if(BasicInfo.language.equals("ko")) {
            mBtnTime.setText(strHour + "시 " + strMinute + "분");
        } else {
            mBtnTime.setText(strHour + ":" + strMinute);
        }
    }

    /**
     *
     */
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(year, monthOfYear, dayOfMonth);

            setCalendarDate(year, monthOfYear, dayOfMonth);
        }
    };

    /**
     *
     */
    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar.set(Calendar.MINUTE, minute);

            setCalendarTime(hourOfDay, minute);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case BasicInfo.REQ_PHOTO_CAPTURE_ACTIVITY:
                if(resultCode == RESULT_OK) {
                    if(IsValidFile(BasicInfo.FOLDER_PHOTO + BasicInfo.FILENAME_CAPTURED)) {
                        mBmpPhoto = BitmapFactory.decodeFile(BasicInfo.FOLDER_PHOTO + BasicInfo.FILENAME_CAPTURED);
                        mTmpPhotoUri = BasicInfo.FILENAME_CAPTURED;
                        mImgPhoto.setImageBitmap(mBmpPhoto);
                        mIsCapturedPhoto = true;
                        mImgPhoto.invalidate();
                    }
                } else { Log.d(TAG, "file doesn't exists : " + BasicInfo.FOLDER_PHOTO + BasicInfo.FILENAME_CAPTURED); }
                break;

            case BasicInfo.REQ_PHOTO_SELECTION_ACTIVITY:
                if(resultCode == RESULT_OK) {
                    Uri uri = intent.getParcelableExtra(BasicInfo.KEY_URI_PHOTO);
                    try{
                        mBmpPhoto = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri)
                                                            , null
                                                            , null);
                    } catch (FileNotFoundException ex) { Log.e(TAG, "file doesn't exists", ex); }
                    mImgPhoto.setImageBitmap(mBmpPhoto);
                    mIsCapturedPhoto = true;
                    mImgPhoto.invalidate();
                }
                break;

            case BasicInfo.REQ_HANDWRITING_MAKING_ACTIVITY:
                if(resultCode == RESULT_OK) {
                    if(IsValidFile(BasicInfo.FOLDER_HANDWRITING + BasicInfo.FILENAME_MADE)) {
                        mBmpHandwriting = BitmapFactory.decodeFile(BasicInfo.FOLDER_HANDWRITING + BasicInfo.FILENAME_MADE);
                        mTmpHandwritingUri = BasicInfo.FILENAME_MADE;
                        mIsMadeHandwriting = true;
                        mImgHandwriting.setImageBitmap(mBmpHandwriting);
                    }
                }
                break;

            case BasicInfo.REQ_VIDEO_RECORDING_ACTIVITY:
                if(resultCode == RESULT_OK) {
                    if(IsValidFile(BasicInfo.FOLDER_VIDEO + BasicInfo.FILENAME_RECORDED)) {
                        mTmpVideoUri = BasicInfo.FILENAME_RECORDED;
                        mIsRecordedVideo = true;
                        mBtnVideo.setCompoundDrawablesWithIntrinsicBounds(null
                                                                        , getResources().getDrawable(R.drawable.icon_video)
                                                                        , null
                                                                        , null);
                    }
                }
                break;

            case BasicInfo.REQ_VIDEO_LOADING_ACTIVITY:
                if(resultCode == RESULT_OK) {
                    String videoUri = intent.getStringExtra(BasicInfo.KEY_URI_VIDEO);
                    mTmpVideoUri = BasicInfo.URI_MEDIA_FORMAT + videoUri;
                    mIsRecordedVideo = true;
                    mBtnVideo.setCompoundDrawablesWithIntrinsicBounds(null
                                                                    , getResources().getDrawable(R.drawable.icon)
                                                                    , null
                                                                    , null);
                }
                break;

            case BasicInfo.REQ_VOICE_RECORDING_ACTIVITY:
                if(resultCode == RESULT_OK) {
                    if(IsValidFile(BasicInfo.FOLDER_VOICE + BasicInfo.FILENAME_RECORDED)) {
                        mTmpVoiceUri = BasicInfo.FILENAME_RECORDED;
                        mIsRecordedVoice = true;
                        mBtnVoice.setCompoundDrawablesWithIntrinsicBounds(null
                                                                        , getResources().getDrawable(R.drawable.icon_voice)
                                                                        , null
                                                                        , null);
                    }
                }
                break;

            default:
                break;
        }
    }

    private void init() {
        mImgPhoto = (ImageView)findViewById(R.id.imgItemPhoto);
        mEdtMemoText = (EditText)findViewById(R.id.edtItemMemo);
        mBtnText = (TitleBitmapButton)findViewById(R.id.btnText);
        mBtnHandwriting = (TitleBitmapButton)findViewById(R.id.btnHandwriting);
        mImgHandwriting = (ImageView)findViewById(R.id.imgItemHandwriting);
        mBtnDelete = (TitleBitmapButton)findViewById(R.id.btnDelete);
        mBtnVideo = (TitleBitmapButton)findViewById(R.id.btnVideo);
        mBtnVoice = (TitleBitmapButton)findViewById(R.id.btnVoice);

        mBtnVideo.setCompoundDrawablesWithIntrinsicBounds(null
                                                        , getResources().getDrawable(R.drawable.icon_video)
                                                        , null
                                                        , null);
        mBtnVoice.setCompoundDrawablesWithIntrinsicBounds(null
                                                        , getResources().getDrawable(R.drawable.icon_voice)
                                                        , null
                                                        , null);

        mAnimLeft = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        mAnimRight = AnimationUtils.loadAnimation(this, R.anim.translate_right);
        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        mAnimLeft.setAnimationListener(animListener);
        mAnimRight.setAnimationListener(animListener);

        mBtnText.setSelected(true);
        mBtnHandwriting.setSelected(false);


        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.btnText:
                        if(mTextViewMode == 1) {
                            setTextViewMode(0);
                            mEdtMemoText.startAnimation(mAnimLeft);
                        }
                        break;

                    case R.id.btnHandwriting:
                        if(mTextViewMode == 0) {
                            mEdtMemoText.startAnimation(mAnimRight);
                            mAnimRight.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {}

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    setTextViewMode(1);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {}
                            });
                        }
                        break;

                    case R.id.imgItemPhoto:
                        if(mIsCapturedPhoto || mIsSavedPhoto) {
                            showContentDialog(BasicInfo.CONTENT_PHOTO_EX);
                        } else {
                            showContentDialog(BasicInfo.CONTENT_PHOTO);
                        }
                        break;

                    case R.id.imgItemHandwriting:
                        Intent intent = new Intent(getApplicationContext(), HandwritingMakingActivity.class);
                        startActivityForResult(intent, BasicInfo.REQ_HANDWRITING_MAKING_ACTIVITY);
                        break;

                    case R.id.btnDelete:
                        showContentDialog(BasicInfo.CONFIRM_DELETE);
                        break;

                    case R.id.btnVideo:
                        if(mIsRecordedVideo || mIsSavedVideo) {
                            showContentDialog(BasicInfo.CONTENT_VIDEO_EX);
                        } else {
                            showContentDialog(BasicInfo.CONTENT_VIDEO);
                        }
                        break;

                    case R.id.btnVoice:
                        if(mIsRecordedVoice || mIsSavedVoice) {
                            showContentDialog(BasicInfo.CONTENT_VOICE_EX);
                        } else {
                            showContentDialog(BasicInfo.CONTENT_VOICE);
                        }
                        break;
                }
            }
        };

        mBtnText.setOnClickListener(OnButtonClick);
        mBtnHandwriting.setOnClickListener(OnButtonClick);
        mImgPhoto.setOnClickListener(OnButtonClick);
        mImgHandwriting.setOnClickListener(OnButtonClick);
        mBtnDelete.setOnClickListener(OnButtonClick);
        mBtnVideo.setOnClickListener(OnButtonClick);
        mBtnVoice.setOnClickListener(OnButtonClick);

        initBottomButtons();
        initMediaLayout();
        initCalendar();

        Intent intent = getIntent();
        mMemoMode = intent.getStringExtra(BasicInfo.KEY_MEMO_MODE);
        if(mMemoMode.equals(BasicInfo.MODE_MODIFY) || mMemoMode.equals(BasicInfo.MODE_VIEW)) {
            processIntent(intent);
            setTitle(R.string.view_title);
            mBtnSave.setText(R.string.modify_btn);
            mBtnDelete.setVisibility(View.VISIBLE);
        } else {
            setTitle(R.string.new_title);
            mBtnSave.setText(R.string.save_btn);
            mBtnDelete.setVisibility(View.GONE);
        }
    }

    /**
     * showDialog() -> onCreateDialog() 대신 구현
     *
     *  - dismiss() : dismissDialog(..) 대체함
     *  - @Override
     *  protected Dialog onCreateDialog(int id) {
     *      :
     *      return super.onCreateDialog(id);
     *  }
     *
     * @param dialogId
     */
    private void showContentDialog(int dialogId) {
        AlertDialog.Builder builder = null;

        switch (dialogId) {
            case BasicInfo.CONFIRM_TEXT_INPUT:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.memo_title);
                builder.setMessage(R.string.text_input_message);
                builder.setPositiveButton(R.string.confirm_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                break;

            case BasicInfo.CONTENT_PHOTO:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.selection_title);
                mSelectedContentArray = R.array.array_photo;
                builder.setSingleChoiceItems(mSelectedContentArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mSelectedContentItem = whichButton;
                    }
                });
                builder.setPositiveButton(R.string.selection_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (mSelectedContentItem == 0) {
                            showActivity(BasicInfo.REQ_PHOTO_CAPTURE_ACTIVITY);
                        } else if (mSelectedContentItem == 1) {
                            showActivity(BasicInfo.REQ_PHOTO_SELECTION_ACTIVITY);
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    }
                });

                break;

            case BasicInfo.CONTENT_PHOTO_EX:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.selection_title);
                mSelectedContentArray = R.array.array_photo_ex;
                builder.setSingleChoiceItems(mSelectedContentArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mSelectedContentItem = whichButton;
                    }
                });
                builder.setPositiveButton(R.string.selection_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (mSelectedContentItem == 0) {
                            showActivity(BasicInfo.REQ_PHOTO_CAPTURE_ACTIVITY);
                        } else if (mSelectedContentItem == 1) {
                            showActivity(BasicInfo.REQ_PHOTO_SELECTION_ACTIVITY);
                        } else if (mSelectedContentItem == 2) {
                            mIsCanceledPhoto = true;
                            mIsCapturedPhoto = false;

                            if (BasicInfo.language.equals("ko")) {
                                mImgPhoto.setImageResource(R.drawable.person_add);
                            } else {
                                mImgPhoto.setImageResource(R.drawable.person_add_en);
                            }
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                break;

            case BasicInfo.CONFIRM_DELETE:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.memo_title);
                builder.setMessage(R.string.memo_delete_question);
                builder.setPositiveButton(R.string.yes_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteMemo();
                    }
                });
                builder.setNegativeButton(R.string.no_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                break;

            case BasicInfo.CONTENT_VIDEO:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.selection_title);
                mSelectedContentArray = R.array.array_video;
                builder.setSingleChoiceItems(mSelectedContentArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mSelectedContentItem = whichButton;
                    }
                });
                builder.setPositiveButton(R.string.selection_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (mSelectedContentItem == 0) {
                            showActivity(BasicInfo.REQ_VIDEO_RECORDING_ACTIVITY);
                        } else if (mSelectedContentItem == 1) {
                            showActivity(BasicInfo.REQ_VIDEO_LOADING_ACTIVITY);
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                break;

            case BasicInfo.CONTENT_VIDEO_EX:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.selection_title);
                mSelectedContentArray = R.array.array_video_ex;
                builder.setSingleChoiceItems(mSelectedContentArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mSelectedContentItem = whichButton;
                    }
                });
                builder.setPositiveButton(R.string.selection_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (mSelectedContentItem == 0) {
                            showActivity(BasicInfo.PLAY_VIDEO_ACTIVITY);
                        } else if (mSelectedContentItem == 1) {
                            showActivity(BasicInfo.REQ_VIDEO_RECORDING_ACTIVITY);
                        } else if (mSelectedContentItem == 2) {
                            showActivity(BasicInfo.REQ_VIDEO_LOADING_ACTIVITY);
                        } else if (mSelectedContentItem == 3) {
                            mIsCanceledVideo = true;
                            mIsRecordedVideo = false;

                            mBtnVideo.setCompoundDrawablesWithIntrinsicBounds(null
                                    , getResources().getDrawable(R.drawable.icon_voice_empty)
                                    , null
                                    , null);
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                break;

            case BasicInfo.CONTENT_VOICE:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.selection_title);
                mSelectedContentArray = R.array.array_voice;
                builder.setSingleChoiceItems(mSelectedContentArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mSelectedContentItem = whichButton;
                    }
                });
                builder.setPositiveButton(R.string.selection_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (mSelectedContentItem == 0) {
                            showActivity(BasicInfo.REQ_VOICE_RECORDING_ACTIVITY);
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                break;

            case BasicInfo.CONTENT_VOICE_EX:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.selection_title);
                mSelectedContentArray = R.array.array_voice_ex;
                builder.setSingleChoiceItems(mSelectedContentArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mSelectedContentItem = whichButton;
                    }
                });
                builder.setPositiveButton(R.string.selection_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (mSelectedContentItem == 0) {
                            showActivity(BasicInfo.PLAY_VOICE_ACTIVITY);
                        } else if(mSelectedContentItem == 1) {
                            showActivity(BasicInfo.REQ_VOICE_RECORDING_ACTIVITY);
                        } else if (mSelectedContentItem == 2) {
                            mIsCanceledVoice = true;
                            mIsRecordedVoice = false;
                            mBtnVoice.setCompoundDrawablesWithIntrinsicBounds(null
                                    , getResources().getDrawable(R.drawable.icon_voice_empty)
                                    , null
                                    , null);
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                break;

            default:
                break;
        }

        builder.show();
    }



    /**
     * 화면 하단의 저장/취소 버튼 초기화
     */
    private void initBottomButtons() {
        mBtnSave = (TitleBitmapButton)findViewById(R.id.btnSave);
        mBtnCancel = (TitleBitmapButton)findViewById(R.id.btnCancel);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnSave:
                        boolean isParsedMemo = parseMemo();
                        if(isParsedMemo){
                            if(mMemoMode.equals(BasicInfo.MODE_INSERT)) {
                                saveMemo();
                            } else {
                                modifyMemo();
                            }
                        }
                        break;

                    case R.id.btnCancel:
                        finish();
                        break;
                }
            }
        };
        mBtnSave.setOnClickListener(OnButtonClick);
        mBtnCancel.setOnClickListener(OnButtonClick);
    }

    /**
     * 입력한 데이터의 유효성 체크
     * @return
     */
    private boolean parseMemo() {
        String strDate = mBtnDate.getText().toString();
        String strTime = mBtnTime.getText().toString();
        String strDateTime = strDate + " " + strTime;

        setMemoDate(strDateTime);

        mMemoText = mEdtMemoText.getText().toString();

        if(mIsMadeHandwriting ||
                (mMemoMode != null && (mMemoMode.equals(BasicInfo.MODE_MODIFY) || mMemoMode.equals(BasicInfo.MODE_VIEW)))) {
            // nothing ..
        } else {
            if(mMemoText.trim().length() < 1) {
                showContentDialog(BasicInfo.CONFIRM_TEXT_INPUT);
                return false;
            }
        }

        return true;
    }

    /**
     * 새로운 메모 저장
     */
    private void saveMemo() {
        int photoId = saveMediaFile(mIsCapturedPhoto
                , MemoDatabase.TABLE_PHOTO
                , BasicInfo.FOLDER_PHOTO
                , mPhotoId
                , mTmpPhotoUri
                , mBmpPhoto);

        int videoId = saveMediaFile(mIsRecordedVideo
                , MemoDatabase.TABLE_VIDEO
                , BasicInfo.FOLDER_VIDEO
                , mVideoId
                , mTmpVideoUri);

        int voiceId = saveMediaFile(mIsRecordedVoice
                , MemoDatabase.TABLE_VOICE
                , BasicInfo.FOLDER_VOICE
                , mVoiceId
                , mTmpVoiceUri);

        int handwritingId = saveMediaFile(mIsMadeHandwriting
                , MemoDatabase.TABLE_HANDWRITING
                , BasicInfo.FOLDER_HANDWRITING
                , mHandwritingId
                , mTmpHandwritingUri
                , mBmpHandwriting);
        Log.d(TAG, "saveMediaFile() ===> photoId: " + photoId + ", videoId: " + videoId + ", voiceId: " + voiceId + ", handwritingId: " + handwritingId);
        ContentValues params = new ContentValues();
        if(photoId > 0) params.put(BasicInfo.KEY_ID_PHOTO, photoId);
        if(videoId > 0) params.put(BasicInfo.KEY_ID_VIDEO, videoId);
        if(voiceId > 0) params.put(BasicInfo.KEY_ID_VOICE, voiceId);
        if(handwritingId > 0) params.put(BasicInfo.KEY_ID_HANDWRITING, handwritingId);
        params.put(BasicInfo.COLUMN_MEMO_DATE, getMemoDate());
        params.put(BasicInfo.COLUMN_MEMO_TEXT, mMemoText);
        if(MainActivity.mDatabase != null) {
            MainActivity.mDatabase.insertRecord(MemoDatabase.TABLE_MEMO, params);
        }

        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 기존의 메모 수정
     */
    private void modifyMemo() {
        int photoId = saveMediaFile(mIsCapturedPhoto
                , MemoDatabase.TABLE_PHOTO
                , BasicInfo.FOLDER_PHOTO
                , mPhotoId
                , mPhotoUri
                , mBmpPhoto
                , mIsCanceledPhoto && mIsSavedPhoto);

        int videoId = saveMediaFile(mIsRecordedVideo
                , MemoDatabase.TABLE_VIDEO
                , BasicInfo.FOLDER_VIDEO
                , mVideoId
                , mVideoUri
                , mIsCanceledVideo && mIsSavedVideo);

        int voiceId = saveMediaFile(mIsRecordedVoice
                , MemoDatabase.TABLE_VOICE
                , BasicInfo.FOLDER_VOICE
                , mVoiceId
                , mVoiceUri
                , mIsCanceledVoice && mIsSavedVoice);

        int handwritingId = saveMediaFile(mIsMadeHandwriting
                , MemoDatabase.TABLE_HANDWRITING
                , BasicInfo.FOLDER_HANDWRITING
                , mHandwritingId
                , mHandwritingUri
                , mBmpHandwriting
                , mIsCanceledHandwriting && mIsSavedHandwriting);

        mPhotoId = photoId;
        mHandwritingId = handwritingId;
        mVideoId = videoId;
        mVoiceId = voiceId;

        ContentValues params = new ContentValues();
        if(photoId > 0) params.put(BasicInfo.KEY_ID_PHOTO, photoId);
        if(videoId > 0) params.put(BasicInfo.KEY_ID_VIDEO, videoId);
        if(voiceId > 0) params.put(BasicInfo.KEY_ID_VOICE, voiceId);
        if(handwritingId > 0) params.put(BasicInfo.KEY_ID_HANDWRITING, handwritingId);
        params.put(BasicInfo.COLUMN_MEMO_DATE, getMemoDate());
        params.put(BasicInfo.COLUMN_MEMO_TEXT, mMemoText);

        if(MainActivity.mDatabase != null) {
            MainActivity.mDatabase.updateRecord(MemoDatabase.TABLE_MEMO, params, "_id=?", new String[]{ mMemoId.toString() });
        }

        Intent intent = getIntent();
        intent.putExtra(BasicInfo.KEY_MEMO_TEXT, mMemoText);
        intent.putExtra(BasicInfo.KEY_ID_PHOTO, mPhotoId);
        intent.putExtra(BasicInfo.KEY_ID_VIDEO, mVideoId);
        intent.putExtra(BasicInfo.KEY_ID_VOICE, mVoiceId);
        intent.putExtra(BasicInfo.KEY_ID_HANDWRITING, mHandwritingId);
        intent.putExtra(BasicInfo.KEY_URI_PHOTO, mPhotoUri);
        intent.putExtra(BasicInfo.KEY_URI_VIDEO, mVideoUri);
        intent.putExtra(BasicInfo.KEY_URI_VOICE, mVoiceUri);
        intent.putExtra(BasicInfo.KEY_URI_HANDWRITING, mHandwritingUri);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 기존의 메모 삭제
     */
    private void deleteMemo() {

        deleteMediaFile(MemoDatabase.TABLE_PHOTO, mPhotoId, mPhotoUri, BasicInfo.FOLDER_PHOTO);

        deleteMediaFile(MemoDatabase.TABLE_VIDEO, mVideoId, mVideoUri, BasicInfo.FOLDER_VIDEO);

        deleteMediaFile(MemoDatabase.TABLE_VOICE, mVoiceId, mVoiceUri, BasicInfo.FOLDER_VIDEO);

        deleteMediaFile(MemoDatabase.TABLE_HANDWRITING, mHandwritingId, mHandwritingUri, BasicInfo.FOLDER_HANDWRITING);

        deleteMediaFile(MemoDatabase.TABLE_MEMO, mMemoId);

        setResult(RESULT_OK);
        finish();
    }


    private void deleteMediaFile(String table, Integer id) {
        if (id > 0 && MainActivity.mDatabase != null) {
            MainActivity.mDatabase.deleteMediaById(table, id);
        }
    }
    private void deleteMediaFile(String table, Integer id, String uri, String folder) {
        deleteMediaFile(table, id);

        if (uri != null && !uri.equals("")) {
            File oldFile = new File(folder + uri);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }
    }

    private int saveMediaFile(boolean isPossible, String table, String dir, int oldId, String oldUri) {
        return saveMediaFile(isPossible, table, dir, oldId, oldUri, null);
    }
    private int saveMediaFile(boolean isPossible, String table, String dir, int oldId, String oldUri, Bitmap bmp) {
        return saveMediaFile(isPossible, table, dir, oldId, oldUri, bmp, false);
    }
    private int saveMediaFile(boolean isPossible, String table, String dir, int oldId, String oldUri, boolean isCancelNSaved){
        return saveMediaFile(isPossible, table, dir, oldId, oldUri, null, isCancelNSaved);
    }
    /**
     * 사진, 손글씨, 동영상, 음성 파일 저장
     *  - photo : isCaptured 일때,
     *           기존 아이디의 레코드 테이블에서 삭제 -> 기존 파일 삭제
     *           -> 해당 디렉토리에 새 파일(png) 생성 -> 새 파일 정보 테이블에 입력(신규 아이디) -> 신규 아이디 조회
     *  - handwriting : isMade 일때,
     *           기존 아이디의 레코드 테이블에서 삭제 -> 기존 파일 삭제
     *           -> 해당 디렉토리에 새 파일(png) 생성 -> 새 파일 정보 테이블에 입력(신규 아이디) -> 신규 아이디 조회
     *  - video : isRecorded 일때,
     *           기존 아이디의 레코드 삭제 -> 기존 파일 삭제
     *           -> Uri 가 'content://media' 로 시작되지 않으면 새 디렉토리 생성
     *           -> 새 파일 정보 테이블에 입력(신규 아이디) -> 신규 아이디 조회
     *  - voice : isRecorded 일때,
     *           기존 아이디의 레크드 삭제 -> 기존 파일 삭제
     *           -> 새 디렉토리 생성 -> 새 파일 정보 입력(신규 아이디) -> 신규 아이디 조회
     *
     * @param isPossible : 저장대상 촬영한 사진, 비디오 또는 녹음파일, 손글씨 파일의 존재 여부
     * @param table : 저장대상을 저장할 DB의 테이블
     * @param dir : 저장대상 파일의 물리적 위치
     * @param oldId : 저장할 미디어의 DB의 미디어 ID
     * @param oldUri : 저장할 미디어의 URI
     * @param bmp : 뷰에 표시할 비트맵
     * @param isCanceledNSaved : 저장된 파일을 취소하는 것이면 미디어 ID 를 -1 로 업데이트
     * @return
     */
    private int saveMediaFile(boolean isPossible, String table, String dir, int oldId, String oldUri, Bitmap bmp, boolean isCanceledNSaved) {
        int newId = -1;
        String newUri = null;

        if(isPossible) {
            try {
                // 수정이면
                if(mMemoMode != null && (mMemoMode.equals(BasicInfo.MODE_MODIFY) || mMemoMode.equals(BasicInfo.MODE_VIEW))) {
                    deleteMediaFile(table, oldId, oldUri, dir);
                    newUri = createFileName();
                } else {
                    newUri = oldUri;
                }
                Log.d(TAG, "saveMediaFile() ===> oldUri: " + oldUri + ", newUri: " + newUri);
                // 신규입력 또는 수정이면
                if (dir != null && !dir.equals("")) {
                    File folder = new File(dir);
                    if (!folder.isDirectory()) {
                        folder.mkdirs();
                    }
                }

                if(newUri != null) {
                    if(bmp != null) {
                        FileOutputStream stream = new FileOutputStream(dir + newUri);
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        stream.close();
                    } else {
                        if(mTmpVideoUri != null && !mTmpVideoUri.equals("")
                                && dir.equals(BasicInfo.FOLDER_VIDEO)
                                && !BasicInfo.isAbsoluteVideoPath(mTmpVideoUri)) {
                            newUri = mTmpVideoUri;
                        } else {
                            File tmpFile = new File(dir + "recorded");
                            tmpFile.renameTo(new File(dir + newUri));
                        }
                    }

                    if(MainActivity.mDatabase != null) {
                        ContentValues params = new ContentValues();
                        params.put("URI", newUri);
                        newId = MainActivity.mDatabase.insertMedia(table, params);
                    }

                } else { //***** newUri == null

                    if (isCanceledNSaved) {
                        deleteMediaFile(table, oldId, oldUri, dir);
                    }
                }
                Log.d(TAG, "saveMediaFile() ===> oldId: " + oldId + ", newId: " + newId);
            } catch (IOException ex) { Log.d(TAG, "saveMediaFile() ==> IOException: " + ex.getMessage()); }
        }

        return newId;
    }


    /**
     * 사진, 동영상, 음성 데이터 관련 초기화
     */
    private void initMediaLayout() {
        mIsCapturedPhoto = mIsRecordedVideo = mIsRecordedVoice = mIsMadeHandwriting = false;

        mBtnVideo = (TitleBitmapButton)findViewById(R.id.btnVideo);
        mBtnVoice = (TitleBitmapButton)findViewById(R.id.btnVoice);
    }

    /**
     * 날짜 데이터 관련 초기화
     */
    private void initCalendar() {
        mBtnDate = (TitleBitmapButton)findViewById(R.id.btnDate);
        mBtnTime = (TitleBitmapButton)findViewById(R.id.btnTime);

        Date date = new Date();
        setCalendar(date);


        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strDate = ((Button)view).getText().toString();
                Calendar calendar = Calendar.getInstance();
                Date date = new Date();
                switch(view.getId()) {
                    case R.id.btnDate:
                        try {
                            if(BasicInfo.language.equals("ko")){
                                date = BasicInfo.dateDayNameFormat.parse(strDate);
                            } else {
                                date = BasicInfo.dateDayFormat.parse(strDate);
                            }
                        } catch (ParseException e) { Log.d(TAG, "Exception in parsing date : " + date); }

                        calendar.setTime(date);

                        new DatePickerDialog(
                                InsertActivity.this
                                , dateSetListener
                                , calendar.get(Calendar.YEAR)
                                , calendar.get(Calendar.MONTH)
                                , calendar.get(Calendar.DAY_OF_MONTH)
                        ).show();

                        break;

                    case R.id.btnTime:
                        try {
                            if(BasicInfo.language.equals("ko")){
                                date = BasicInfo.dateTimeNameFormat.parse(strDate);
                            } else {
                                date = BasicInfo.dateTimeFormat.parse(strDate);
                            }
                        } catch (ParseException e) { Log.d(TAG, "Exception in parsing date : " + date); }

                        calendar.setTime(date);

                        new TimePickerDialog(
                                InsertActivity.this
                                , timeSetListener
                                , calendar.get(Calendar.HOUR_OF_DAY)
                                , calendar.get(Calendar.MINUTE)
                                , true
                        ).show();

                        break;
                }
            }
        };

        mBtnDate.setOnClickListener(OnButtonClick);
        mBtnTime.setOnClickListener(OnButtonClick);
    }


    /**
     * UI 설정
     * @param intent
     */
    private void processIntent(Intent intent) {
        mMemoId = intent.getIntExtra(BasicInfo.KEY_MEMO_ID, -1);
        mMemoDate = intent.getStringExtra(BasicInfo.KEY_MEMO_DATE);
        String curtMemoText = intent.getStringExtra(BasicInfo.KEY_MEMO_TEXT);
        mEdtMemoText.setText(curtMemoText);
        mPhotoId = intent.getIntExtra(BasicInfo.KEY_ID_PHOTO, -1);
        mPhotoUri = intent.getStringExtra(BasicInfo.KEY_URI_PHOTO);
        mVideoId = intent.getIntExtra(BasicInfo.KEY_ID_VIDEO, -1);
        mVideoUri = intent.getStringExtra(BasicInfo.KEY_URI_VIDEO);
        mVoiceId = intent.getIntExtra(BasicInfo.KEY_ID_VOICE, -1);
        mVoiceUri = intent.getStringExtra(BasicInfo.KEY_URI_VOICE);
        mHandwritingId = intent.getIntExtra(BasicInfo.KEY_ID_HANDWRITING, -1);
        mHandwritingUri = intent.getStringExtra(BasicInfo.KEY_URI_HANDWRITING);

        setMedia(mPhotoId, mPhotoUri, R.drawable.person);
        setMedia(mVideoId, mVideoUri, R.drawable.icon_video);
        setMedia(mVoiceId, mVoiceUri, R.drawable.icon_voice);
        setMedia(mHandwritingId, mHandwritingUri, -1);
        setCalendarDateTime(mMemoDate);

        if(curtMemoText != null && !curtMemoText.equals("")) {
            setTextViewMode(0);
        }
    }


    /**
     * 사진, 동영상, 음성 데이터 관련 UI 설정
     * @param id : -1 또는 1보다 작은 값이면 관련 미디어(사진, 동영상, 음성)파일이 존재하지 않음.
     * @param uri : 관련 미디어파일 uri
     * @param resId : -1이면 Handwriting
     */
    private void setMedia(int id, String uri, int resId) {
        switch(resId){
            case R.drawable.person:
                if(id > 0) {
                    mIsSavedPhoto = true;
                    mTmpPhotoUri = uri;
                    mImgPhoto.setImageURI(Uri.parse(BasicInfo.FOLDER_PHOTO + uri));
                } else {
                    mImgPhoto.setImageResource(BasicInfo.language.equals("ko") ? R.drawable.person_add : R.drawable.person_add_en);
                }
                break;

            case R.drawable.icon_video:
                if(id > 0) {
                    mIsSavedVideo = true;
                    mTmpVideoUri = uri;
                } else {
                    resId = R.drawable.icon_video_empty;
                }

                mBtnVideo.setCompoundDrawablesWithIntrinsicBounds(null
                                                                , getResources().getDrawable(resId)
                                                                , null
                                                                , null);

                break;

            case R.drawable.icon_voice:
                if(id > 0) {
                    mIsSavedVoice = true;
                    mTmpVoiceUri = uri;
                } else {
                    resId = R.drawable.icon_voice_empty;
                }
                mBtnVoice.setCompoundDrawablesWithIntrinsicBounds(null
                                                                , getResources().getDrawable(resId)
                                                                , null
                                                                , null);

                break;

            case -1:
                if(id > 0) {
                    mIsSavedHandwriting = true;
                    mTmpHandwritingUri = uri;
                    Bitmap bmp = BitmapFactory.decodeFile(BasicInfo.FOLDER_HANDWRITING + uri);
                    mImgHandwriting.setImageBitmap(bmp);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 날짜 데이터 관련 UI 설정
     * @param memoDate
     */
    private void setCalendarDateTime(String memoDate) {
        Date date = new Date();
        try{
            if(BasicInfo.language.equals("ko")) {
                date = BasicInfo.dateNameFormat2.parse(memoDate);
            } else {
                date = BasicInfo.dateNameFormat3.parse(memoDate);
            }
        } catch (Exception ex) { }

        setCalendar(date);
    }

    /**
     * 월, 일, 시간, 분 값을 '%02d' 또는 '00' 사용자 지정 숫자 형식 문자열로 변환하여 반환
     * @param value
     * @return
     */
    private String convertToNumericFormatString(int value) {
        return (value < 10) ? "0" + String.valueOf(value) : String.valueOf(value);
    }

    /**
     * 파일 생성시간을 파일명으로 변환하여 반환
     * @return
     */
    private String createFileName() {
        Date date = new Date();
        String fileName = String.valueOf(date.getTime());

        return fileName;
    }

    /**
     * 유효한(존재하는) 파일 여부
     * @param filename
     * @return
     */
    private boolean IsValidFile(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    /**
     * 팝업창 띄우기
     * @param requestCode
     */
    public void showActivity(int requestCode) {
        Log.d(TAG, "showActivity(" + requestCode + ")");

        Intent intent = null;
        switch(requestCode) {
            case BasicInfo.REQ_PHOTO_CAPTURE_ACTIVITY:
                intent = new Intent(getApplicationContext(), PhotoCaptureActivity.class);
                startActivityForResult(intent, requestCode);
                break;

            case BasicInfo.REQ_PHOTO_SELECTION_ACTIVITY:
                intent = new Intent(getApplicationContext(), PhotoSelectionActivity.class);
                startActivityForResult(intent, requestCode);
                break;

            case BasicInfo.REQ_VIDEO_RECORDING_ACTIVITY:
                intent = new Intent(getApplicationContext(), VideoRecordingActivity.class);
                startActivityForResult(intent, requestCode);
                break;

            case BasicInfo.REQ_VIDEO_LOADING_ACTIVITY:
                intent = new Intent(getApplicationContext(), VideoSelectionActivity.class);
                startActivityForResult(intent, requestCode);
                break;

            case BasicInfo.PLAY_VIDEO_ACTIVITY:
                if(mTmpVideoUri == null || mTmpVideoUri.equals("")) { Log.d(TAG, "mTmpVideoUri is null."); return; }
                intent = new Intent(getApplicationContext(), VideoPlayActivity.class);
                if(BasicInfo.isAbsoluteVideoPath(mTmpVideoUri)) {
                    intent.putExtra(BasicInfo.KEY_URI_VIDEO, BasicInfo.FOLDER_VIDEO + mTmpVideoUri);
                } else {
                    intent.putExtra(BasicInfo.KEY_URI_VIDEO, mTmpVideoUri);
                }
                startActivity(intent);
                break;

            case BasicInfo.REQ_VOICE_RECORDING_ACTIVITY:
                intent = new Intent(getApplicationContext(), VoiceRecordingActivity.class);
                startActivityForResult(intent, requestCode);
                break;

            case BasicInfo.PLAY_VOICE_ACTIVITY:
                intent = new Intent(getApplicationContext(), VoicePlayActivity.class);
                if (mTmpVoiceUri == null || mTmpVoiceUri.equals("")) {
                    if (mVoiceUri != null && !mVoiceUri.equals("")) {
                        intent.putExtra(BasicInfo.KEY_URI_VOICE, BasicInfo.FOLDER_VOICE + mVoiceUri);
                    }
                } else {
                    intent.putExtra(BasicInfo.KEY_URI_VOICE, BasicInfo.FOLDER_VOICE + mTmpVoiceUri);
                }
                startActivity(intent);

            default:
                break;
        }
    }

    /*****************************************************************************************
     *
     * 애니메이션 리스너
     *
     */
    private class SlidingPageAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    /****************************************************************************************/


}

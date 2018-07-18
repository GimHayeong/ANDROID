package org.hyg.seraph0.multinotepad;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by shiny on 2018-04-04.
 */



public class BasicInfo {
    public static String language = "";
    public static String ExternalPath = "/mnt/sdcard/";
    public static boolean ExternalChecked = false;

    public static String FOLDER_PHOTO 		    = "memo/photo/";
    public static String FOLDER_VIDEO 		    = "memo/video/";
    public static String FOLDER_VOICE 		    = "memo/voice/";
    public static String FOLDER_HANDWRITING 	= "memo/handwriting/";

    public static final String FILENAME_CAPTURED      = "captured";
    public static final String FILENAME_MADE          = "made";
    public static final String FILENAME_RECORDED      = "recorded";

    public static final String URI_MEDIA_FORMAT		= "content://media";

    public static String DATABASE_NAME = "memo/memo.db";

    public static final String KEY_MEMO_MODE = "MEMO_MODE";
    public static final String KEY_MEMO_TEXT = "MEMO_TEXT";
    public static final String KEY_MEMO_ID = "MEMO_ID";
    public static final String KEY_MEMO_DATE = "MEMO_DATE";
    public static final String KEY_ID_PHOTO = "ID_PHOTO";
    public static final String KEY_URI_PHOTO = "URI_PHOTO";
    public static final String KEY_ID_VIDEO = "ID_VIDEO";
    public static final String KEY_URI_VIDEO = "URI_VIDEO";
    public static final String KEY_ID_VOICE = "ID_VOICE";
    public static final String KEY_URI_VOICE = "URI_VOICE";
    public static final String KEY_ID_HANDWRITING = "ID_HANDWRITING";
    public static final String KEY_URI_HANDWRITING = "URI_HANDWRITING";


    public static final String MODE_INSERT = "MODE_INSERT";
    public static final String MODE_MODIFY = "MODE_MODIFY";
    public static final String MODE_VIEW = "MODE_VIEW";

    /**
     * Column name
     */
    public final static String COLUMN_URI = "URI";
    public final static String COLUMN_ID = "_id";
    public final static String COLUMN_MEMO_DATE = "INPUT_DATE";
    public final static String COLUMN_MEMO_TEXT = "CONTENT_TEXT";



    public static final int REQ_VIEW_ACTIVITY = 1001;
    public static final int REQ_INSERT_ACTIVITY = 1002;
    public static final int REQ_PHOTO_CAPTURE_ACTIVITY = 1501;
    public static final int REQ_PHOTO_SELECTION_ACTIVITY = 1502;
    public static final int REQ_VIDEO_RECORDING_ACTIVITY = 1503;
    public static final int REQ_VIDEO_LOADING_ACTIVITY = 1504;
    public static final int REQ_VOICE_RECORDING_ACTIVITY = 1505;
    public static final int REQ_HANDWRITING_MAKING_ACTIVITY = 1506;
    public static final int PLAY_VIDEO_ACTIVITY = 1601;
    public static final int PLAY_VOICE_ACTIVITY = 1602;


    public static SimpleDateFormat dateDayNameFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
    public static SimpleDateFormat dateDayFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat dateNameFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
    public static SimpleDateFormat dateNameFormat2 = new SimpleDateFormat("yyyy-MM-dd HH시 mm분");
    public static SimpleDateFormat dateNameFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat dateTimeNameFormat = new SimpleDateFormat("HH시 mm분");
    public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("HH:mm");


    public static final int WARNING_INSERT_SDCARD   = 1001;
    public static final int IMAGE_CANNOT_BE_STORED  = 1002;


    public static final int CONTENT_PHOTO           = 2001;
    public static final int CONTENT_VIDEO           = 2002;
    public static final int CONTENT_VOICE           = 2003;
    public static final int CONTENT_HANDWRITING     = 2004;
    public static final int CONTENT_PHOTO_EX        = 2005;
    public static final int CONTENT_VIDEO_EX        = 2006;
    public static final int CONTENT_VOICE_EX        = 2007;
    public static final int CONTENT_HANDWRITING_EX  = 2008;

    public static final int CONFIRM_DELETE          = 3001;
    public static final int CONFIRM_TEXT_INPUT      = 3002;


    /**
     * 동영상의 Uri 확인
     * @param videoUri : 동영상 Uri
     * @return : "content://media" 로 시작되는지 여부
     */
    public static boolean isAbsoluteVideoPath(String videoUri) {
        if (videoUri.startsWith(URI_MEDIA_FORMAT)) {
            return false;
        } else {
            return true;
        }
    }
}

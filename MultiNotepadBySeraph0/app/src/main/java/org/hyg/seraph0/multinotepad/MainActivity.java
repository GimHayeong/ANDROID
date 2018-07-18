package org.hyg.seraph0.multinotepad;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.hyg.seraph0.multinotepad.common.TitleBitmapButton;
import org.hyg.seraph0.multinotepad.db.MediaRecord;
import org.hyg.seraph0.multinotepad.db.MemoDatabase;
import org.hyg.seraph0.multinotepad.db.MemoRecord;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 메인
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "[MEMO-MAIN]";
    public static MemoDatabase mDatabase = null;

    private ListView mListView;
    private MemoListAdapter mListAdapter;

    private int mCnt = 0;

    private TextView mTxtItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        Locale locale = getResources().getConfiguration().locale;
        BasicInfo.language = locale.getLanguage();

        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { return; }

        String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        if(!BasicInfo.ExternalChecked && externalPath != null) {
            BasicInfo.ExternalPath = externalPath + File.separator;
            BasicInfo.FOLDER_PHOTO = BasicInfo.ExternalPath + BasicInfo.FOLDER_PHOTO;
            BasicInfo.FOLDER_VIDEO = BasicInfo.ExternalPath + BasicInfo.FOLDER_VIDEO;
            BasicInfo.FOLDER_VOICE = BasicInfo.ExternalPath + BasicInfo.FOLDER_VOICE;
            BasicInfo.FOLDER_HANDWRITING = BasicInfo.ExternalPath + BasicInfo.FOLDER_HANDWRITING;
            BasicInfo.DATABASE_NAME = BasicInfo.ExternalPath + BasicInfo.DATABASE_NAME;
            BasicInfo.ExternalChecked = true;
            Log.d(TAG, "completed to check external");
        }

        mListView = (ListView)findViewById(R.id.lstMemoList);
        mListAdapter = new MemoListAdapter(this);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setSelectedItemView(i);
            }
        });


        mTxtItemCount = (TextView)findViewById(R.id.txtItemCount);

        checkDangerousPermissions();

    }

    private void checkDangerousPermissions() {
        String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.CAMERA
                                , Manifest.permission.RECORD_AUDIO };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for(int i=0; i<permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if(permissionCheck == PackageManager.PERMISSION_DENIED) { break; }
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    /**
     * popup selected item view.
     * @param i : index
     */
    private void setSelectedItemView(int i) {
        MemoListItem item = (MemoListItem)mListAdapter.getItem(i);
        Intent intent;
        intent = new Intent(getApplicationContext(), InsertActivity.class);
        intent.putExtra(BasicInfo.KEY_MEMO_MODE, BasicInfo.MODE_VIEW);

        intent.putExtra(BasicInfo.KEY_MEMO_ID, item.getData().getId());
        intent.putExtra(BasicInfo.KEY_MEMO_DATE, item.getData().getDate());
        intent.putExtra(BasicInfo.KEY_MEMO_TEXT, item.getData().getMemoText());

        intent.putExtra(BasicInfo.KEY_ID_HANDWRITING, item.getData().getHandwritingId());
        intent.putExtra(BasicInfo.KEY_URI_HANDWRITING, item.getData().getHandwritingUri());

        intent.putExtra(BasicInfo.KEY_ID_PHOTO, item.getData().getPhotoId());
        intent.putExtra(BasicInfo.KEY_URI_PHOTO, item.getData().getPhotoUri());

        intent.putExtra(BasicInfo.KEY_ID_VIDEO, item.getData().getVideoId());
        intent.putExtra(BasicInfo.KEY_URI_VIDEO, item.getData().getVideoUri());

        intent.putExtra(BasicInfo.KEY_ID_VOICE, item.getData().getVoiceId());
        intent.putExtra(BasicInfo.KEY_URI_VOICE, item.getData().getVoiceUri());

        startActivityForResult(intent, BasicInfo.REQ_VIEW_ACTIVITY);
    }

    /**
     * popup blank insert view.
     * @param view
     */
    public void OnNewTitleButtonClick(View view) {

        try {
            Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
            intent.putExtra(BasicInfo.KEY_MEMO_MODE, BasicInfo.MODE_INSERT);
            startActivityForResult(intent, BasicInfo.REQ_INSERT_ACTIVITY);
        } catch (Exception ex) { Log.d(TAG, ex.getMessage()); }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case BasicInfo.REQ_INSERT_ACTIVITY:
                if(resultCode == RESULT_OK) {
                    loadListData();
                }
                break;

            case BasicInfo.REQ_VIEW_ACTIVITY:
                loadListData();
                break;
        }
    }

    /**
     *
     * @param view
     */
    public void OnCloseTitleButtonClick(View view) {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1) {
            for(int i=0; i<permissions.length; i++) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permission granted : " + permissions[i]);
                } else {
                    Log.d(TAG, "permission not granted: " + permissions[i]);
                }
            }
        }
    }

    @Override
    protected void onStart() {

        openDatabase();

        try {
            loadListData();
        }catch(Exception ex) { Log.d(TAG, "Exception loadListData: " + ex.getMessage()); }

        super.onStart();

    }

    private void openDatabase() {
        if(mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }

        mDatabase = MemoDatabase.getInstance(this);
        final boolean isOpen = mDatabase.open();
        if(isOpen) {
            Log.d(TAG, "Database is opened.");
        } else {
            Log.d(TAG, "Database is not opened.");
        }
    }

    private int loadListData() {
        List<MemoRecord> lst = null;
        int cnt = -1;

        if(mDatabase != null) {
            lst = mDatabase.selectAllMemo();
        }

        if(lst != null) {
            cnt = lst.size();
            mListAdapter.clear();
            for(int i=0; i<lst.size(); i++) {
                mListAdapter.addItem(new MemoListItem(lst.get(i)));
            }
            mListAdapter.notifyDataSetChanged();

            mTxtItemCount.setText(cnt + " " + getResources().getString(R.string.item_count));
            mTxtItemCount.invalidate();
        }

        return cnt;
    }

    /**
     * get uri
     * @param table : table name
     * @param id : id value
     *    String photoUri = getUri(MemoDatabase.TABLE_PHOTO, photoId);
     *    String videoUri = getUri(MemoDatabase.TABLE_VIDEO, videoId);
     *    String voiceUri = getUri(MemoDatabase.TABLE_VOICE, voiceId);
     *    String handwritingUri = getUri(MemoDatabase.TABLE_HANDWRITING, handwritingId);
     * @return : uri value
     */
    private String getUri(String table, int id) {
        MediaRecord record = null;
        if(id > 0) {
            if(mDatabase != null) {
                record = mDatabase.selectMediaById(table, id);
            }
        }

        return (record != null) ? record.getUri() : "";
    }

    @Override
    public void onDetachedFromWindow() {
        if(mDatabase != null) { mDatabase.close(); mDatabase = null; }
        super.onDetachedFromWindow();
    }
}

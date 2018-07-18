package org.hyg.intentbyseraph0;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class RssMainActivity extends AppCompatActivity {

    private final String TAG = "RSSMAIN";
    private static String rssUrl = "http://api.sbs.co.kr/xml/news/rss.jsp?pmDiv=entertainment";
    private ArrayList<RSSNewsItem> mNewsList;
    private RSSListView mView;
    private RSSListAdapter mAdapter;

    private ProgressDialog mProgressDialog;


    private EditText mEdtRssChannel;
    private Button mBtnGetRss;
    private Runnable mUpdateRSSRunnable = new Runnable() {
        @Override
        public void run() {
            Resources res = getResources();
            //Drawable icon = res.getDrawable(R.drawable.)
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_main);

        init();
    }

    private void init() {
        mView = new RSSListView(this);
        mAdapter = new RSSListAdapter(this);
        mView.setAdapter(mAdapter);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                                                                 , ViewGroup.LayoutParams.WRAP_CONTENT);

        mNewsList = new ArrayList<RSSNewsItem>();
        LinearLayout latRssList = (LinearLayout)findViewById(R.id.latRssList);
        //latRssList.addView(mView, params);

        mEdtRssChannel = (EditText)findViewById(R.id.edtRssChannel);
        mBtnGetRss = (Button)findViewById(R.id.btnGetRss);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String url = mEdtRssChannel.getText().toString();
                showRss(url);
            }
        };
        mBtnGetRss.setOnClickListener(OnButtonClick);
    }

    /**
     * 해당 RssUrl 의 데이터를 읽어온다.
     * @param url
     */
    private void showRss(String url){
        try{
            mProgressDialog = ProgressDialog.show(this
                                                , "RSS Refresh"
                                                , "RSS 정보 업데이트 중 ..."
                                                , true
                                                , true);

            RSSRefreshThread thread = new RSSRefreshThread(url);
            thread.start();
        } catch (Exception ex) { Log.e(TAG, "Error", ex); }
    }
}

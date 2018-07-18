package org.hyg.rssfeederbyseraph0;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RSSFeeder";
    private static String RSS_URL = "http://rss.joins.com/joins_star_list.xml";

    private ArrayList<RSSNewsItem> mNewsList;
    private RSSListView mListView;
    private RSSListAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    private EditText mEdtRssChannel;
    private Button mBtnGetRss;

    private Runnable mUpdateRSSRunnable = new Runnable() {
        @Override
        public void run() {
            Resources res = getResources();
            Drawable icon = res.getDrawable(R.drawable.weather1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mListView = new RSSListView(this);
        mAdapter = new RSSListAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnDataSelectionListener(new RSSListView.OnDataSelectionListener() {
            @Override
            public void onDataSelected(AdapterView parent, View view, int position, long id) {
                RSSNewsItem itm = (RSSNewsItem)mAdapter.getItem(position);
                String title = itm.getTitle();
            }
        });

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                                                                 , ViewGroup.LayoutParams.WRAP_CONTENT);

        mNewsList = new ArrayList<RSSNewsItem>();
        LinearLayout latMain = (LinearLayout)findViewById(R.id.latMain);
        latMain.addView(mListView, params);

        mEdtRssChannel = (EditText)findViewById(R.id.edtRssChannel);
        mBtnGetRss = (Button)findViewById(R.id.btnSearch);

        mEdtRssChannel.setText(RSS_URL);

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

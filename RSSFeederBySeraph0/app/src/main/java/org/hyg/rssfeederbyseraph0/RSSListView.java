package org.hyg.rssfeederbyseraph0;


import android.app.ListActivity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by shiny on 2018-03-27.
 * [선택 위젯] 리스트 뷰
 *  - ListView 에 필요한 속성이나 이벤트를 정의해서 사용할 때 ListView 를 상속받은 선택 위젯 클래스 정의
 *    (선택 위젯이므로 아답터 필요)
 */

public class RSSListView extends ListView {

    public static interface OnDataSelectionListener {
        void onDataSelected(AdapterView parent, View view, int position, long id);
    }

    public static OnDataSelectionListener mListener;
    public void setOnDataSelectionListener(OnDataSelectionListener listener){
        mListener = listener;
    }

    private Context mContext;
    private RSSListAdapter mAdapter;

    public RSSListView(Context context){
        super(context);
        mContext = context;
    }

    public void setAdapter(RSSListAdapter adapter) { mAdapter = adapter; }


}

package org.hyg.intentbyseraph0;


import android.app.ListActivity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by shiny on 2018-03-27.
 */

public class RSSListView extends ListActivity{

    private Context mContext;

    public RSSListView(Context context){
        mContext = context;
    }

    public void setAdapter(RSSListAdapter adapter) {
    }


}

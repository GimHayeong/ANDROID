package org.hyg.intentbyseraph0;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by shiny on 2018-03-27.
 */

public class RSSListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<RSSNewsItem> mNewsList;

    public RSSListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mNewsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}

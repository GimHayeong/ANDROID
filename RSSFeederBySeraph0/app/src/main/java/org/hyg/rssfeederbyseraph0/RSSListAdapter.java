package org.hyg.rssfeederbyseraph0;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiny on 2018-03-27.
 * [어댑터]
 *  - 선택 위젯 ListView 에서 사용할 어댑터
 */

public class RSSListAdapter extends BaseAdapter {

    private Context mContext;
    private List<RSSNewsItem> mNewsList = new ArrayList<RSSNewsItem>();
    public void setListItems(List<RSSNewsItem> list) { mNewsList = list; }

    public RSSListAdapter(Context context) {
        mContext = context;
    }

    /**
     * 리스트에 해당 기사 정보 추가
     * @param itm : <item>...</item> 태그의 내용
     */
    public void addItem(RSSNewsItem itm){
        mNewsList.add(itm);
    }

    /**
     * 전체 아이템 선택 여부
     * @return : 전체 아이템 선택 불가
     */
    public boolean areAllItemsSelectable() {
        return false;
    }

    /**
     * 해당 위치의 아이템 선택 여부
     * @param i : 인덱스
     * @return
     */
    public boolean isSelectable(int i){
        return true;
    }

    @Override
    public int getCount() {
        return mNewsList.size();
    }

    @Override
    public Object getItem(int i) {
        return mNewsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     *
     * @param i : 인덱스
     * @param view : 반환할 RSSListView
     *             (item_news.xml 에서 화면에 보유줄 레이아웃 설정하고 RSSNewsItemView 에서 각각의 RSSNewsItem 값을 처리)
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RSSNewsItemView itmView;

        if(view == null){
            itmView = new RSSNewsItemView(mContext, mNewsList.get(i));
        } else {
            itmView = (RSSNewsItemView)view;

            itmView.setIcon(mNewsList.get(i).getIcon());
            itmView.setText(0, mNewsList.get(i).getTitle());
            itmView.setText(0, mNewsList.get(i).getPubDate());
            itmView.setText(0, mNewsList.get(i).getCategory());
            itmView.setText(0, mNewsList.get(i).getDescription());
        }

        return itmView;
    }
}

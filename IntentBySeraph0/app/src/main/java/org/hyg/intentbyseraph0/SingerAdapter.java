package org.hyg.intentbyseraph0;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by shiny on 2018-03-16.
 * ListView 와 SingerItemView 의 모양으로 Item을 표현할 수 있게 연결하는 어댑터 클래스
 *  * 어댑터 : 위젯에 들어가는 데이터를 설정
 *  * getView() : 각 아이템이 표현되는 모양 설정
 */
public class SingerAdapter extends BaseAdapter {
    private ArrayList<SingerItem> mSingerList = new ArrayList<SingerItem>();
    private Context m_context;

    public SingerAdapter(Context context){
        m_context = context;
    }

    public void addItem(SingerItem itm){
        mSingerList.add(itm);
    }

    @Override
    public int getCount() {
        return mSingerList.size();
    }

    @Override
    public Object getItem(int i) {
        return mSingerList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * 각 아이템이 표현되는 모양 설정
     * @param i
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SingerItemView singerView = new SingerItemView(m_context);
        SingerItem itm = mSingerList.get(i);
        singerView.setName(itm.getName());
        singerView.setMobile(itm.getMobile());
        singerView.setAge(itm.getAge());
        singerView.setImage(itm.getResId());

        return singerView;
    }
}
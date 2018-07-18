package org.hyg.seraph0.multinotepad;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiny on 2018-04-04.
 * ListView 의 각 아이템을 사용자정의 뷰인 MemoListItemView 으로 변환할 수 있게 연결하는 어댑터 클래스
 */

public class MemoListAdapter extends BaseAdapter {

    private Context mContext;
    /**
     * MemoListItemView 의 MemoListItem 집합
     */
    private List<MemoListItem> mItems = new ArrayList<MemoListItem>();
    public void setItemList(List<MemoListItem> values) { mItems = values; }

    public MemoListAdapter(Context context){
        mContext = context;
    }

    /**
     * MemoListItemView 의 모든 아이템 제거
     */
    public void clear() {
        mItems.clear();
    }

    /**
     * ListView 에 MemoListItem 형의 데이터 추가
     * @param item
     */
    public void addItem(MemoListItem item) {
        mItems.add(item);
    }

    /**
     * ListView 의 전체 아이템 선택가능여부
     * @return
     */
    public boolean areAllItemSelectable() {
        return false;
    }

    /**
     * 현재 아이템의 선택가능여부
     * @param i
     * @return
     */
    public boolean isSelectable(int i) {
        try{
            return mItems.get(i).ismSelectable();
        } catch (IndexOutOfBoundsException ex) { return false; }
    }

    /**
     * ListView 의 아이템 갯수
     * @return
     */
    @Override
    public int getCount() {
        return mItems.size();
    }

    /**
     * ListView 에서 i 번째 MemoListItem 형 아이템 반환
     * @param i
     * @return
     */
    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    /**
     * ListView 에서 i번째 MemoListItem 의 고유 아이디(인덱스) 반환
     * @param i
     * @return
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * ListView 의 각 아이템의 뷰를 MemoListItemView 로 반환
     * @param i : ListView 의 아이템의 고유 인덱스
     * @param view : MemoListItemView 로 변환할 뷰
     * @param viewGroup
     * @return : MemoListItemView 형으로 변환된 아이템 뷰
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MemoListItemView itemView;

        if(view == null) {
            itemView = new MemoListItemView(mContext);
        } else {
            itemView = (MemoListItemView)view;
        }

        //::: setContents(index, value) index => 0: date, 1: text, 2: handwriting, 3: photo
        itemView.setContents(0, (mItems.get(i).getData().getDate()));
        itemView.setContents(1, (mItems.get(i).getData().getMemoText()));
        itemView.setContents(2, (mItems.get(i).getData().getHandwritingUri()));
        itemView.setContents(3, (mItems.get(i).getData().getPhotoUri()));
        itemView.setMediaContents(mItems.get(i).getData().getVideoUri()
                                , mItems.get(i).getData().getVoiceUri());



        return itemView;
    }
}

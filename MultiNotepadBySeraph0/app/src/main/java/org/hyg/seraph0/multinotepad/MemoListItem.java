package org.hyg.seraph0.multinotepad;

import org.hyg.seraph0.multinotepad.db.MediaRecord;
import org.hyg.seraph0.multinotepad.db.MemoRecord;

/**
 * Created by shiny on 2018-04-04.
 * ListView 의 각 아이템의 사용자 정의 데이터 클래스
 */

public class MemoListItem {
    /**
     * 날짜부터 소리까지의 10개의 데이터 저장
     */
    private MemoRecord mData;
    public MemoRecord getData() { return mData; }


    /**
     * 아이템 선택가능 여부
     */
    private boolean mSelectable = true;
    public boolean ismSelectable() { return mSelectable; }
    public void setSelectable(boolean value) { mSelectable = value; }


    public MemoListItem(MemoRecord data) {
        mData = data;
    }


    /**
     * 현재의 MemoListItem 과 비교대상 MemoListItem 의 날짜부터 음성데이터까지 모두 일치하는지 여부
     * @param item
     * @return : 모든 데이터가 일치하면 0, 아니면 -1
     */
    public int compareTo(MemoListItem item) {
        if(mData == null) { throw new IllegalArgumentException(); }

        MemoRecord itemData = item.getData();
        if(!mData.isEquals(itemData)) { return -1; }


        return 0;
    }

}

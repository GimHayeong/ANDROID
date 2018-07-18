package org.hyg.intentbyseraph0;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by shiny on 2018-03-19.
 */

public class MonthAdapter extends BaseAdapter {



    private Context mContext;
    private final int COLUMNS = 7;

    // 0 ~ mIdxFirstDay ~ mIdxLastDay ~ 41
    private MonthItem[] mMonthItems;

    private Calendar mCalendar;
    public int getYear() { return mCalendar.get(Calendar.YEAR); }
    public int getMonth() { return mCalendar.get(Calendar.MONTH); }

    // 이번달 시작 날짜의 배열 인덱스, 마지막 날짜의 배열 인덱스
    private int mIdxFirstDay, mIdxLastDay;
    // 이번달 마지막 날짜 (시작일은 무조건 1일이므로 변수 없음)
    private int mLastDay;

    private MonthItemView mSelectedItemView;
    private int mIdxSelectedDay = -1;
    public int getSelectedPosition() { return mIdxSelectedDay; }
    public void setSelectedPosition(int idx) {
        mIdxSelectedDay = idx;
        //Toast.makeText(mContext, "SELECTED INDEX: " + idx, Toast.LENGTH_LONG).show();
    }


    public MonthAdapter(Context context){
        mContext = context;

        init();
    }

    private void init() {
        mMonthItems = new MonthItem[7 * 6];
        mCalendar = Calendar.getInstance();

        recalculate();
        resetDayNumbers();
    }

    /**
     * 달력 초기화
     */
    private void recalculate() {
        // 현재 연도와 월, 요일
        int year, month, dayOfWeek;
        // 지난달의 마지막 일자
        int lastDayOfLastMonth;

        mCalendar.set(Calendar.DAY_OF_MONTH, 1);

        mIdxFirstDay = mCalendar.get(Calendar.DAY_OF_WEEK);

        mCalendar.add(Calendar.MONTH, -1);
        lastDayOfLastMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        mCalendar.add(Calendar.MONTH, 1);
        mLastDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        year = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH);
        mIdxLastDay = (mIdxFirstDay + mLastDay - 1);

        mSelectedItemView = null;
    }



    /**
     * 해당 월의 마지막 날짜(일수) 구하기
     * @param year
     * @param month
     * @return
     */
    private int getMonthLastDay(int year, int month){
        Calendar cld = Calendar.getInstance();
        cld.set(Calendar.YEAR, year);
        cld.set(Calendar.MONTH, month - 1);

        return cld.get(Calendar.DAY_OF_MONTH);
    }



    /**
     * 지정한 월의 일별 데이터 재계산
     */
    private void resetDayNumbers() {
        for(int i=0; i<42; i++){
            if(i < mIdxFirstDay - 1 || i > mIdxLastDay - 1){
                mMonthItems[i] = null;
            } else {
                mMonthItems[i] = new MonthItem(i, mIdxFirstDay);
            }
        }
    }



    /**
     * 이전달 이동
     */
    public void setPreviousMonth(){
        setMonth(-1);
    }




    /**
     * 다음달 이동
     */
    public void setNextMonth() {
        setMonth(1);
    }




    /**
     * 이전(다음)달 이동 처리
     * @param increment
     */
    private void setMonth(int increment){
        mCalendar.add(Calendar.MONTH, increment);

        recalculate();
        resetDayNumbers();

        mIdxSelectedDay = -1;
    }



    @Override
    public int getCount() {
        return mMonthItems.length;
    }

    @Override
    public Object getItem(int i) {
        return mMonthItems[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MonthItemView itmView;

        if(view == null){
            itmView = new MonthItemView(mContext);
        } else {
            itmView = (MonthItemView)view;
        }

        GridView.LayoutParams params = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, 150);

        int row = i / this.COLUMNS;
        int col = i % this.COLUMNS;

        itmView.setItem(mMonthItems[i]);
        itmView.setLayoutParams(params);
        itmView.setPadding(20, 10, 2, 2);
        itmView.setGravity(Gravity.LEFT);

        if(col == 0){
            itmView.setTextColor(Color.RED);
        } else if (col == 6) {
            itmView.setTextColor(Color.BLUE);
        } else {
            itmView.setTextColor(Color.BLACK);
        }

        if(i == getSelectedPosition()) {

            if (mSelectedItemView != null && mSelectedItemView != itmView) { mSelectedItemView.setBackgroundColor(Color.WHITE); }

            itmView.setBackgroundColor(Color.YELLOW);
            mSelectedItemView = itmView;

        } else {
            itmView.setBackgroundColor(Color.WHITE);
        }

        return itmView;
    }
}

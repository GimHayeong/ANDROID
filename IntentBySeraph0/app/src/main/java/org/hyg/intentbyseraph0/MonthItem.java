package org.hyg.intentbyseraph0;

/**
 * Created by shiny on 2018-03-19.
 */

public class MonthItem {

    private int mDayPos;
    private int mFirstDay;
    public String getDay() { return String.valueOf(mDayPos - mFirstDay + 2); }

    public MonthItem(int idx, int firstDay){
        mDayPos = idx;
        mFirstDay = firstDay;
    }
}

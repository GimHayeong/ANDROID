package org.hyg.intentbyseraph0;

import android.content.Context;

import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by shiny on 2018-03-16.
 * 사용자정의 위젯의 레이아웃인 picker_datetime.xml 의 최상위 레이아웃인 LinearLayout을 상속받는 클래스 정의
 */

public class DatetimePicker extends LinearLayout {



    public static interface OnDatetimeChangeListener {
        void onDatetimeChanged(DatetimePicker view, int year, int month, int day, int hour, int minute);
    }

    private OnDatetimeChangeListener mListener;
    private DatePicker mDate;
    private TimePicker mTime;
    private CheckBox mCkbIsVisible;

    public DatetimePicker(Context context) {
        super(context);

        init(context);
    }

    public DatetimePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.picker_datetime, this, true);

        Calendar calendar = Calendar.getInstance();
        final int curYear = calendar.get(calendar.YEAR);
        final int curMonth = calendar.get(Calendar.MONTH);
        final int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        final int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int curMinute = calendar.get(Calendar.MINUTE);

        mDate = (DatePicker)findViewById(R.id.dtpDate);
        mDate.init(curYear, curMonth, curDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                if(mListener != null){
                    mListener.onDatetimeChanged(DatetimePicker.this, year, monthOfYear, dayOfMonth, getHour(), getMinute());
                }
            }
        });

        mCkbIsVisible = (CheckBox)findViewById(R.id.ckbIsVisible);
        mCkbIsVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton ckbView, boolean isChecked) {
                mTime.setEnabled(isChecked);
                mTime.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
            }
        });

        mTime = (TimePicker)findViewById(R.id.tipTime);
        mTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                if(mListener != null){
                    mListener.onDatetimeChanged(DatetimePicker.this
                                               , mDate.getYear()
                                               , mDate.getMonth()
                                               , mDate.getDayOfMonth()
                                               , hourOfDay
                                               , minute);
                }
            }
        });

        this.setHour(curHour);
        this.setMinute(curMinute);
        mTime.setEnabled(mCkbIsVisible.isChecked());
        mTime.setVisibility(mCkbIsVisible.isChecked() ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 리스너 설정
     * @param listener
     */
    public void setOnDatetimeChangedListener(OnDatetimeChangeListener listener){
        mListener = listener;
    }

    /**
     * DatePicker 의 날짜와 시간 업데이트
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     * @param hour
     * @param minute
     */
    public void updateDatetime(int year, int monthOfYear, int dayOfMonth, int hour, int minute) {
        mDate.updateDate(year, monthOfYear, dayOfMonth);
        this.setHour(hour);
        this.setMinute(minute);
    }




    /**
     * 속성값을 반환 get 메서드
     * 안드로이드 API 버전에 따라 시간, 분 읽기 메서드 달리함
     */
    public int getYear() { return mDate.getYear(); }
    public int getMonth() { return mDate.getMonth(); }
    public int getDayOfMonth() { return mDate.getDayOfMonth(); }

    public int getHour() {
        int hour;
        if(Build.VERSION.SDK_INT >= 23){
            hour = mTime.getHour();
        } else {
            hour = mTime.getCurrentHour();
        }

        return hour;
    }

    public int getMinute(){
        int minute;
        if(Build.VERSION.SDK_INT >= 23){
            minute = mTime.getMinute();
        } else {
            minute = mTime.getCurrentMinute();
        }

        return minute;
    }




    /**
     * 속성값을 설정하는 set 메서드
     * 안드로이드 API 버전에 따라 시간, 분 설정 메서드 달리함
     * @param hourOfDay
     */
    private void setHour(int hourOfDay){
        if(Build.VERSION.SDK_INT >= 23){
            mTime.setHour(hourOfDay);
        } else {
            mTime.setCurrentHour(hourOfDay);
        }
    }

    private void setMinute(int minute){
        if(Build.VERSION.SDK_INT >= 23) {
            mTime.setMinute(minute);
        } else {
            mTime.setCurrentMinute(minute);
        }
    }
}

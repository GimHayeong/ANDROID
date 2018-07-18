package org.hyg.intentbyseraph0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatetimeMainActivity extends AppCompatActivity {

    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");

    private TextView mTxt;
    private DatetimePicker mPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datetime_main);

        init();
    }

    private void init() {
        mTxt = (TextView)findViewById(R.id.txtTimeMsg);
        mPicker = (DatetimePicker)findViewById(R.id.cwPicker);

        mPicker.setOnDatetimeChangedListener(new DatetimePicker.OnDatetimeChangeListener() {
            @Override
            public void onDatetimeChanged(DatetimePicker view, int year, int month, int day, int hour, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hour, minute);
                //mPicker.updateDatetime(year, month, day, hour, minute);

                mTxt.setText(mDateFormat.format(calendar.getTime()));
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.set(mPicker.getYear()
                   , mPicker.getMonth()
                   , mPicker.getDayOfMonth()
                   , mPicker.getHour()
                   , mPicker.getMinute());
        mTxt.setText(mDateFormat.format(calendar.getTime()));
    }
}

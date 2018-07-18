package org.hyg.intentbyseraph0;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarMainActivity extends AppCompatActivity {

    private GridView mGrdMonth;
    private MonthAdapter mAdtMonth;
    private TextView mTxtMonth;

    private int mCurYear, mCurMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_main);

        init();
    }

    private void init() {
        mGrdMonth = (GridView)findViewById(R.id.grdMonth);
        mAdtMonth = new MonthAdapter(this);
        mGrdMonth.setAdapter(mAdtMonth);

        // TODO: setOnDataSelectionListener ==> setOnItemClickListener 로 설정
        mGrdMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MonthItem itm = (MonthItem)mAdtMonth.getItem(i);
                if(itm != null){
                    mAdtMonth.setSelectedPosition(i);
                    mAdtMonth.getView(i, view, adapterView);
                    /**
                    Toast.makeText(getApplicationContext(),
                                  mCurYear + " 년 " + (mCurMonth + 1) + " 월 " + itm.getDay() + " 일",
                                   Toast.LENGTH_LONG)
                                  .show();**/
                }
            }
        });

        mTxtMonth = (TextView)findViewById(R.id.txtMonth);
        setMonthText();

        ((Button)findViewById(R.id.btnPreviousMonth)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdtMonth.setPreviousMonth();
                mAdtMonth.notifyDataSetChanged();

                setMonthText();
            }
        });

        ((Button)findViewById(R.id.btnNextMonth)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mAdtMonth.setNextMonth();
                mAdtMonth.notifyDataSetChanged();

                setMonthText();
            }
        });
    }

    /**
     * 달력의 년 월 표시
     */
    private void setMonthText() {
        mCurYear = mAdtMonth.getYear();
        mCurMonth = mAdtMonth.getMonth();

        mTxtMonth.setText(mCurYear + " 년 " + (mCurMonth + 1) + " 월");
    }
}

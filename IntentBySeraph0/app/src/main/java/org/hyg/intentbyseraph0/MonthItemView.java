package org.hyg.intentbyseraph0;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by shiny on 2018-03-19.
 * LinearLayout을 상속받은 클래스 : item_month.xml 의 루트는 LinearLayout
 *  : GridView 의 아이템을 어떻게 보여줄지를 결정
 */

public class MonthItemView extends LinearLayout{

    private TextView mTxtDayNum;

    public MonthItemView(Context context){
        super(context);
        init(context);
    }

    public MonthItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.item_month, this, true);

        mTxtDayNum = (TextView)findViewById(R.id.txtDayNum);
    }



    public void setItem(MonthItem itm) {
        if (itm != null) {
            mTxtDayNum.setText(itm.getDay());
        } else {
            mTxtDayNum.setText("");
        }
    }


    /**
     * 날짜 색상 지정
     * @param color
     */
    public void setTextColor(int color){
        mTxtDayNum.setTextColor(color);
    }
}

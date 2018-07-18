package org.hyg.paintboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by shiny on 2018-03-21.
 * ColorPlatteDialog 의 색상그리드의 각 아이템을 생성
 */

public class PenDataAdapter extends BaseAdapter {
    private static int[] mPens = new int[] {
            2, 4, 8, 10, 12, 14, 16
            , 18, 20, 22, 24, 26, 28, 30
            , 40, 50, 60, 70, 80, 90, 100
    };


    private Context mContext;
    private final int MAX_COL = 7;
    private final int MAX_ROW = 3;
    public int getColumns() { return MAX_COL; }
    public int getRows() { return MAX_ROW; }

    public PenDataAdapter(Context context){
        super();

        mContext = context;
    }

    @Override
    public int getCount() {
        return mPens.length;
    }

    @Override
    public Object getItem(int i) {
        return mPens[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     *
     *  : GridView 가 전체 GridView의 높이를 계산하는 시점이 첫번째 행 높이를 기준으로 하는 것 같다.
     *    => 전체 GridView 의 높이를 행의 높이와 여백을 감안하여 GridView 의 높이로 강제 설정.
     * @param i
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        int row = i / MAX_COL;
        int col = i % MAX_COL;

        GridView.LayoutParams params;

        params  = new GridView.LayoutParams(100, row == 2 ? 110 : 40);
        LinearLayout lat = new LinearLayout(mContext);
        lat.setLayoutParams(params);
        lat.setOrientation(LinearLayout.VERTICAL);
        lat.setGravity(Gravity.CENTER_VERTICAL);
        lat.setBackgroundColor(Color.LTGRAY);
        lat.setTag(mPens[i]);

        params = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT
                                         , GridView.LayoutParams.WRAP_CONTENT);
        Button btnPenCell = new Button(mContext);
        btnPenCell.setText("");
        btnPenCell.setLayoutParams(params);
        btnPenCell.setPadding(4, 4, 4,4);
        btnPenCell.setBackgroundColor(Color.BLACK);
        btnPenCell.setTag(mPens[i]);
        btnPenCell.setHeight(mPens[i]);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PenDialog.SelectedListener != null){
                    PenDialog.SelectedListener.onPenSelected(((Integer)view.getTag()).intValue());
                }

                ((PenDialog)mContext).finish();
            }
        };
        if(row == 0){
            lat.setOnClickListener(OnButtonClick);
        } else {
            btnPenCell.setOnClickListener(OnButtonClick);
        }

        lat.addView(btnPenCell);
        return lat;
    }
}

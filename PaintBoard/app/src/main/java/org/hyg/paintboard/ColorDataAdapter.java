package org.hyg.paintboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

/**
 * Created by shiny on 2018-03-21.
 * ColorPlatteDialog 의 색상그리드의 각 아이템을 생성
 */

public class ColorDataAdapter extends BaseAdapter {

    private static int[] mColors = new int[] {
            0xff000000, 0xff00007f, 0xff0000ff, 0xff007f00, 0xff007f7f, 0xff00ff00, 0xff00ff7f
            , 0xff00ffff, 0xff7f007f, 0xff7f00ff, 0xff7f7f00, 0xff7f7f7f, 0xffff0000, 0xffff007f
            , 0xffff00ff, 0xffff7f00, 0xffff7f7f, 0xffff7fff, 0xffffff00, 0xffffff7f, 0xffffffff
    };


    private Context mContext;
    private final int MAX_COL = 7;
    private final int MAX_ROW = 3;
    public int getColumns() { return MAX_COL; }
    public int getRows() { return MAX_ROW; }

    public ColorDataAdapter(Context context){
        super();

        mContext = context;
    }

    @Override
    public int getCount() {
        return mColors.length;
    }

    @Override
    public Object getItem(int i) {
        return mColors[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        int row = i / MAX_ROW;
        int col = i % MAX_ROW;

        GridView.LayoutParams params = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT
                                                                , GridView.LayoutParams.MATCH_PARENT);

        Button btnColorCell = new Button(mContext);
        btnColorCell.setText("");
        btnColorCell.setLayoutParams(params);
        btnColorCell.setPadding(4, 4, 4, 4);
        btnColorCell.setBackgroundColor(mColors[i]);
        btnColorCell.setTag(mColors[i]);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ColorPaletteDialog.SelectedListener != null){
                    ColorPaletteDialog.SelectedListener.onColorSelected(((Integer)view.getTag()).intValue());
                }

                ((ColorPaletteDialog)mContext).finish();
            }
        };
        btnColorCell.setOnClickListener(OnButtonClick);

        return btnColorCell;
    }
}

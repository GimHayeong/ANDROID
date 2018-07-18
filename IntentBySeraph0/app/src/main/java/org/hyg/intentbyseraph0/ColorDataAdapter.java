package org.hyg.intentbyseraph0;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

/**
 * Created by shiny on 2018-03-21.
 */

/**
 * 색상표 컬렉션의 각 아이템의 데이터와 아이템의 뷰와 이벤트를 정의하여
 * 색상표 컬렉션을 포함하고 있는 뷰(ColorPaletteDialog)로
 * 반환하는 데이터어댑터 클래스
 */
public class ColorDataAdapter extends BaseAdapter {
    /**
     * 색상표 컬렉션의 아이템 데이터 배열
     */
    public static final int[] mColors = new int[] {
            0xff000000, 0xff00007f, 0xff0000ff, 0xff007f00, 0xff007f7f, 0xff00ff00, 0xff00ff7f
            , 0xff00ffff, 0xff7f007f, 0xff7f00ff, 0xff7f7f00, 0xff7f7f7f, 0xffff0000, 0xffff007f
            , 0xffff00ff, 0xffff7f00, 0xffff7f7f, 0xffff7fff, 0xffffff00, 0xffffff7f, 0xffffffff
    };

    private Context mContext;
    private final int MAX_ROW = 3;
    private final int MAX_COL = 7;

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

        Button btn = new Button(mContext);
        btn.setText("");
        btn.setLayoutParams(params);
        btn.setPadding(4, 4, 4, 4);
        btn.setBackgroundColor(mColors[i]);
        btn.setHeight(25);
        btn.setTag(mColors[i]);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ColorPaletteDialog.mListener != null){
                    ColorPaletteDialog.mListener.onColorSelected(((Integer)view.getTag()).intValue());
                }

                ((ColorPaletteDialog)mContext).finish();
            }
        });


        return btn;
    }
}

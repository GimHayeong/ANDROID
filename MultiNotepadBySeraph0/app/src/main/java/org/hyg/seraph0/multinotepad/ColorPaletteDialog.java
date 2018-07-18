package org.hyg.seraph0.multinotepad;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import org.hyg.seraph0.multinotepad.common.TitleBitmapButton;

/**
 * Created by shiny on 2018-04-08.
 */

public class ColorPaletteDialog extends Activity {
    private GridView mGrid;
    TitleBitmapButton mBtnClose;
    ColorDataAdapter mAdapter;
    public static OnColorSelectedListener mSelectedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_handwriting);
        setTitle(R.string.color_selection_title);

        init();
    }

    private void init() {
        mGrid = (GridView)findViewById(R.id.grdColor);
        mBtnClose = (TitleBitmapButton)findViewById(R.id.btnClose);

        mGrid.setColumnWidth(50);
        mGrid.setMinimumHeight(50);
        mGrid.setBackgroundColor(Color.GRAY);
        mGrid.setVerticalSpacing(4);
        mGrid.setHorizontalSpacing(4);

        mAdapter = new ColorDataAdapter(this);
        mGrid.setAdapter(mAdapter);
        mGrid.setNumColumns(mAdapter.getColumns());

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}

/**
 *
 */
class ColorDataAdapter extends BaseAdapter{

    public static final int[] COLOR_ITEMS = new int[] {
            0xff000000,0xff00007f,0xff0000ff,0xff007f00,0xff007f7f,0xff00ff00,0xff00ff7f,
            0xff00ffff,0xff7f007f,0xff7f00ff,0xff7f7f00,0xff7f7f7f,0xffff0000,0xffff007f,
            0xffff00ff,0xffff7f00,0xffff7f7f,0xffff7fff,0xffffff00,0xffffff7f,0xffffffff
    };

    private Context mContext;
    private int mRows, mCols;
    public int getColumns() {
        return mCols;
    }


    public ColorDataAdapter(Context context){
        super();

        mContext = context;

        mRows = 3;
        mCols = 7;
    }

    @Override
    public int getCount() {
        return mRows * mCols;
    }

    @Override
    public Object getItem(int i) {
        return COLOR_ITEMS[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        int row = i / mRows;
        int col = i % mRows;

        GridView.LayoutParams params = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT
                                                                , GridView.LayoutParams.MATCH_PARENT);

        TitleBitmapButton item = new TitleBitmapButton(mContext);
        item.setText("");
        item.setLayoutParams(params);
        item.setPadding(4, 4, 4, 4);
        item.setBackgroundColor(COLOR_ITEMS[i]);
        item.setHeight(50);
        item.setTag(COLOR_ITEMS[i]);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ColorPaletteDialog.mSelectedListener != null) {
                    ColorPaletteDialog.mSelectedListener.onColorSelected(((Integer)view.getTag()).intValue());
                }

                ((ColorPaletteDialog)mContext).finish();
            }
        });

        return item;
    }
}

/**
 *
 */
interface OnColorSelectedListener {
    public void onColorSelected(int color);
}

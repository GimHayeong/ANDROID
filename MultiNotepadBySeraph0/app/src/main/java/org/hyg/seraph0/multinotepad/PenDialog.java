package org.hyg.seraph0.multinotepad;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
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

public class PenDialog extends Activity {

    private GridView mGrid;
    private TitleBitmapButton mBtnClose;
    private PenDataAdapter mAdapter;
    public static OnPenSelectedListener mSelectedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_handwriting);

        setTitle(R.string.pen_selection_title);

        this.setFinishOnTouchOutside(false);

        init();
    }

    private void init() {
        mGrid = (GridView)findViewById(R.id.grdColor);
        mBtnClose = (TitleBitmapButton)findViewById(R.id.btnClose);

        mGrid.setColumnWidth(12);
        mGrid.setBackgroundColor(Color.GRAY);
        mGrid.setVerticalSpacing(4);
        mGrid.setHorizontalSpacing(4);

        mAdapter = new PenDataAdapter(this);
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


class PenDataAdapter extends BaseAdapter{

    public static final int[] PEN_ITEMS = new int[] {
            1, 2, 3, 4, 5
            , 6, 7, 8, 9, 10
            , 11, 13, 15, 17, 20
    };

    private Context mContext;
    private int mRows, mCols;
    public int getColumns() { return mCols; }

    public PenDataAdapter(Context context){
        super();

        mContext = context;
        mRows = 3;
        mCols = 5;
    }

    @Override
    public int getCount() {
        return mRows * mCols;
    }

    @Override
    public Object getItem(int i) {
        return PEN_ITEMS[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final int row = i / mRows;
        final int col = i % mRows;

        GridView.LayoutParams params = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT
                                                                , GridView.LayoutParams.MATCH_PARENT);

        final BitmapDrawable pen = new BitmapDrawable(mContext.getResources(), getBitmapDrawable(i));

        TitleBitmapButton item = new TitleBitmapButton(mContext);
        item.setText("");
        item.setLayoutParams(params);
        item.setPadding(4, 4, 4, 4);
        item.setBackgroundDrawable(pen);
        item.setHeight(120);
        item.setTag(PEN_ITEMS[i]);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PenDialog.mSelectedListener != null) {
                    PenDialog.mSelectedListener.onPenSelected(((Integer)view.getTag()).intValue());
                }

                ((PenDialog)mContext).finish();
            }
        });

        return item;
    }

    private Bitmap getBitmapDrawable(int i) {
        final int width = 10;
        final int height = 20;

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bmp);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, width, height, paint);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth((float)PEN_ITEMS[i]);
        canvas.drawLine(0, height / 2, width - 1, height / 2, paint);

        return bmp;
    }
}

/**
 *
 */
interface OnPenSelectedListener {
    public void onPenSelected(int pen);
}

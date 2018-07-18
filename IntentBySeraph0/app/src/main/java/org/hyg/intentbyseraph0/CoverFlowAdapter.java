package org.hyg.intentbyseraph0;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.FileInputStream;

/**
 * Created by shiny on 2018-03-23.
 */

public class CoverFlowAdapter extends BaseAdapter {

    private Context mContext;
    private int[] mImgRes = new int[8];
    private int mBgItem;
    private FileInputStream mStream;
    private ImageView[] mImgViews;

    public CoverFlowAdapter(Context context){
        mContext = context;

        init();
    }

    private void init() {
        for(int i=0; i<mImgRes.length; i++){
            mImgRes[i] = R.drawable.photo1 + i;
        }

        mImgViews = new ImageView[mImgRes.length];
    }

    @Override
    public int getCount() {
        return mImgRes.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imgView;

        if(view == null) {
            imgView = new ImageView(mContext);
        } else {
            imgView = (ImageView)view;
        }

        imgView.setImageResource(mImgRes[i]);
        imgView.setLayoutParams(new CoverFlow.LayoutParams(300, 140));
        imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        BitmapDrawable drawable = (BitmapDrawable)imgView.getDrawable();
        drawable.setAntiAlias(true);

        return imgView;
    }

    public float getScale(boolean focused, int offset){
        return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset)));
    }
}
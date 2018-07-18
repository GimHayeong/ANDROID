package org.hyg.intentbyseraph0;

import android.content.Context;
import android.graphics.Matrix;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by shiny on 2018-03-23.
 */

public class SampleScaleType {


    private Context mContext;
    private LinearLayout mContainer;

    public SampleScaleType(Context context, LinearLayout layout) {
        mContext = context;
        mContainer = layout;
    }


    /**
     * GridView 에 아이템을 추가할 때, 어댑터를 연결해 추가
     */
    private void sampleImageScaleTypeByGridView() {
        //:: mContainer = (LinearLayout)findViewById(R.id.layContainer);

        GridView grdContainer = new GridView(mContext);
        grdContainer.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
        grdContainer.setColumnWidth(420);
        //:: grdContainer.setNumColumns(GridView.AUTO_FIT);
        grdContainer.setNumColumns(2);
        grdContainer.setVerticalSpacing(10);
        grdContainer.setHorizontalSpacing(10);
        grdContainer.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        grdContainer.setGravity(Gravity.CENTER);
        grdContainer.setAdapter(new ImageAdapter(mContext));

        mContainer.addView(grdContainer);
    }

    private void sampleImageScaleTypeLinearLayout() {
        final ImageView[] imgViews = new ImageView[8];
        final ImageView.ScaleType[] types = {
                ImageView.ScaleType.CENTER, ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.CENTER_INSIDE
                , ImageView.ScaleType.FIT_CENTER, ImageView.ScaleType.FIT_END, ImageView.ScaleType.FIT_START
                , ImageView.ScaleType.FIT_XY, ImageView.ScaleType.MATRIX
        };

        //:: mContainer = (LinearLayout)findViewById(R.id.layContainer);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(400, 400);

        LinearLayout layout = getLayout();
        for(int i=0; i<types.length; i++){
            imgViews[i] = new ImageView(mContext);
            imgViews[i].setImageResource(R.drawable.photo2);

            if (i == 7){
                Matrix matrix = new Matrix();
                matrix.postRotate(45.0f);
                imgViews[i].setImageMatrix(matrix);
            }

            imgViews[i].setScaleType(types[i]);
            imgViews[i].setPadding(8, 8, 8, 8);
            layout.addView(imgViews[i], params);

            if (i % 2 == 1 ){
                layout = getLayout();
            }
        }
    }

    private LinearLayout getLayout() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout layout = new LinearLayout(mContext);;
        layout.setOrientation(LinearLayout.HORIZONTAL);
        mContainer.addView(layout, params);

        return layout;
    }

    /**
     * GridView 에 추가할 아이템
     */
    private class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private int[] mPhotos = {
                R.drawable.photo2, R.drawable.photo2, R.drawable.photo2, R.drawable.photo2
                , R.drawable.photo2, R.drawable.photo2, R.drawable.photo2, R.drawable.photo2
        };
        final ImageView.ScaleType[] mTypes = {
                ImageView.ScaleType.CENTER, ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.CENTER_INSIDE
                , ImageView.ScaleType.FIT_CENTER, ImageView.ScaleType.FIT_END, ImageView.ScaleType.FIT_START
                , ImageView.ScaleType.FIT_XY, ImageView.ScaleType.MATRIX
        };

        public ImageAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mPhotos.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
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
                imgView.setLayoutParams(new GridView.LayoutParams(400, 400));
            } else {
                imgView = (ImageView)view;
            }

            if (i == 7){
                Matrix matrix = new Matrix();
                matrix.postRotate(45.0f);
                imgView.setImageMatrix(matrix);
            }

            imgView.setScaleType(mTypes[i]);
            imgView.setPadding(8, 8, 8, 8);

            imgView.setImageResource(mPhotos[i]);
            return imgView;
        }
    }
}

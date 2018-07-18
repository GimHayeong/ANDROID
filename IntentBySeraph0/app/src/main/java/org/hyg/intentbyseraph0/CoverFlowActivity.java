package org.hyg.intentbyseraph0;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

public class CoverFlowActivity extends AppCompatActivity {

    private LinearLayout mContainer;
    public final static int SPACING = -45;
    public final static int ANI_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_flow);

        initCoverFlow();
    }

    private void initCoverFlow() {
        CoverFlow cfCoverFlow = (CoverFlow)findViewById(R.id.cfCoverFlow);

        CoverFlowAdapter imgAdapter = new CoverFlowAdapter(this);
        cfCoverFlow.setAdapter(imgAdapter);

        cfCoverFlow.setSpacing(SPACING);
        cfCoverFlow.setSelection(2, true);
        cfCoverFlow.setAnimationDuration(ANI_DURATION);
    }





}





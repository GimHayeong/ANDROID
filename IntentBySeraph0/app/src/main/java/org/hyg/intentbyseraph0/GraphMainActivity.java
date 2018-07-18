package org.hyg.intentbyseraph0;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GraphMainActivity extends AppCompatActivity {

    private Animation mAnimGrow;
    private LinearLayout mLatGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_main);

        initAnim();
        initGraph();
    }

    private void initAnim() {
        Resources res = getResources();

        mAnimGrow = AnimationUtils.loadAnimation(this, R.anim.graph_grow);
    }

    private void initGraph(){
        mLatGraph = (LinearLayout)findViewById(R.id.latGraph);

        addItem("Apple", 80);
        addItem("Orange", 95);
        addItem("Kiwi", 40);
    }

    private void addItem(String name, int value) {
        LinearLayout latItem = new LinearLayout(this);
        latItem.setOrientation(LinearLayout.HORIZONTAL);
        latItem.setPadding(25, 5, 25, 5);

        LinearLayout.LayoutParams params;

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                             , LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView txtName = new TextView(this);
        txtName.setText(name);
        params.width = 180;
        params.setMargins(0, 4, 0, 4);
        latItem.addView(txtName, params);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                             , LinearLayout.LayoutParams.WRAP_CONTENT);
        ProgressBar barValue = new ProgressBar(this
                                             , null
                                             , android.R.attr.progressBarStyleHorizontal);
        barValue.setIndeterminate(false);
        barValue.setMax(100);
        barValue.setAnimation(mAnimGrow);
        //:: [1] Progress(값)
        barValue.setProgress(value);
        params.weight = 1;
        //:: [2] Progress(100) 에 최대값 주고 with 속성으로 값(value * 3) 표시
        //barValue.setProgress(100);
        //params.width = value * 3;
        params.height = 80;
        params.gravity = Gravity.LEFT;
        latItem.addView(barValue, params);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                             , LinearLayout.LayoutParams.WRAP_CONTENT);
        mLatGraph.addView(latItem, params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){
            mAnimGrow.start();
        } else {
            mAnimGrow.reset();
        }
    }
}

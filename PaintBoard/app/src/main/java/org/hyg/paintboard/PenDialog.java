package org.hyg.paintboard;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;


/**
 * Activity 를 상속받는 Dialog BOX
 * (manifest 에 Dialog 테마 적용해야 함)
 */
public class PenDialog extends Activity {

    public static OnPenSelectedListener SelectedListener;
    public void setOnPenSelectedListener(OnPenSelectedListener listener) {
        SelectedListener = listener;
    }

    private GridView mGrdPalette;
    private Button mBtnClose;
    private PenDataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pen);

        this.setFinishOnTouchOutside(false);

        init();
    }

    private void init() {
        mGrdPalette = (GridView)findViewById(R.id.grdPalette);
        mBtnClose = (Button)findViewById(R.id.btnClose);

        mGrdPalette.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 230));
        mGrdPalette.setColumnWidth(50);
        mGrdPalette.setBackgroundColor(Color.GRAY);
        mGrdPalette.setVerticalSpacing(4);
        mGrdPalette.setHorizontalSpacing(4);

        mAdapter = new PenDataAdapter(this);
        mGrdPalette.setAdapter(mAdapter);
        mGrdPalette.setNumColumns(mAdapter.getColumns());

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };
        mBtnClose.setOnClickListener(OnButtonClick);

    }
}

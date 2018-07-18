package org.hyg.paintboard;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;

/**
 * Activity 를 상속받는 Dialog BOX
 * (manifest 에 Dialog 테마 적용해야 함)
 */
public class ColorPaletteDialog extends Activity {

    public static OnColorSelectedListener SelectedListener;


    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        SelectedListener = listener;
    }

    private GridView mGrdPalette;
    private Button mBtnClose;
    private ColorDataAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_color_palette);
        this.setFinishOnTouchOutside(false);


        init();
    }

    private void init() {
        mGrdPalette = (GridView)findViewById(R.id.grdPalette);
        mBtnClose = (Button)findViewById(R.id.btnClose);

        mGrdPalette.setColumnWidth(50);
        mGrdPalette.setMinimumHeight(50);
        mGrdPalette.setBackgroundColor(Color.GRAY);
        mGrdPalette.setVerticalSpacing(4);
        mGrdPalette.setHorizontalSpacing(4);

        mAdapter = new ColorDataAdapter(this);
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

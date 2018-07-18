package org.hyg.intentbyseraph0;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import static android.graphics.Color.*;

public class ColorPaletteDialog extends AppCompatActivity {

    public static interface OnColorSelectedListener {
        void onColorSelected(int color);
    }

    public static OnColorSelectedListener mListener;
    public void setOnColorSelectedListener(OnColorSelectedListener listener){
        mListener = listener;
    }


    private GridView mGrdPalette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_palette_dialog);

        initPalette();
    }

    private void initPalette() {
        mGrdPalette = (GridView)findViewById(R.id.grdPalette);
        mGrdPalette.setColumnWidth(14);
        mGrdPalette.setBackgroundColor(Color.GRAY);
        mGrdPalette.setVerticalSpacing(4);
        mGrdPalette.setHorizontalSpacing(4);
    }
}

package org.hyg.intentbyseraph0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PaintboardMainActivity extends AppCompatActivity {

    private Button mBtnColor, mBtnPen, mBtnEraser, mBtnUndo;
    private int mColor;
    private PaintBoardView mBoard;
    private float mSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paintboard_main);


        initButton();
    }

    private void initButton() {
        View.OnClickListener onButtonClick = new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btnColor:
                        /*ColorPaletteDialog.mListener = new ColorPaletteDialog.OnColorSelectedListener(){

                            @Override
                            public void onColorSelected(int color) {
                                mColor = color;
                                //mBoard.setPaint(mColor, mSize);
                            }
                        };*/

                        try {
                            Intent intent = new Intent(getApplicationContext()
                                    , ColorPaletteDialog.class);
                            startActivity(intent);
                        }catch (Exception ex){
                            Log.d("Paint", ex.getMessage());
                        }
                        break;

                    case R.id.btnPen:
                        break;

                    case R.id.btnEraser:
                        break;

                    case R.id.btnUndo:
                        break;
                }
            }
        };
        ((Button)findViewById(R.id.btnColor)).setOnClickListener(onButtonClick);


    }


}

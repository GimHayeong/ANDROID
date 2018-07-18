package org.hyg.paintboard;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.concurrent.Callable;

public class BestPaintBoardActivity extends AppCompatActivity {

    private BestPaintBoard mBoard;
    private Button mBtnColor, mBtnPen, mBtnEraser, mBtnUndo;
    private int mColor = 0xff000000, mOldColor;
    private int mSize = 2, mOldSize;
    private boolean mIsSelectedEraser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_paint_board);

        init();
    }

    private void init() {
        LinearLayout latBoard = (LinearLayout)findViewById(R.id.latBoard);
        mBtnColor = (Button)findViewById(R.id.btnColor);
        mBtnPen = (Button)findViewById(R.id.btnPen);
        mBtnEraser = (Button)findViewById(R.id.btnEraser);
        mBtnUndo = (Button)findViewById(R.id.btnUndo);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.MATCH_PARENT);

        mBoard = new BestPaintBoard(this);
        mBoard.setLayoutParams(params);
        mBoard.setPadding(2, 2, 2, 2);

        latBoard.addView(mBoard);


        View.OnClickListener OnButtonClick = new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent;
                switch(view.getId()){
                    case R.id.btnColor:
                        ColorPaletteDialog.SelectedListener = new OnColorSelectedListener() {
                            public void onColorSelected(int color) {
                                mColor = color;
                                mBoard.setPaint(mColor, mSize);
                            }
                        };

                        intent = new Intent(getApplicationContext(), ColorPaletteDialog.class);
                        startActivity(intent);
                        break;

                    case R.id.btnPen:
                        PenDialog.SelectedListener = new OnPenSelectedListener() {
                            @Override
                            public void onPenSelected(int size) {
                                mSize = size;
                                mBoard.setPaint(mColor, mSize);
                            }
                        };

                        intent = new Intent(getApplicationContext(), PenDialog.class);
                        startActivity(intent);
                        break;

                    case R.id.btnEraser:
                        mColor = Color.WHITE;
                        mBoard.setPaint(mColor, mSize);
                        break;

                    case R.id.btnUndo:
                        mBoard.undo();
                        break;
                }
            }
        };
        mBtnColor.setOnClickListener(OnButtonClick);
        mBtnPen.setOnClickListener(OnButtonClick);
        mBtnEraser.setOnClickListener(OnButtonClick);
        mBtnUndo.setOnClickListener(OnButtonClick);
    }
}

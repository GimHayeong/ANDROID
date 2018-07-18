package org.hyg.seraph0.multinotepad;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.hyg.seraph0.multinotepad.common.TitleBitmapButton;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 손글씨 쓰기 / 삭제
 */
public class HandwritingMakingActivity extends AppCompatActivity {

    private static final String TAG = "[MEMO-HANDWRITING]";

    private HandwritingView mBoard;
    private TitleBitmapButton mBtnColor, mBtnPen, mBtnEraser, mBtnUndo, mBtnSaveHandwriting;

    private LinearLayout mLatPaintInfo;
    private TitleBitmapButton mBtnColorLegend;
    private TextView mTxtSizeLegend;
    private void displayPaintProperty() {
        mBtnColorLegend.setBackgroundColor(mColor);
        mTxtSizeLegend.setText("Size : " + mSize);
        mTxtSizeLegend.setTextColor(mColor);

        mLatPaintInfo.invalidate();
    }

    private int mColor = 0xff000000, mSize = 2, mOldColor, mOldSize;
    public int getSelectedColor() { return mColor; }
    public int getPenThickness() { return mSize; }

    private boolean mIsEraserMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_handwriting_making);

        initTopLayout();
        initBottomLayout();
        initWriteBoard();
    }

    /**
     * 상단 툴버튼 레이아웃 초기화
     */
    private void initTopLayout() {
        LinearLayout toolsLayout = (LinearLayout)findViewById(R.id.latTools);
        mBtnColor = (TitleBitmapButton)findViewById(R.id.btnColor);
        mBtnPen = (TitleBitmapButton)findViewById(R.id.btnPen);
        mBtnEraser = (TitleBitmapButton)findViewById(R.id.btnEraser);
        mBtnUndo = (TitleBitmapButton)findViewById(R.id.btnUndo);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                switch (view.getId()) {
                    case R.id.btnColor:
                        ColorPaletteDialog.mSelectedListener = new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int color) {
                                mColor = color;
                                mBoard.setPaint(mColor, mSize);
                                displayPaintProperty();
                            }
                        };

                        intent = new Intent(getApplicationContext(), ColorPaletteDialog.class);
                        startActivity(intent);

                        break;

                    case R.id.btnPen:
                        PenDialog.mSelectedListener = new OnPenSelectedListener() {
                            @Override
                            public void onPenSelected(int size) {
                                mSize =  size;
                                mBoard.setPaint(mColor, mSize);
                                displayPaintProperty();
                            }
                        };

                        intent = new Intent(getApplicationContext(), PenDialog.class);
                        startActivity(intent);
                        break;

                    case R.id.btnEraser:
                        mBoard.setPaint(mColor, mSize);
                        mBtnColor.setEnabled(mIsEraserMode);
                        mBtnPen.setEnabled(mIsEraserMode);
                        mBtnUndo.setEnabled(mIsEraserMode);

                        mBtnColor.invalidate();
                        mBtnPen.invalidate();
                        mBtnUndo.invalidate();

                        mIsEraserMode = !mIsEraserMode;
                        if(mIsEraserMode) {
                            mOldColor = mColor;
                            mOldSize = mSize;

                            mColor = Color.WHITE;
                            mSize = 15;
                        } else {
                            mColor = mOldColor;
                            mSize = mOldSize;
                        }

                        displayPaintProperty();

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

        LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams(
                                                           LinearLayout.LayoutParams.MATCH_PARENT
                                                         , LinearLayout.LayoutParams.MATCH_PARENT
        );

        LinearLayout.LayoutParams paramsButton = new LinearLayout.LayoutParams(
                                                          LinearLayout.LayoutParams.MATCH_PARENT
                                                        , LinearLayout.LayoutParams.WRAP_CONTENT
        );

        mLatPaintInfo = new LinearLayout(this);
        mLatPaintInfo.setLayoutParams(paramsLayout);
        mLatPaintInfo.setOrientation(LinearLayout.VERTICAL);
        mLatPaintInfo.setPadding(8, 8, 8, 8);

        LinearLayout latColorOutline = new LinearLayout(this);
        latColorOutline.setLayoutParams(paramsButton);
        latColorOutline.setOrientation(LinearLayout.VERTICAL);
        latColorOutline.setBackgroundColor(Color.GRAY);
        latColorOutline.setPadding(1, 1, 1, 1);

        mBtnColorLegend = new TitleBitmapButton(this);
        mBtnColorLegend.setClickable(false);
        mBtnColorLegend.setLayoutParams(paramsButton);
        mBtnColorLegend.setText(" ");
        mBtnColorLegend.setBackgroundColor(mColor);
        mBtnColorLegend.setHeight(20);
        latColorOutline.addView(mBtnColorLegend);

        mTxtSizeLegend = new TextView(this);
        mTxtSizeLegend.setLayoutParams(paramsButton);
        mTxtSizeLegend.setText("Size : " + mSize);
        mTxtSizeLegend.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        mTxtSizeLegend.setTextSize(16);
        mTxtSizeLegend.setTextColor(Color.BLACK);
        mLatPaintInfo.addView(mTxtSizeLegend);

        toolsLayout.addView(mLatPaintInfo);
    }

    /**
     * 하단 저장버튼 초기화
     */
    private void initBottomLayout() {
        mBtnSaveHandwriting = (TitleBitmapButton)findViewById(R.id.btnSaveHandwriting);

        mBtnSaveHandwriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveHandwriting();
            }
        });
    }

    /**
     * Handwriting Board UI 초기화
     */
    private void initWriteBoard() {
        LinearLayout latBoard = (LinearLayout)findViewById(R.id.latBoard);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                                                                        , ViewGroup.LayoutParams.MATCH_PARENT);
        mBoard = new HandwritingView(this);
        mBoard.setLayoutParams(params);
        mBoard.setPadding(2, 2, 2, 2);

        latBoard.addView(mBoard);
    }

    /**
     * Handwriting 데이터를 이미지 파일로 저장
     */
    private void saveHandwriting() {
        try {
            File folder = new File(BasicInfo.FOLDER_HANDWRITING);
            if(!folder.isDirectory()) { folder.mkdirs(); }

            String uri = "made";

            File file = new File(BasicInfo.FOLDER_HANDWRITING + uri);
            if(file.exists()) { file.delete(); }

            FileOutputStream stream = new FileOutputStream(BasicInfo.FOLDER_HANDWRITING + uri);
            Bitmap bmp = mBoard.getBitmap();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (Exception ex) { Log.e(TAG, "failed to save handwriting.", ex); }

        setResult(RESULT_OK);
        finish();
    }
}

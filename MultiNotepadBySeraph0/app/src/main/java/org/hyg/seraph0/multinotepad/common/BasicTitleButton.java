package org.hyg.seraph0.multinotepad.common;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by shiny on 2018-04-04.
 */

public class BasicTitleButton extends AppCompatButton {
    protected Context mContext;

    protected Paint mPaint;

    protected int mColor = 0xff333333;
    public int getColor() { return mColor; }
    public void setColor(int value) { mColor = value; mIsChangedPaint = true;}

    protected float mSize = 20f;
    public float getSize() { return mSize; }
    public void setSize(float value) { mSize = value; mIsChangedPaint = true; }

    protected float mScaleX = 1.0f;
    public float getScaleX() { return mScaleX; }
    public void setScaleX(float value) { mScaleX = value; mIsChangedPaint = true; }

    protected Typeface mTypeface = Typeface.DEFAULT_BOLD;
    public Typeface getTypeface() { return mTypeface; }
    public void setTypeface(Typeface value) { mTypeface = value; mIsChangedPaint = true; }

    protected String mTitle = "";
    public String getTitle() { return mTitle; }
    public void setTitle(String value) { mTitle = value; }

    protected boolean mIsChangedPaint = false;

    public BasicTitleButton(Context context) {
        super(context);
    }

    public BasicTitleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}

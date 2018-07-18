package org.hyg.seraph0.multinotepad.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import org.hyg.seraph0.multinotepad.R;

/**
 * Created by shiny on 2018-04-04.
 */

public class TitleBitmapButton extends AppCompatButton {

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
    //public String getTitle() { return mTitle; }
    //public void setTitle(String value) { mTitle = value; }

    protected boolean mIsChangedPaint = false;
    /*==========*/

    private boolean mIsSelected = false;
    public boolean getIsSelected() { return mIsSelected; }
    public void setSelected(boolean value) {
        mIsSelected = value;
        mIsChangedPaint = true;
        if(mIsSelected) {
            setBackgroundResource(mResBgClicked);
            mColor = Color.BLACK;
        } else {
            setBackgroundResource(mResBgNormal);
            mColor = Color.WHITE;
        }
    }
    private boolean mIsClickedIcon = false;

    private Bitmap mBmpIconNormal, mBmpIconClicked;
    public void setBackground(Bitmap normal){
        setBackground(normal, normal);
    }
    public void setBackground(Bitmap normal, Bitmap clicked) {
        mBmpIconNormal = normal;
        mBmpIconClicked = clicked;
    }
    public void setBackground(int normal) {
        setBackground(normal, normal);
    }
    public void setBackground(int normal, int clicked) {
        mBmpIconNormal = BitmapFactory.decodeResource(getResources(), normal);
        mBmpIconClicked = BitmapFactory.decodeResource(getResources(), clicked);

    }
    private int mResBgNormal = R.drawable.title_button, mResBgClicked = R.drawable.title_button_clicked;
    public void setIcon(int normal, int clicked) {
        mResBgNormal = normal;
        mResBgClicked = clicked;

        setBackgroundResource(mResBgNormal);
    }

    public static final int BITMAP_ALIGN_CENTER = 0;
    public static final int BITMAP_ALIGN_LEFT = 1;
    public static final int BITMAP_ALIGN_RIGHT = 2;

    private int mAlignBitmap = BITMAP_ALIGN_CENTER;
    public int getAlignBitmap() { return mAlignBitmap; }
    public void setAlignBitmap(int value) { mAlignBitmap = value; }

    private int mPaddingBitmap = 10;
    public int getPaddingBitmap() { return mPaddingBitmap; }
    public void setPaddingBitmap(int value) { mPaddingBitmap = value; }

    private int mTabId;
    public void setTabId(int value) { mTabId = value; }

    public TitleBitmapButton(Context context) {
        super(context);

        init(context);
    }

    public TitleBitmapButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        mContext = context;

        setBackgroundResource(mResBgNormal);

        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setTextScaleX(mScaleX);
        mPaint.setTextSize(mSize);
        mPaint.setTypeface(mTypeface);

        mIsSelected = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int action = event.getAction();


        switch (action) {
            case MotionEvent.ACTION_UP:
                if(!mIsSelected) {
                    setBackgroundResource(mResBgNormal);
                    mIsClickedIcon = false;
                    mIsChangedPaint = true;
                    mColor = Color.WHITE;
                }
                break;

            case MotionEvent.ACTION_DOWN:
                if(!mIsSelected) {
                    setBackgroundResource(mResBgClicked);
                    mIsClickedIcon = true;
                    mIsChangedPaint = true;
                    mColor = Color.BLACK;
                }
                break;

            default:
                break;
        }

        invalidate();

        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        if(mIsChangedPaint) {
            mPaint.setColor(mColor);
            mPaint.setTextScaleX(mScaleX);
            mPaint.setTextSize(mSize);
            mPaint.setTypeface(mTypeface);

            mIsChangedPaint = false;
        }


        Bitmap bmpIcon = (mIsClickedIcon) ? mBmpIconClicked : mBmpIconNormal;

        if(bmpIcon != null) {
            int iconWidth = bmpIcon.getWidth();
            int iconHeight = bmpIcon.getHeight();
            int x = 0;

            if(mAlignBitmap == BITMAP_ALIGN_CENTER) {
                x = (width - iconWidth) / 2;
            } else if(mAlignBitmap == BITMAP_ALIGN_LEFT) {
                x = mPaddingBitmap;
            } else if(mAlignBitmap == BITMAP_ALIGN_RIGHT) {
                x = width - mPaddingBitmap;
            }

            canvas.drawBitmap(bmpIcon, x, (height - iconHeight) / 2, mPaint);
        }

        Rect rect = new Rect();
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), rect);
        float x = ((float)width - rect.width()) / 2.0f;
        float y = ((float)height + rect.height()) / 2.0f + 4.0f;

        canvas.drawText(mTitle, x, y, mPaint);
    }
}

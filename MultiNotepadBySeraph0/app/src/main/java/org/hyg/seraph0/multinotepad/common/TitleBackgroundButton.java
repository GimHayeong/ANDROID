package org.hyg.seraph0.multinotepad.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.hyg.seraph0.multinotepad.R;

/**
 * Created by shiny on 2018-04-04.
 */

public class TitleBackgroundButton extends BasicTitleButton {

    public TitleBackgroundButton(Context context) {
        super(context);

        init(context);
    }

    public TitleBackgroundButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        mContext = context;

        setBackgroundResource(R.drawable.title_background);

        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setTextScaleX(mScaleX);
        mPaint.setTextSize(mSize);
        mPaint.setTypeface(mTypeface);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        final int action = event.getAction();

        switch(action) {
            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_DOWN:
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
            mPaint.setTextScaleX(mSize);
            mPaint.setTypeface(mTypeface);
        }

        Rect rect = new Rect();
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), rect);
        float x = ((float)width - rect.width()) / 2.0f;
        float y = ((float)height - rect.height()) / 2.0f + rect.height();

        canvas.drawText(mTitle, x, y, mPaint);
    }
}

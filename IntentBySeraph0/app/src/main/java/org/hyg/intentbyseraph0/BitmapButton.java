package org.hyg.intentbyseraph0;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.jar.Attributes;

/**
 * Created by shiny on 2018-03-15.
 */

public class BitmapButton extends AppCompatButton {

    private int mResIconNormal = R.drawable.img_1;
    private int mResIconClicked = R.drawable.img_2;

    public final static int STATUS_NORMAL = 0;
    public final static int STATUS_CLICKED = 1;

    private int mIconStatus = STATUS_NORMAL;

    public BitmapButton(Context context) {
        super(context);

        init();
    }

    public BitmapButton(Context context, AttributeSet attSet){
        super(context, attSet);

        init();
    }

    private void init() {
        setBackgroundResource(mResIconNormal);

        int defaultTextColor = Color.WHITE;
        float defaultTextSize = getResources().getDimension(R.dimen.text_size);
        Typeface defaultTypeface = Typeface.DEFAULT_BOLD;

        setTextColor(defaultTextColor);
        setTextSize(defaultTextSize);
        setTypeface(defaultTypeface);
    }

    public void setIcon(int iconNormal, int iconClicked){
        mResIconNormal = iconNormal;
        mResIconClicked = iconClicked;
    }

    public boolean onTouchEvent(MotionEvent event){
        super.onTouchEvent(event);

        int action = event.getAction();

        switch(action){
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(mResIconClicked);
                mIconStatus = STATUS_CLICKED;
                break;

            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                setBackgroundResource(mResIconNormal);
                mIconStatus = STATUS_NORMAL;
                break;
        }

        invalidate();

        return true;
    }
}

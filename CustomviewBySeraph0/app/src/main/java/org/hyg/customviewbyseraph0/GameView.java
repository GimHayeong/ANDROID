package org.hyg.customviewbyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    int m_w;
    int m_h;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        //int paddingLeft = getPaddingLeft();
        //int paddingTop = getPaddingTop();
        //int paddingRight = getPaddingRight();
        //int paddingBottom = getPaddingBottom();

        //int contentWidth = getWidth() - paddingLeft - paddingRight;
        //int contentHeight = getHeight() - paddingTop - paddingBottom;
        //int startX = paddingLeft + (contentWidth - mTextWidth) / 2;
        //int startY = paddingTop + (contentHeight + mTextHeight) / 2;

        Paint ptNormal = new Paint();
        ptNormal.setColor(Color.BLACK);
        ptNormal.setTextSize(60);
        ptNormal.setTextAlign(Paint.Align.CENTER);

        String strMsg = String.format("화면 해상도: %d x %d", m_w, m_h);

        canvas.drawText(strMsg, m_w / 2, m_h / 2, ptNormal);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.m_w = w;
        this.m_h = h;
    }
}

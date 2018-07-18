package org.hyg.intentbyseraph0;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by shiny on 2018-03-20.
 */



public class PaintBoardView extends View {

    private Context mContext;

    private Paint mPaint;
    private Path mPath;

    public PaintBoardView(Context context){
        super(context);

        mContext = context;

        init();
    }

    public PaintBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        init();
    }

    private void init() {
        mPath = new Path();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        setPaint(Color.BLUE, 10f);
    }

    public void setPaint(int color, float size){
        mPaint.setColor(color);
        mPaint.setStrokeWidth(size);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch(action){
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);

                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                break;

            case MotionEvent.ACTION_UP:
                break;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }
}

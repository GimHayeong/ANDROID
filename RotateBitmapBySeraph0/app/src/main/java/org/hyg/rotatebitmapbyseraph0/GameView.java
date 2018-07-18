package org.hyg.rotatebitmapbyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    int[] imgResrcs = new int[4];
    Bitmap[] bmpRoses = new Bitmap[4];
    int[] wRoses = new int[4];
    int[] hRoses = new int[4];

    public int imgIdx = 0;

    // 화면의 중심
    int m_centerX, m_centerY;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        for(int i=0; i<imgResrcs.length; i++){
            imgResrcs[i] = R.drawable.rose_1 + i;
            bmpRoses[i] = BitmapFactory.decodeResource(getResources(), imgResrcs[i]);
        }

        // 기준점 (0.5W, H)
        wRoses[0] = bmpRoses[0].getWidth() / 2;
        hRoses[0] = bmpRoses[0].getHeight();

        // 기준점 (0, 0.5H)
        wRoses[1] = 0;
        hRoses[1] = bmpRoses[1].getHeight() / 2;

        // 기준점 (0.5W, 0)
        wRoses[2] = bmpRoses[2].getWidth() / 2;
        hRoses[2] = 0;

        // 기준점 (W, 0.5H)
        wRoses[3] = bmpRoses[3].getWidth();
        hRoses[3] = bmpRoses[3].getHeight() / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int marginTRBL = 30;

        Paint bluePaint = new Paint();
        bluePaint.setColor(Color.BLUE);

        if (imgIdx >= bmpRoses.length) imgIdx = 0;

        canvas.drawBitmap(bmpRoses[imgIdx], marginTRBL, marginTRBL, null);

        canvas.drawCircle(wRoses[imgIdx] + marginTRBL
                         ,hRoses[imgIdx] + marginTRBL
                         , 20
                         , bluePaint);

        // 22.5도 회전하며 16번 출력
        int rotateCnt = 16;
        float angle = 360f / rotateCnt;

        canvas.scale(0.9f, 0.9f, m_centerX, m_centerY);

        for(int i=0; i<=rotateCnt / 2; i++){
            canvas.drawBitmap(bmpRoses[imgIdx], m_centerX - wRoses[imgIdx], m_centerY - hRoses[imgIdx], null);
            canvas.rotate(angle, m_centerX, m_centerY);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 화면 중심점
        m_centerX = w / 2;
        m_centerY = h / 2;
    }
}

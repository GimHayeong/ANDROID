package org.hyg.canvasexamplebyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class CanvasView extends View {

    public int funcIdx = 0;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        switch(funcIdx){
            case 1:
                funcRotate(canvas);
                break;

            case 2:
                funcSkew(canvas);
                break;

            case 3:
                funcSaveAndRestore(canvas);
                break;

            case 4:
                funcScale(canvas);
                break;

            default:
                funcIdx = 0;
                funcTranslate(canvas);
                break;
        }
    }

    private void funcTranslate(Canvas canvas) {
        Paint bluePaint = new Paint();
        bluePaint.setColor(0xff2040ff);

        canvas.drawRect(200, 200, 450, 550, bluePaint);

        canvas.translate(300, 0);
        canvas.drawRect(200, 200, 450, 550, bluePaint);

        Rect rect = new Rect(200, 200, 450, 550);
        canvas.translate(300, 0);
        canvas.drawRect(rect, bluePaint);

        canvas.translate(300, 0);
        canvas.drawRect(rect, bluePaint);
    }

    private void funcRotate(Canvas canvas){
        Paint bluePaint = new Paint();
        bluePaint.setColor(Color.BLUE);

        for(int i=0; i<4; i++){
            canvas.drawRect(1000, 200, 1500, 300, bluePaint);
            canvas.rotate(40, 900, 360);
        }
    }

    private void funcSkew(Canvas canvas){
        Paint rgbPaint = new Paint();
        rgbPaint.setColor(Color.rgb(0, 80, 255));
        RectF rect = new RectF(0, 0, 350, 400);

        canvas.translate(100, 200);
        canvas.drawRect(rect, rgbPaint);

        rgbPaint.setColor(Color.argb(255, 255, 0, 0));
        canvas.translate(500, 0);

        // tan(50)
        canvas.skew(1.29f, 0);
        canvas.drawRect(rect, rgbPaint);
    }

    private void funcSaveAndRestore(Canvas canvas){
        Paint alphaPaint = new Paint();
        alphaPaint.setColor(0x800000ff);
        Rect rect = new Rect(0, 0, 400, 400);

        canvas.translate(500, 150);
        canvas.drawRect(rect, alphaPaint);
        canvas.save();

        // x 축 45도 기울기
        alphaPaint.setColor(0x80ff0000);
        canvas.skew(1, 0);
        canvas.drawRect(rect, alphaPaint);

        canvas.restore();
        canvas.save();

        // y 축 45도 기울기
        alphaPaint.setColor(0x8000ff00);
        canvas.skew(0, 1);
        canvas.drawRect(rect, alphaPaint);

        canvas.restore();
        canvas.translate(400, 400);

        alphaPaint.setColor(Color.BLUE);
        canvas.drawRect(rect, alphaPaint);
    }

    private void funcScale(Canvas canvas){
        Paint rainbowPaint = new Paint();
        rainbowPaint.setColor(Color.RED);

        Path diamondPath = new Path();
        diamondPath.moveTo(20, 0);
        diamondPath.lineTo(320, -150);
        diamondPath.lineTo(620, 0);
        diamondPath.lineTo(320, 150);
        diamondPath.lineTo(20, 0);

        canvas.translate(900, 540);
        canvas.rotate(45);
        canvas.drawPath(diamondPath, rainbowPaint);

        rainbowPaint.setColor(getResources().getColor(R.color.colorOrange));
        canvas.scale(-1, 1);
        canvas.drawPath(diamondPath, rainbowPaint);

        rainbowPaint.setColor(Color.YELLOW);
        canvas.rotate(90);
        canvas.drawPath(diamondPath, rainbowPaint);

        rainbowPaint.setColor(Color.GREEN);
        canvas.scale(-1, 1);
        canvas.drawPath(diamondPath, rainbowPaint);
    }

}
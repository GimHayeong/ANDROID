package org.hyg.intentbyseraph0;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by shiny on 2018-03-20.
 */

public class CustomViewDrawables extends View {

    private Context mContext;

    // 화면 2/3 위쪽 영역 그라데이션
    private ShapeDrawable mUpperDraw;
    // 화면 1/3 아래쪽 영역 그라데이션
    private ShapeDrawable mLowerDraw;

    public  CustomViewDrawables(Context context){
        super(context);

        mContext = context;

        init();
    }

    public CustomViewDrawables(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(mUpperDraw != null && mLowerDraw != null) {
            mUpperDraw.draw(canvas);
            mLowerDraw.draw(canvas);
        }

        drawCapNJoin(canvas);
    }

    private void drawCapNJoin(Canvas canvas) {
        Paint patPath;
        Path path;

        patPath = new Paint();
        patPath.setAntiAlias(true);
        patPath.setStyle(Paint.Style.STROKE);
        patPath.setStrokeWidth(16.0f);


        path = new Path();
        path.moveTo(20, 20);
        path.lineTo(120, 20);
        path.lineTo(160, 90);
        path.lineTo(180, 80);
        path.lineTo(200, 120);


        // *** Yellow path
        patPath.setColor(Color.YELLOW);
        patPath.setStrokeCap(Paint.Cap.BUTT);
        patPath.setStrokeJoin(Paint.Join.MITER);

        canvas.drawPath(path, patPath);


        // *** White path
        patPath.setColor(Color.WHITE);
        patPath.setStrokeCap(Paint.Cap.ROUND);
        patPath.setStrokeJoin(Paint.Join.ROUND);

        path.offset(30, 120);
        canvas.drawPath(path, patPath);


        // *** Cyan path
        patPath.setColor(Color.CYAN);
        patPath.setStrokeCap(Paint.Cap.SQUARE);
        patPath.setStrokeJoin(Paint.Join.BEVEL);

        path.offset(30, 120);
        canvas.drawPath(path, patPath);
    }

    private void init() {
        WindowManager winMgr = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);

        Display display = winMgr.getDefaultDisplay();
        Point ptSize = new Point();
        display.getSize(ptSize);

        int width = ptSize.x;
        int height = ptSize.y;

        Resources res = getResources();
        int blackColor = res.getColor(R.color.colorBlack);
        int grayColor = res.getColor(R.color.colorGray);
        int darkGrayColor = res.getColor(R.color.colorDarkGray);

        mUpperDraw = initShape(width, height * 2 / 3, grayColor, blackColor);
        mLowerDraw = initShape(width, height * 1 / 3, blackColor, darkGrayColor, height * 2 / 3, height);
    }

    private ShapeDrawable initShape(int width, int height, int beginColor, int endColor) {
        return initShape(width, height, beginColor, endColor, 0, height);
    }

    private ShapeDrawable initShape(int width, int height, int beginColor, int endColor, int top, int bottom) {

        ShapeDrawable shape = new ShapeDrawable();
        RectShape rect = new RectShape();
        rect.resize(width, height);
        shape.setShape(rect);
        shape.setBounds(0, top, width, bottom);

        LinearGradient gradient = new LinearGradient(0, 0, 0, height
                , beginColor
                , endColor
                , Shader.TileMode.CLAMP);

        Paint paint = shape.getPaint();

        paint.setShader(gradient);

        return shape;
    }


}

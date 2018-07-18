package org.hyg.squaretargetbyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class GameView extends View {

    Bitmap m_bmpBack;
    Bitmap m_bmpTarget;

    int m_width, m_height;
    int m_centerX, m_centerY;
    int m_targetSize;

    int m_score = 0;
    int m_total = 0;

    RectF[] m_targets = new RectF[3];
    Paint m_scorePaint = new Paint();

    ArrayList<BulletHole> m_holeList = new ArrayList<BulletHole>();

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        m_scorePaint.setColor(Color.WHITE);
        m_scorePaint.setTextSize(60);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(m_bmpBack, 0, 0, null);
        canvas.drawBitmap(m_bmpTarget
                       , m_centerX - m_targetSize
                       , m_centerY - m_targetSize
                       , null);

        for(BulletHole itm : m_holeList){
            canvas.drawBitmap(itm.bmpHole, itm.x - itm.w, itm.y - itm.h, null);
        }


        String strScore = String.format("득점: %d", m_score);
        String strTotal = String.format("총점: %d", m_total);
        m_scorePaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(strScore, 100, 100, m_scorePaint);

        m_scorePaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(strTotal, m_width - 100, 100, m_scorePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.m_width = w;
        this.m_height = h;

        m_centerX = w / 2;
        m_centerY = h / 2;

        getBitmap();
        getArea();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            int x = (int)event.getX();
            int y = (int)event.getY();

            if(getScore(x, y) > 0){
                m_holeList.add(new BulletHole(x, y));
            }

            invalidate();
        }

        return true;
    }

    private void getBitmap() {
        m_bmpTarget = BitmapFactory.decodeResource(getResources(), R.drawable.target);
        m_targetSize = m_bmpTarget.getWidth() / 2;

        m_bmpBack = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        m_bmpBack = Bitmap.createScaledBitmap(m_bmpBack, m_width, m_height, true);
    }

    // 터치 판정 영역
    //  : 사각형 크기 이용
    private void getArea() {
        // 실제 이미지 크기(280)와 화면에 표시된 크기(738)의 크기 비율 확인 로그
        //Log.v("과녁의크기: ", m_targetSize * 2 + "-------");

        float ratio = m_targetSize * 2 / 280f;
        float[] expendedSize = {280 * ratio * 0.5f, 180 * ratio * 0.5f, 80 * ratio * 0.5f};

        for(int i=0; i<m_targets.length; i++){
            m_targets[i] = new RectF(m_centerX - expendedSize[i]
                                   , m_centerY - expendedSize[i]
                                   , m_centerX + expendedSize[i]
                                   , m_centerY + expendedSize[i]);
        }
    }

    private int getScore(int x, int y){
        int[] rectScore = { 6, 8, 10 };

        m_score = 0;
        for(int i=2; i>=0; i--){
            if(m_targets[i].contains(x, y)){
                m_score = rectScore[i];
                m_total += m_score;
                break;
            }
        }

        return m_score;
    }


    /*inner class*/
    class BulletHole{
        public int x, y;
        public int w, h;
        public Bitmap bmpHole;

        public BulletHole(int x, int y){
            this.x = x;
            this.y = y;

            this.bmpHole = BitmapFactory.decodeResource(getResources(), R.drawable.hole);
            this.w = bmpHole.getWidth() / 2;
            this.h = bmpHole.getHeight() / 2;
        }
    }
}

package org.hyg.polygontargetbyseraph0;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
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

    // 6각형 꼭지점 배열
    Point[][] m_points = new Point[3][7];
    float[] m_targets = new float[3];
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
    //  : 원의 반지름 이용
    private void getArea() {
        // 실제 이미지 크기(280)와 화면에 표시된 크기(738)의 크기 비율 확인 로그
        //Log.v("과녁의크기: ", m_targetSize * 2 + "-------");

        float ratio = m_targetSize / 140f;
        float[] originalSize = {140, 90, 40};

        for(int i=0; i<m_targets.length; i++){
            m_targets[i] = originalSize[i] * ratio;

            for(int j=0; j<6; j++){
                double rds = Math.toRadians(60 * j);
                double x = m_centerX + Math.cos(rds) * m_targets[i];
                double y = m_centerY + Math.sin(rds) * m_targets[i];

                m_points[i][j] = new Point((int)x, (int)y);
            }

            // 끝점과 시작점 잇기
            m_points[i][6] = new Point(m_points[i][0]);
        }
    }

    // 중심점(cx, cy)이고 반지름이 r인 원의 내부에 있는 점 (x1, y1)은 다음의 부등식이 성립한다.
    // (cx - x1)*(cx - x1) + (cy - y1)*(cy - y1) < r*r
    private int getScore(int x, int y){
        int[] rectScore = { 10, 6, 12, 4, 15, 8, 10, 6, 12, 4, 15, 6 };

        // 회전각 구하기(0 ~ 360도 범위값)
        float degree = (float)-Math.toDegrees(Math.atan2(y - m_centerY, x - m_centerX));
        if(degree < 0) degree += 360;

        int idx = (int)(degree / 60);

        m_score = 0;
        for(int i=2; i>=0; i--){
            // (x, y)에서 그은 수평선이 각 변과 교차되는 숫자
            int cnt = 0;

            for(int j=0; j<6; j++) {
                if (hitTest(x, y, m_points[i][j], m_points[i][j+1])) {
                    cnt++;
                }
            }

            // 교차되는 선분의 합이 홀수이면 득점
            if(cnt % 2 == 1) {
                m_score = rectScore[i] * (i + 1);
                m_total += m_score;
                break;
            }
        }

        return m_score;
    }

    /**
     *
     * @param x
     * @param y
     * @param pt1 : 다각형의 포인트 1
     * @param pt2 : 다각형의 포인트 2
     * @return : 선분의 교차여부
     */
    private boolean hitTest(int x, int y, Point pt1, Point pt2) {
        boolean hit = false;

        // 화면기준(CCW 또는 CW) 선분 교차여부
        if((y > pt1.y && y <= pt2.y) || (y < pt1.y && y >= pt2.y)){

            // 기울기
            float m = (float)((pt2.y - pt1.y)) / (pt2.x - pt1.x);

            // 교차점의 x좌표
            float px = pt1.x + (y - pt1.y) / m;

            if(x < px){
                hit = true;
            }
        }

        return hit;
    }

    // 게임 초기화
    public void initGame(){
        m_holeList.clear();
        m_score = m_total = 0;
        invalidate();
    }


    /*inner class*/
    class BulletHole{
        public int x, y;
        public int w, h;
        public Bitmap bmpHole;

        public BulletHole(int x, int y){
            this.x = x;
            this.y = y;

            this.bmpHole = BitmapFactory.decodeResource(getResources(), R.drawable.dart);
            this.w = 0;
            this.h = bmpHole.getHeight();
        }
    }
}
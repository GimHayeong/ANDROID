package org.hyg.butterflybyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.Random;

/**
 * Created by shiny on 2018-02-23.
 */

public class Butterfly {
    // 화면 크기
    private int m_scrnWt, m_scrnHt;
    private Random m_rnd = new Random();

    // 이동속도 200 ~ 300 픽셀
    private int m_speed;
    // 이동방향 벡터
    private PointF m_direction = new PointF();

    // 애니메이션 속도 0.06 ~ 0.13 초
    private float m_aniSpan;
    // 이전프레임으로부터의 경과시간
    private float m_aniTime = 0;

    private Bitmap[] m_butterflys = new Bitmap[10];
    private int m_bmpIdx;

    // 목적지 최대 근접 거리 50 ~ 150 픽셀
    private int m_dist;
    // 목적지 여부
    private boolean m_existTarget;
    // 목적지 도착여부
    private boolean m_isReached;
    // 목적지에 머무를 시간 1.0 ~ 1.5 초
    private float m_stay;

    // 나비 위치
    private int m_x, m_y;
    public int getX() { return m_x; }
    public int getY() { return m_y; }

    // 목적지 위치
    private float m_tx, m_ty;
    public float getTargetX() { return m_tx; }
    public float getTargetY() { return m_ty; }

    // 나비 이미지
    private Bitmap m_butterfly;
    public Bitmap getButterfly() { return m_butterfly; }

    // 나비 크기
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    // 회전각도
    private float m_angle;
    public float getAngle() { return m_angle; }

    public Butterfly(Context context, int width, int height){
        m_scrnWt = width;
        m_scrnHt = height;

        // 화면의 임의의 위치에 나비 초기위치 설정
        m_x = m_rnd.nextInt(m_scrnWt);
        m_y = m_rnd.nextInt(m_scrnHt);

        makeButterfly(context);
        init();
    }

    private void makeButterfly(Context context) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.butterfly);
        m_width = bmp.getWidth();
        m_height = bmp.getHeight();

        Bitmap bmpNew = Bitmap.createBitmap(m_width, m_height, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();

        int color = m_rnd.nextInt(0x808080) + 0x808080;
        ColorFilter filter = new LightingColorFilter(color, 0x404040);
        paint.setColorFilter(filter);

        Canvas canvas = new Canvas(bmpNew);
        canvas.drawBitmap(bmp, 0, 0, paint);

        m_width /= 10;
        for(int i=0; i<10; i++){
            m_butterflys[i] = Bitmap.createBitmap(bmp, m_width * i, 0, m_width, m_height);
        }

        m_width /= 2;
        m_height /= 2;

        m_butterfly = m_butterflys[0];
    }

    private void init() {
        // 200~300 픽셀
        m_speed = m_rnd.nextInt(101) + 200;

        // 이동방향
        double radius = Math.toRadians(m_rnd.nextInt(360));

        m_direction.set((float)Math.cos(radius) * m_speed
                      , -(float)Math.sin(radius) * m_speed);

        m_angle = 90 - (float)Math.toDegrees(radius);
        m_aniSpan = (m_rnd.nextInt(8) + 6) / 100f;
        m_stay = (m_rnd.nextInt(6) + 10) / 10f;
        m_dist = m_rnd.nextInt(101) + 50;

        m_existTarget = m_isReached = false;
    }

    private void startAnimation(){
        m_aniTime += DTime.DeltaTime;

        if(m_aniTime > m_aniSpan){
            m_bmpIdx++;
            m_aniTime = 0;

            if(m_bmpIdx >= 10) { m_bmpIdx = 0; }
        }

        m_butterfly = m_butterflys[m_bmpIdx];
    }

    // 목적지 50 ~ 150 픽셀 이내로 접근하면 대기
    // 대기시간이 지나면 다시 랜덤하게 이동
    private void checkApporachTarget(){
        float radius = (m_x - m_tx) * (m_x - m_tx) + (m_y - m_ty) * (m_y - m_ty);

        m_isReached = (radius <= m_dist * m_dist);

        if(m_isReached){
            m_direction.set(0, 0);
            m_stay -= DTime.DeltaTime;

            if(m_stay <= 0) {
                init();
            }
        }
    }

    // 목적지 좌표 설정
    public void setTarget(float px, float py){
        m_tx = py;
        m_ty = py;

        double radius = -Math.atan2(m_ty - m_y
                                  , m_tx - m_x);

        m_direction.set((float)Math.cos(radius) * m_speed
                     , -(float)Math.sin(radius) * m_speed);

        m_angle = 90 - (float)Math.toDegrees(radius);
        m_existTarget = true;
    }

    public void moveToNext(){
        startAnimation();

        m_x += m_direction.x * DTime.DeltaTime;
        m_y += m_direction.y * DTime.DeltaTime;

        if(m_existTarget){
            checkApporachTarget();
        }

        // 화면을 벗어나면 반대쪽에서 등장
        if(m_x < -m_width) { m_x = m_scrnWt + m_width; }
        if(m_x > m_scrnWt + m_width) { m_x = -m_width; }
        if(m_y < -m_height) { m_y = m_scrnHt + m_height; }
        if(m_y > m_scrnHt + m_height) { m_y = -m_height; }
    }
}

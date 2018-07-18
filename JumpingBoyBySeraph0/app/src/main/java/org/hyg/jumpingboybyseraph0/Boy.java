package org.hyg.jumpingboybyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.support.design.widget.Snackbar;

/**
 * Created by shiny on 2018-02-23.
 */

public class Boy {

    // 화면 크기
    private int m_scrnWt, m_scrnHt;

    // 걷기 속도
    private int m_speedWalk = 300;
    // 점핑 속도
    private int m_speedJump = 1000;
    // 중력
    private int m_gravity = 2000;
    // 이동방향 벡터
    private PointF m_direction = new PointF();

    // 애니메이션 속도 (초당 5프레임)
    private float m_aniSpan = 0.2f;

    // 이전 프레임으로부터 경과시간
    private float m_aniTime = 0;

    private Bitmap[] m_bmpBoys = new Bitmap[5];
    private int m_idx = 0;

    // 현재위치
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }

    // 바닥높이
    private int m_groundHt;
    public int getGroundHeight() { return m_groundHt; }

    // 착지상태여부
    private boolean m_isLanding = true;
    public boolean getIsGround() { return m_isLanding; }

    // 소년 이미지
    private Bitmap m_boy;
    public Bitmap getBoy() { return m_boy; }
    // 소년 이미지 크기
    private int m_wt, m_ht;
    public int getWidth() { return m_wt; }
    public int getHeight() { return m_ht; }

    // 그림자, 크기
    private Bitmap m_bmpShadow;
    public Bitmap getShadow() { return m_bmpShadow; }
    // 그림자 크기
    private int m_sdwWt, m_sdwHt;
    public int getShadowWidth() { return m_sdwWt; }
    public int getShadowHeight() { return m_sdwHt; }
    // 점프 높이에 따른 그림자 축소비율
    private float m_sdwScale;
    public float getSadowScale() { return m_sdwScale; }


    public Boy(Context context, int width, int height){
        m_scrnWt = width;
        m_scrnHt = height;
        m_groundHt = (int)(height * 0.9f);

        init(context);
        initBoy();
    }

    private void init(Context context) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy);
        int wt = bmp.getWidth() / 5;
        int ht = bmp.getHeight();

        for(int i=0; i<5; i++){
            m_bmpBoys[i] = Bitmap.createBitmap(bmp
                                             , wt * i
                                             , 0
                                             , wt
                                             , ht);
        }

        m_wt = wt / 2;
        m_ht = ht / 2;

        m_bmpShadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.shadow);
        m_sdwWt = m_bmpShadow.getWidth() / 2;
        m_sdwHt = m_bmpShadow.getHeight() / 2;
    }

    private void initBoy(){
        m_x = m_wt * 2;
        m_y = m_groundHt - m_ht;
        m_isLanding = true;
        m_idx = 0;
        m_boy = m_bmpBoys[m_idx];

        m_direction.set(m_speedWalk, 0);
    }


    public void moveToNext(){

        startAnimation();

        // 점프 중이면 중력 적용
        if(!m_isLanding){
            m_direction.y += m_gravity * DTime.DeltaTime;
        }

        m_x += m_direction.x * DTime.DeltaTime;
        m_y += m_direction.y * DTime.DeltaTime;

        m_sdwScale = m_y / (m_groundHt - m_ht);

        checkLanding();

        // 화면을 벗어나면 초기화
        if(m_x > m_scrnWt + m_wt * 2) {
            initBoy();
        }
    }

    private void startAnimation(){
        // 점프 중 이면,
        // 점프 이미지는 하나밖에 없으므로 애니매이션 하지 않음
        if(!m_isLanding){
            m_boy = m_bmpBoys[4];
            return;
        }

        m_aniTime += DTime.DeltaTime;
        if(m_aniTime > m_aniSpan){
            m_idx++;
            m_aniTime = 0;

            if(m_idx > 3) {
                m_idx = 0;
            }
        }

        m_boy = m_bmpBoys[m_idx];
    }

    // 터치하면 점프
    // 수학의 y좌표와 단말기의 y좌표 반대이므로 (-)
    public void startJump(){
        if(m_isLanding){
            m_direction.y = -m_speedJump;
            m_isLanding = false;
        }
    }

    public void startStepByStepJump(){
        int jumpCnt = 0;
        if(jumpCnt < 2){
            jumpCnt++;
            startJump();
        }
        checkLanding();

    }

    // 바닥에 닿으면 착지
    private void checkLanding(){
        if(m_y > m_groundHt - m_ht){
            m_y = m_groundHt - m_ht;
            m_isLanding = true;
        }
    }

}

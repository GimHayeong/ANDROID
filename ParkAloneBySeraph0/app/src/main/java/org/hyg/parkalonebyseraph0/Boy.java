package org.hyg.parkalonebyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import java.util.Random;

/**
 * Created by shiny on 2018-02-26.
 */

public class Boy {
    // 동작상태코드 : 울기, 앉기, 정지, 높이 뛰기, 걷기
    private enum STATE { CRY, DOWN, IDLE, JUMP, WALK };
    private STATE m_state;

    // 화면크기
    private int m_scrW, m_scrH;

    private Random m_rnd = new Random();

    // 이동속도
    private float m_speed = 600;
    // 현재 이동속도
    private float m_currentSpeed;
    // 점프속도
    private float m_speedJump = 1300;
    // 중력(점프에 반영)
    private float m_gravity = 2000;

    // 애니메이션 이미지인덱스, 지연시간, 이전프레임으로부터의 경과시간
    private int m_bmpIdx = 0;
    private float m_aniTime;
    private float m_aniSpan = 0.15f;

    // 대기시간 : 상태가 DOWN => CRY => IDLE 로 전이될 때의 지연시간
    private float m_waitTime = 0.5f;

    // 원본이미지, 목적지 x, y 좌표
    private Bitmap[][] m_bmpBoys = new Bitmap[5][4];
    private float m_tx, m_ty;

    // 현재위치, 이동방향벡터
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private PointF m_direction = new PointF();
    public PointF getDirection() { return m_direction; }

    // 이미지, 크기
    private Bitmap m_bmp;
    public Bitmap getBoy() { return m_bmp; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    // 그림자, 크기, 축소비율
    private Bitmap m_bmpShadow;
    public Bitmap getShadow() { return m_bmpShadow; }
    private int m_sw, m_sh;
    public int getShadowWidth() { return m_sw; }
    public int getShadowHeight() { return m_sh; }
    private float m_sScale = 1;
    public float getShadowScale() { return m_sScale; }

    // 지면높이
    private float m_groundH;
    public float getGroundHeight() { return m_groundH; }


    public Boy(Context context, int width, int height){
        m_scrW = width;
        m_scrH = height;

        initResources(context);
        init();
    }

    /**
     * 걷기와 대기 상태일 때
     * 소년을 터치하면 점프하고, 다른 지점을 터치하면 목적지 설정후 소년을 걸어서 이동시킴
     * @param tx
     * @param ty
     */
    public void getIntoAction(float tx, float ty){
        if(m_state != STATE.WALK && m_state != STATE.IDLE) { return; }

        if(MathF.getIsTouch(m_x, m_y, m_width, tx, ty)){
            m_direction.y = -m_speedJump;
            m_state = STATE.JUMP;
        } else {
            m_tx = tx;
            m_ty = ty;

            m_direction.x = (m_x < tx) ? 1 : -1;
            m_state = STATE.WALK;
        }
    }



    public void moveToNext(){
        switch(m_state){
            case IDLE:
                m_currentSpeed = 0;
                break;

            case WALK:
                m_currentSpeed = m_speed;
                break;

            case CRY:
                initCry();
                break;

            case DOWN:
                initDown();
                break;
        }

        moveBoy();

        startAnimation();
    }

    /**
     * 연속 충돌을 금지하기 위해 앉거나 울기상태에서는 충돌체크 안함
     * @param tx : 충돌대상 x좌표
     * @param ty : 충돌대상 y좌표
     * @param tr : 충돌대상 반지름
     * @return
     */
    public boolean getIsCollision(float tx, float ty, int tr){
        boolean hit = false;

        if(m_state != STATE.DOWN && m_state != STATE.CRY) {
            if(MathF.getIsCollision(tx, ty, tr, m_x, m_y, m_width * 0.9f, m_height * 0.9f)){
                m_state = STATE.DOWN;
                hit = true;
            }
        }

        return hit;
    }

    /**
     * 소년의 위치 이동
     */
    private void moveBoy() {
        // 중력 반영
        m_direction.y += m_gravity * DTime.DeltaTime;

        // 수평 이동
        m_x += m_direction.x * m_currentSpeed * DTime.DeltaTime;
        // 수직 이동
        m_y += m_direction.y * DTime.DeltaTime;

        checkArrivedAtDestination();
        checkIsLanded();
    }

    /**
     * 지면에 닿으면, 점프중이면 정지상태로 상태변경
     */
    private void checkIsLanded() {
        if(m_y > m_groundH - m_height){
            m_y = m_groundH - m_height;
            m_direction.y = 0;

            if(m_state == STATE.JUMP) { m_state = STATE.IDLE; }
        }

        m_sScale = m_y / (m_groundH - m_height);
    }

    /**
     * 걷기 상태에서 목적지 근처에 도달하면 정지
     */
    private void checkArrivedAtDestination() {
        if(m_state == STATE.WALK && Math.abs(m_x - m_tx) < 2){
            m_state = STATE.IDLE;
        }

        if(m_x < m_width){
            m_x = m_width;
            m_state = STATE.IDLE;
        }

        if(m_x > m_scrW - m_width){
            m_x = m_scrW - m_width;
            m_state = STATE.IDLE;
        }
    }

    /**
     * 상태에 따른 애니메이션
     */
    private void startAnimation() {

        if(m_state != STATE.WALK){
            m_bmpIdx = 0;
        } else {
            m_aniTime += DTime.DeltaTime;

            if(m_aniTime > m_aniSpan){
                m_aniTime = 0;
                m_bmpIdx = MathF.getRepeatIdx(m_bmpIdx, 4);
            }
        }

        m_bmp = m_bmpBoys[m_state.ordinal()][m_bmpIdx];
    }

    /**
     * WALK(JUMP) => DOWN => CRY 상태 변경 초기 설정
     * 앉은 후 일정시간이 지나면 울기 시작한다.
     * 점프 상태이면, 지면에 닿을 정도의 시간을 기다린 후 CRY로 상태변경
     */
    private void initDown() {
        if(m_state != STATE.JUMP){
            m_currentSpeed = 0;
        }

        m_waitTime -= DTime.DeltaTime;
        if(m_waitTime <= 0){
            m_state = STATE.CRY;
            m_waitTime = 1f;
        }
    }

    /**
     * 울다가 일정시간이 지나면 울음을 그치고 정지한다.
     * IDLE 상태로 변경될 때, 이후에 다시 공에 맞았을때의 DOWN 대기 시간 0.5f 설정
     */
    private void initCry() {
        m_currentSpeed = 0;
        m_waitTime -= DTime.DeltaTime;

        if(m_waitTime <= 0){
            m_waitTime = 0.5f;
            m_state = STATE.IDLE;
        }
    }

    /**
     * 좌표, 목적지 좌표, 이동방향, 지면높이, 소년의 상태와 그에 따른 이미지 초기화
     */
    private void init() {
        // 이동방향, 지면높이
        m_direction.set(1, 0);
        m_groundH = m_scrH * 0.9f;

        // 현재위치 및 목적지 초기좌표
        m_tx = m_x = m_scrW / 2;
        m_ty = m_y = m_groundH - m_height;

        // 상태와 소년 이미지 초기화
        m_state = STATE.IDLE;
        m_bmp = m_bmpBoys[m_state.ordinal()][0];
    }

    /**
     * 상태별 소년 이미지, 그림자 이미지 초기화
     * @param context
     */
    private void initResources(Context context) {
        m_bmpShadow = BitmapFactory.decodeResource(context.getResources(), R.drawable.shadow);

        m_sw = m_bmpShadow.getWidth();
        m_sh = m_bmpShadow.getHeight();

        // 그림자 2배 확대
        m_bmpShadow = Bitmap.createScaledBitmap(m_bmpShadow, m_sw * 2, m_sh * 2, true);

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.boy);
        m_width = bmp.getWidth() / 4;
        m_height = bmp.getHeight() / 5;

        // 상태별 초기이미지 : 울기, 앉기, 정지, 점프
        for(int i=0; i<=3; i++){
            m_bmpBoys[i][0] = Bitmap.createBitmap(bmp, 0, i * m_height, m_width, m_height);
        }

        // 걷기 상태 초기이미지
        for(int i=0; i<=3; i++){
            m_bmpBoys[4][i] = Bitmap.createBitmap(bmp, m_width * i, m_height * 4, m_width, m_height);
        }

        m_width /= 2;
        m_height /= 2;
    }


}

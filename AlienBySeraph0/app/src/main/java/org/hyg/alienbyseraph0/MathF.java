package org.hyg.alienbyseraph0;

import android.graphics.PointF;

/**
 * Created by shiny on 2018-02-24.
 *
 * 팁::: 게임에서는 매 프레임마다 호출되는 함수에서는 객체 타입의 지역변수 타입을 자제하고
 *       전역변수 사용으로 가비지 컬렉션으로 인한 랙현상 발생을 줄인다.
 *
 * 팁::: 모바일 단말기는 부동소수점 연산을 소프트웨어적으로 처리하여 퍼포먼스에 영향을 주므로
 *       double 형을 리턴하는 Math 함수는 자제한다.
 */

public class MathF {

    // 보간 좌표 (두 점 사이의 임의의 추정 좌표)
    static private PointF m_pos = new PointF();
    // x축 수평거리, Y축 수직거리, 기울기
    static private float m_dx, m_dy, m_m;

    // 두 지점 사이의 거리
    static public float getDistance(PointF p1, PointF p2){
        return getDistance(p1.x, p1.y, p2.x, p2.y);
    }

    // 두 지점 사이의 거리
    static public float getDistance(float x, float y, float px, float py){
        return (float)Math.sqrt((x - px) * (x - px) + (y - py) * (y - py));
    }

    /**
     * 선형보간 구하기 (두 점 사이의 임의의 추정 값 구하기)
     * @param p1 : 시작점
     * @param p2 : 끝점
     * @param k : 전체거리에 대한 비율
     * @return 두 점 사이의 임의의 추정 좌표 반환
     */
    static public PointF getLerp(PointF p1, PointF p2, float k){
        // 목적지 근처이면 p2
        if(getDistance(p1, p2) < 1.0f) { return p2; }

        // 수평, 수직 거리
        m_dx = p2.x - p1.x;
        m_dy = p2.y - p1.y;

        // 수직으로만 이동했으면
        if(m_dx == 0){
            m_pos.x = p1.x;
            m_pos.y = p1.y + k * m_dy;
        } else {
            // 기울기
            m_m = m_dy / m_dx;
            m_pos.x = p1.x + k * m_dx;
            m_pos.y = p1.y + k * m_dx * m_m;
        }

        return m_pos;
    }

    /**
     * 선형보간 구하기 (두 값 사이의 임의의 추정 값 구하기)
     * @param start : 시작 값
     * @param end : 끝 값
     * @param k : 증가 비율 (클수록 빠르게 가속/감속)
     * @return : 시작 값과 끝 값 사이의 임의의 추정 값 반환
     *    가속 예) MathF.getLerp(speed, maxSpeed, 3 * DTime.DeltaTime)
     *    감속 예) MathF.getLerp(speed, 0, 3 * DTime.DeltaTime)
     */
    static public float getLerp(float start, float end, float k){
        return start + (end - start) * k;
    }




    // 타겟의 내부를 터치했는지 여부 반환
    static public boolean getIsTouch(PointF p, float radius, PointF tp){
        return getIsTouch(p.x, p.y, radius, tp.x, tp.y);
    }

    // 타겟의 내부를 터치했는지 여부 반환
    static public boolean getIsTouch(float x, float y, float radius, float tx, float ty){
        return ((x - tx) * (x - tx) + (y - ty) * (y - ty)) < radius * radius;
    }

    // 애니메이션의 인덱스를 반복
    static public int getRepeatIdx(int idx, int end){
        if(++idx >= end) { idx = 0; }

        return idx;
    }

    // 12시 기준 CW 회전 각도 반환
    static public float getDegreeCW(PointF p1, PointF p2){
        double radius = -Math.atan2(p2.y - p1.y, p2.x - p1.x);

        return 90 - (float)Math.toDegrees(radius);
    }

    // 제한된 범위 안의 값 반환
    static public float getClampedValue(float org, float min, float max){
        return Math.max(min, Math.min(org, max));
    }

    /**
     * 이동방향 구하기
     * @param p
     * @param t
     * @return
     */
    static public PointF getDirection(PointF p, PointF t){
        // 3시 방향 기준 CCW로 회전한 각도(라디안)
        m_m = (float) -Math.atan2(t.y - p.y, t.x - p.x);

        m_pos.x = (float)Math.cos(m_m);
        m_pos.y = -(float)Math.sin(m_m);

        return m_pos;
    }


}

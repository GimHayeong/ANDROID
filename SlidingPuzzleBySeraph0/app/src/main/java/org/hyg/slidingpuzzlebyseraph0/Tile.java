package org.hyg.slidingpuzzlebyseraph0;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;

/**
 * Created by shiny on 2018-03-07.
 */

public class Tile {

    // 터치 판정 영역
    RectF m_rectTouch = new RectF();

    // 이동속도, 이동방향벡터, 목적지
    private int m_speed = 2000;
    private Point m_direction = new Point();
    private float m_tx, m_ty;

    // 타일 번호, 위치, 크기, 이미지
    private int m_idx;
    public int getIdx() { return m_idx; }
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private int m_width;
    public int getWidth() { return m_width; }
    private Bitmap m_bmp;
    public Bitmap getTile() { return m_bmp; }

    public Tile(int idx){
        m_idx = idx;
        m_bmp = CommonResources.getTile()[idx];
        m_width = CommonResources.getTileWidth();
        setPosition(idx);
    }

    /**
     * 타일 좌표 설정
     *  : 첫번째 타일의 좌표를 (0, 0)으로 설정
     *    => 추후 canvas.translate()로 여백만큼 이동하여 화면에 출력
     * @param idx
     */
    public void setPosition(int idx){
        m_x = (idx % Settings.Size) * m_width;
        m_y = (idx / Settings.Size) * m_width;
    }

    /**
     * 목적지에 도착할때까지 이동
     *  : 이동 가능 범위(목적지)를 지났으면 목적지 좌표를 타일 좌표로 설정하고 이동방향 초기화(이동정지)
     * @return : 이동 가능 범위내이면 True, 벗어나면(목적지 도착) False
     */
    public boolean moveToNext(){
        m_x += m_direction.x * m_speed * DTime.DeltaTime;
        m_y += m_direction.y * m_speed * DTime.DeltaTime;

        if(m_direction.x > 0 && m_x > m_tx
          || m_direction.x < 0 && m_x < m_tx
          || m_direction.y > 0 && m_y > m_ty
          || m_direction.y < 0 && m_y < m_ty){
            m_x = m_tx;
            m_y = m_ty;
            m_direction.set(0, 0);

            return false;
        }

        return true;
    }


    /**
     * 타일 개체의 좌표 설정
     * 목적지 좌표, 이동방향(-1, 0, 1) 설정
     *  : 목적지 (12시기준 CW 1, 2, 3, 4)
     * @param dir
     */
    public void setDirection(int dir){

        int[] distX = { 0, 0, m_width, 0, -m_width };
        int[] distY = { 0, -m_width, 0, m_width, -0 };

        m_tx = m_x + distX[dir];
        m_ty = m_y + distY[dir];

        m_direction.x = distX[dir] / m_width;
        m_direction.y = distY[dir] / m_width;
    }

    /**
     *  : 타일 배열의 좌표는 여백을 고려하지 않았으므로 터치영역의 좌표 계산시 여백을 더함.
     *    터치영역 => 사각형영역(L, T, R, B)
     * @param tx : 터치한 x좌표
     * @param ty : 터치한 y좌표
     * @return : 타일을 터치했으면 타일의 인덱스, 아니면 -1 반환
     */
    public int hitTest(float tx, float ty){
        float x = m_x + CommonResources.MarginWidth;
        float y = m_y + CommonResources.MarginHeight;

        m_rectTouch.set(x, y, x + m_width - 1, y + m_width - 1);

        return m_rectTouch.contains(tx, ty) ? m_idx : -1;
    }

}

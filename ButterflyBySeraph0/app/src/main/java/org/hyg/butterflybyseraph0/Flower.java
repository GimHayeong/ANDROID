package org.hyg.butterflybyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

/**
 * Created by shiny on 2018-02-23.
 */

public class Flower {

    // 꽃다발 위치
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }

    // 꽃다발 크기
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    // 꽃다발 이미지
    private Bitmap m_bmpFlower;
    public Bitmap getFlower() { return m_bmpFlower; }

    public Flower(Context context, int width, int height) {
        m_bmpFlower = BitmapFactory.decodeResource(context.getResources(), R.drawable.flower);
        m_width = m_bmpFlower.getWidth() / 2;
        m_height = m_bmpFlower.getHeight() / 2;

        Random rnd = new Random();
        m_x = rnd.nextInt(width - m_width * 2) + m_width;
        m_y = rnd.nextInt(height - m_height * 2) + m_height;
    }

    // 꽃다발의 내부를 클릭한 경우, 클릭한 지점을 꽃다발의 좌표로 변경
    public boolean moveToNext(float tx, float ty) {
        float dist = (tx - m_x) * (tx - m_x) + (ty - m_y) * (ty - m_y);

        if(dist < m_width * m_width) {
            m_x = tx;
            m_y = ty;
            return true;
        }

        return false;
    }
}

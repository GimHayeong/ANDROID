package org.hyg.redrabbitbyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by shiny on 2018-03-02.
 */

public class Cloud {

    // 화면크기
    private int m_scrW, m_scrH;

    // 이미지(구름)의 각각 이동속도
    private int m_speed;

    // 이미지, 이미지(구름)의 위치, 크기
    private Bitmap m_bmp;
    public Bitmap getCloud() { return m_bmp; }
    private float m_x, m_y;
    public float getX() { return m_x; }
    public float getY() { return m_y; }
    private int m_width, m_height;
    public int getWidth() { return m_width; }
    public int getHeight() { return m_height; }

    public Cloud(Context context, int w, int h)
    {
        this(context, w, h, R.drawable.clould1, 50, w * 0.3f, h * 0.2f);
    }

    public Cloud(Context context, int w, int h, int cloudId, int speed, float px, float py){
        m_scrW = w;
        m_scrH = h;

        m_bmp = BitmapFactory.decodeResource(context.getResources(), cloudId);
        m_width = m_bmp.getWidth() / 2;
        m_height = m_bmp.getHeight() / 2;

        m_speed = speed;
        m_x = px;
        m_y = py;
    }

    /**
     * 화면 오른쪽에서 왼쪽으로 이동
     * 화면을 벗어나면 다시 오른쪽에서 나타나 왼쪽으로 이동 반복
     */
    public void moveToNext() {
        m_x -= m_speed * DTime.DeltaTime;

        if(m_x < -m_width){
            m_x = m_scrW + m_width;
        }
    }
}

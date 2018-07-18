package org.hyg.huntingbirdbyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by shiny on 2018-02-21.
 */

public class Sparrow extends Bird{

    //== 공용리소스 대체로 생성자의 context 파라메터 제거
    //public Sparrow(Context context, int  width, int height){
    public Sparrow(int  width, int height){
        m_width = width;
        m_height = height;

        //== 공용리소스 대체로 삭제 makeSpriteImage(context);
        m_bW = CommonBird.BW;
        m_bH = CommonBird.BH;
        // 초기 이미지
        m_bird = CommonBird.Birds[0];



        initSparrow();
    }

    private void initSparrow() {
        Random rnd = new Random();
        m_speed = rnd.nextInt(101) + 700;
        m_aniTime = 0.85f - m_speed / 1000f;
        m_direction.set((float)m_speed, 0f);

        // 초기 위치
        m_bX = -m_bW * 2;
        m_bY = rnd.nextInt(m_height - 500) + 100;
    }



}

/**
 **
 * 공용리소스를 사용하기 전
public class Sparrow extends Bird{

    public Sparrow(Context context, int  width, int height){
        m_width = width;
        m_height = height;

        makeSpriteImage(context);

        initSparrow();
    }

    private void initSparrow() {
        Random rnd = new Random();
        m_speed = rnd.nextInt(101) + 700;
        m_aniTime = 0.85f - m_speed / 1000f;
        m_direction.set((float)m_speed, 0f);

        // 초기 위치
        m_bX = -m_bW * 2;
        m_bY = rnd.nextInt(m_height - 500) + 100;
    }

    private void makeSpriteImage(Context context) {
        // 6개의 동작이 하나로 연결된 이미지
        Bitmap bird = BitmapFactory.decodeResource(context.getResources(), R.drawable.sparrow);

        int bw = bird.getWidth() / 6;
        int bh = bird.getHeight();

        // 6개의 이미지로 분해
        for(int i=0; i<m_birds.length; i++){
            m_birds[i] = Bitmap.createBitmap(bird, bw * i, 0, bw, bh);
        }

        m_bW = bw / 2;
        m_bH = bh / 2;

        // 초기 이미지
        m_bird = m_birds[0];
    }

}
*/
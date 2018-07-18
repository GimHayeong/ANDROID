package org.hyg.bounceballbyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by shiny on 2018-02-22.
 * 공용 리소스 클래스
 */

public class CommonBall {
    // 공 이미지
    static public Bitmap SmBall;

    // 공 반지름
    static public int SmRadius = 80;

    static public void set(Context context){
        SmBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
        SmBall = Bitmap.createScaledBitmap(SmBall, SmRadius * 2, SmRadius * 2, true);
    }

}

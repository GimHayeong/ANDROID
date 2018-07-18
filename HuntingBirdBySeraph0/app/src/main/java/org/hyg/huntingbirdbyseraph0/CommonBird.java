package org.hyg.huntingbirdbyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by shiny on 2018-02-22.
 */

public class CommonBird {

    // 새 크기
    static public int BW, BH;

    static public Bitmap[] Birds = new Bitmap[6];

    static public void set(Context context, int id, int spriteCnt) {
        //Birds = new Bitmap[spriteCnt];
        // 6개의 동작이 하나로 연결된 이미지
        Bitmap bird = BitmapFactory.decodeResource(context.getResources(), id);

        BW = bird.getWidth() / spriteCnt;
        BH = bird.getHeight();

        // 6개의 이미지로 분해
        for(int i=0; i<Birds.length; i++){
            Birds[i] = Bitmap.createBitmap(bird, BW * i, 0, BW, BH);
        }

        BW /= 2;
        BH /= 2;
    }
}

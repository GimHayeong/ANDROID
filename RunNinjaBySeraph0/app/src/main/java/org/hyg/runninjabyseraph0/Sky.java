package org.hyg.runninjabyseraph0;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by shiny on 2018-03-05.
 */

public class Sky extends Field {

    public Sky(Context context, int width, int height){
        super(context, width, height);//

        m_speed = 25;
        m_py = 0;

        setBitmap(context, height);
    }

    /**
     * 동일한 이미지 두개를 연결하여 스크롤하면서 화면 애니메이션 효과
     * @param context
     * @param height
     */
    private void setBitmap(Context context, int height) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.sky);
        m_bmpFields[0] = Bitmap.createScaledBitmap(bmp, m_scrW, (int)(height * 0.4f), true);
        m_bmpFields[1] = m_bmpFields[0];
    }
}

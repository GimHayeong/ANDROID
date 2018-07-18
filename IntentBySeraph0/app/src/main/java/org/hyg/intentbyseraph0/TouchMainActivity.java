package org.hyg.intentbyseraph0;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class TouchMainActivity extends AppCompatActivity {

    private LinearLayout mLayContainer;
    private ImageDisplayView mDisplayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_touch_main);
        //init();

        //CustomView view = new CustomView(this);
        //setContentView(view);

        //CustomViewDrawables view = new CustomViewDrawables(this);
        //setContentView(view);

        PaintBoardView view = new PaintBoardView(this);
        setContentView(view);

    }

    private void init() {
        mLayContainer = (LinearLayout)findViewById(R.id.layContainer);

        Bitmap bmpSource = loadImage();
        if(bmpSource != null){
            mDisplayView = new ImageDisplayView(this);
            mDisplayView.setImageData(bmpSource);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                                                            , LinearLayout.LayoutParams.MATCH_PARENT);
            mLayContainer.addView(mDisplayView, params);
        }
    }

    /**
     * 비트맵 이미지 객체 생성하여 반환
     * @return
     */
    private Bitmap loadImage() {
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.singers);

        return bmp;
    }
}

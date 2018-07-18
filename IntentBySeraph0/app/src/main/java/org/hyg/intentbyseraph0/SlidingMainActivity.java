package org.hyg.intentbyseraph0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SlidingMainActivity extends AppCompatActivity {

    private boolean m_isPageOpen = false;
    private Animation m_animTransLeft;
    private Animation m_animTransRight;
    private LinearLayout m_lotPage;
    private Button m_btnOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_main);

        init();
    }

    private void init() {
        m_lotPage = (LinearLayout)findViewById(R.id.lotPage);
        m_btnOpen = (Button)findViewById(R.id.btnOpen);

        m_animTransLeft = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        m_animTransRight = AnimationUtils.loadAnimation(this, R.anim.translate_right);

        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        m_animTransLeft.setAnimationListener(animListener);
        m_animTransRight.setAnimationListener(animListener);
    }

    public void onButtonClick(View view) {
        try {
            if (m_isPageOpen) {
                m_lotPage.startAnimation(m_animTransRight);
            } else {
                m_lotPage.setVisibility(View.VISIBLE);
                m_lotPage.startAnimation(m_animTransLeft);
            }
        } catch (Exception ex){
            Log.d("OPEN", ex.getMessage());
        }
    }


    private class SlidingPageAnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(m_isPageOpen){
                m_lotPage.setVisibility(View.INVISIBLE);

                m_isPageOpen = false;
                m_btnOpen.setText("Open");
            } else {
                m_isPageOpen = true;
                m_btnOpen.setText("Close");
            }
        }
    }
}

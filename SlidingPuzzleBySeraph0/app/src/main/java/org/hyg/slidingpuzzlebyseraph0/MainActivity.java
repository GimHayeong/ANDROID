package org.hyg.slidingpuzzlebyseraph0;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {


    /**
     *
     *  : 세로 모드 고정
     *  : Statusbar 감추기
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                            , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //getActionBar().hide();

    }

    /**
     * [Back] 키를 누르면 액티비티 종료
     */
    @Override
    public void onBackPressed() {
        finish();
    }
}

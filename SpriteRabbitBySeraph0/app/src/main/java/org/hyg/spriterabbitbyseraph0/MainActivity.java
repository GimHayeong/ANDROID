package org.hyg.spriterabbitbyseraph0;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    GameView w_cvGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 단말기 방향 설정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hide Statusbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide Actionbar
        //getSupportActionBar().hide();

        setTitle("엽기 토끼");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 액션버튼에 토글기능 추가
                w_cvGameView.isRun = !w_cvGameView.isRun;
            }
        });

        w_cvGameView = (GameView)findViewById(R.id.cvGameView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;

        String[] mnuString = {"Handler 시작", "Handler 중지", "프로그램 종료"};
        int grpNo = 0, mnuNo = 0;
        for(int i=0; i<mnuString.length; i++) {
            menu.add(grpNo, ++mnuNo, i, mnuString[i]);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //return super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case 1:
                // 동작 시작
                w_cvGameView.isRun = true;
                w_cvGameView.m_hdlrRun.sendEmptyMessageDelayed(0, 10);
                break;

            case 2:
                // 동작 중지
                w_cvGameView.isRun = false;
                w_cvGameView.m_hdlrRun.removeMessages(0);
                break;

            case 3:
                // 프로그램 종료
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        // 백버튼 프로그램 종료기능 추가
        finish();
    }
}

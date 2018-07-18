package org.hyg.intentbyseraph0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 *
 *
 */
public class ThreadMainActivity extends AppCompatActivity {

    public static boolean mIsRun = true;
    public static int mValue = 0;
    public TextView mTxtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_main);

        mTxtResult = (TextView)findViewById(R.id.txtResult);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxtResult.setText("From Thread: " + mValue);
            }
        };
        ((Button)findViewById(R.id.btnThread)).setOnClickListener(OnButtonClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mIsRun = true;

        Thread thread = new BackgroundThread();
        thread.start();
    }



    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mIsRun = false;
        mValue = 0;
    }

    /**
     * 안드로이드에서 UI에 응답을 보내주는 방식
     *  1. Thread : 동일 프로세스 내에서 동시 작업 단위.
     *    - 작업 수행의 결과를 바로 처리 가능.
     *    - UI 객체는 직접 접근 불가 => Handler 객체 사용하면 UI 객체 직접 접근 가능
     *  2. Service : 백그라운드 작업은 서비스로 실행하고 사용자에게 알림메시지 보냄.
     *    - MainActivity 로 결과값을 전달하고 이를 이용해 다른 작업을 수행할 목적이라면
     *      BroadCasting 을 이용해 결과값 전달 가능.
     */
    class BackgroundThread extends Thread{
        public void run(){
            while(mIsRun){
                try{
                    sleep(1000);
                    mValue++;
                } catch (InterruptedException e) {
                    mIsRun = false;
                }
            }
        }

    }
}






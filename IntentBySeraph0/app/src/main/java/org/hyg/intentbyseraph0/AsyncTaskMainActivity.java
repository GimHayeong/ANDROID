package org.hyg.intentbyseraph0;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class AsyncTaskMainActivity extends AppCompatActivity {

    private BackgroundAsyncTask mBgAsyncTask;
    private int mValue;
    Button mBtnExecute, mBtnCancel;
    TextView mTxtMsgAsync;
    ProgressBar mBarProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task_main);

        mBtnExecute = (Button)findViewById(R.id.btnExecute);
        mBtnCancel = (Button)findViewById(R.id.btnCancel);
        mTxtMsgAsync = (TextView)findViewById(R.id.txtMsgAsync);
        mBarProgress = (ProgressBar)findViewById(R.id.barProgress);

        init();

    }

    private void init() {
        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.btnExecute:
                        mBgAsyncTask = new BackgroundAsyncTask();
                        mBgAsyncTask.execute(100);
                        break;

                    case R.id.btnCancel:
                        mBgAsyncTask.cancel(true);
                        break;
                }
            }
        };
        mBtnExecute.setOnClickListener(OnButtonClick);
        mBtnCancel.setOnClickListener(OnButtonClick);
    }

    /**
     * 핸들러 대신 AsyncTask 클래스를 상속하여 스레드를 위한 코드와 UI 접근 코드를 한꺼번에 처리 가능
     */
    private class BackgroundAsyncTask extends AsyncTask<Integer, Integer, Integer>{

        /**
         * 별도 생성 스레드에서 실행되어야 할 코드 (2. 백그라운드 작업)
         * @param integers : - execute() 메서드 호출시 사용된 파라미터
         * @return : onPostExecute() 함수로 전달할 값.
         */
        @Override
        protected Integer doInBackground(Integer... integers) {
            final int taskCnt = integers[0];

            while(!isCancelled()){
                mValue++;

                if(mValue >= taskCnt) { break; }

                publishProgress(mValue);

                delayJob();
            }

            return taskCnt;
        }

        private void delayJob(){
            delayJob(100);
        }
        private void delayJob(int i) {
            try{
                sleep(i);
            } catch (InterruptedException e) { /* ... */ }
        }


        /**
         * 메인 스레드에서 실행되는 코드 (1. 백그라운드 작업 전 호출)
         *  - 초기화 작업
         */
        @Override
        protected void onPreExecute() {
            mBtnExecute.setEnabled(false);
            mBtnExecute.setClickable(false);
            mValue = 0;
            mBarProgress.setProgress(mValue);
            mTxtMsgAsync.setText("AsyncTask를 이용한 백그라운드 작업 시작...");
        }

        /**
         * 메인 스레드에서 실행되는 코드 (3. 백그라운드 작업 진행상태에 따른 UI 반영위해 호출)
         *  - 백그라운드 작업 중간에 publishProgress() 메서드로 호출
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("BgProgress: ", "" + mValue);
            mBarProgress.setProgress(values[0].intValue());
            mTxtMsgAsync.setText("Current Value: " + values[0].toString());
        }

        /**
         * 메인 스레드에서 실행되는 코드 (4. 백그라운드 작업 종료후 호출)
         *  - 메모리 리소스 해제 등 작업
         * @param integer : 백그라운드 작업의 결과. doInBackground() 메서드로부터 전달받은 값
         */
        @Override
        protected void onPostExecute(Integer integer) {
            mBarProgress.setProgress(mValue);
            mTxtMsgAsync.setText("Finished.");
            mBtnExecute.setEnabled(true);
            mBtnExecute.setClickable(true);
        }

        @Override
        protected void onCancelled() {
            mBarProgress.setProgress(mValue);
            mTxtMsgAsync.setText("Cancelled.");
            mBtnExecute.setEnabled(true);
            mBtnExecute.setClickable(true);
        }
    }
}

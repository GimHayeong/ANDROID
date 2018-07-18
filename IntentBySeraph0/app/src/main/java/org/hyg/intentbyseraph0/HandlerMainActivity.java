package org.hyg.intentbyseraph0;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class HandlerMainActivity extends AppCompatActivity {

    /**
     * Main Activity 의 Handler
     */
    private Handler mHandler;
    /**
     * Main Activity 의 Thread
     */
    private Thread mThread;
    private TextView mTxtResult;
    private ProgressBar mBarResult;
    private Button mBtnRequest;
    private EditText mEdtInput;

    private long mLastClickedTime;
    private int mCount = 20;
    private boolean mIsRun = true;

    private ProgressRunnable mRunnable;

    private int mIdxJob = 0;
    private boolean mIsClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_main);

        mTxtResult = (TextView)findViewById(R.id.txtResult);
        mBarResult = (ProgressBar)findViewById(R.id.barResult);
        mBtnRequest = (Button)findViewById(R.id.btnRequest);
        mEdtInput = (EditText)findViewById(R.id.edtInput);

        initJob(3);
    }

    private void initJob(int jobIndex) {
        mIdxJob = jobIndex;

        switch (mIdxJob){
            case 0:
                initProgressHandler();
                break;

            case 1:
                initProgressRunnableWithHandler();
                break;

            case 2:
                initRequestAfterDelay();
                break;

            case 3:
                initRequestAfterDelay(0);
                break;

            case 4:
                initLooperThread();
                break;
        }
    }

    private void initLooperThread() {
        mHandler = new LooperMainHandler();
        mThread = new LooperWithHandlerThread();

        mBtnRequest.setOnClickListener(new View.OnClickListener() {
            /**
             * 메인 스레드가 아닌 별도 생성된 스레드 내의 핸들러로 메시지 전송
             * @param view
             */
            @Override
            public void onClick(View view) {
                String strInput = mEdtInput.getText().toString();
                Message msgSendToHandlerInThread = Message.obtain();
                msgSendToHandlerInThread.obj = "[Me] " + strInput;

                ((LooperWithHandlerThread)mThread).getHandler().handleMessage(msgSendToHandlerInThread);
            }
        });

        mThread.start();
    }

    private void initProgressHandler() {
        mHandler = new ProgressHandler();
    }

    private void initProgressRunnableWithHandler() {
        mHandler = new Handler();
        mRunnable = new ProgressRunnable();
    }

    /**
     * MainActivity 에서 10초 지연후 UI 변경
     */
    private void initRequestAfterDelay() {
        mBtnRequest.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String dlgTitle = "원격요청";
                String dlgMsg = "데이터를 요청하시겠습니까?";
                String dlgButtonYes = "예";
                String dlgButtonNo = "아니오";
                AlertDialog dlg = createRequestDialog(dlgTitle, dlgMsg, dlgButtonYes, dlgButtonNo);

                mTxtResult.setText("원격 데이터 요청 중 ... \r\n컴퓨터를 끄지 말고 기다려 주십시오.");
            }
        });
    }

    /**
     * MainActivity 에서 10초 지연후 UI 변경 => 팝업창 바로 닫힘
     */
    private void initRequestAfterDelay(final int jobId) {

        mBtnRequest.setOnClickListener(new OnDisableClickListener() {
            @Override
            public void onDisableClick(View view) {
                String dlgTitle = "원격요청";
                String dlgMsg = "데이터를 요청하시겠습니까?";
                String dlgButtonYes = "예";
                String dlgButtonNo = "아니오";
                AlertDialog dlg = createRequestDelayedDialog(dlgTitle, dlgMsg, dlgButtonYes, dlgButtonNo);

                mTxtResult.setText("원격 데이터 요청 중 ... \r\n컴퓨터를 끄지 말고 기다려 주십시오.");
            }
        });

        /*
        mBtnRequest.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(mBtnRequest.isEnabled()) {
                    mBtnRequest.setEnabled(false);
                    mBtnRequest.setClickable(false);
                } else {
                    return;
                }
                String dlgTitle = "원격요청";
                String dlgMsg = "데이터를 요청하시겠습니까?";
                String dlgButtonYes = "예";
                String dlgButtonNo = "아니오";
                AlertDialog dlg = createRequestDelayedDialog(dlgTitle, dlgMsg, dlgButtonYes, dlgButtonNo);

                mTxtResult.setText("원격 데이터 요청 중 ... \r\n컴퓨터를 끄지 말고 기다려 주십시오.");
            }
        });
        */
    }



    /**
     * AlertDialog 객체를 만들어 화면에 띄운 후,
     * [예] 버튼을 누르면 10초 동안 서버로부터의 응답을 기다린 후 처리하여 UI 멈춤 현상 개선효과
     *
     * @param dlgTitle : Dialog Title
     * @param dlgMsg : Dialog Message
     * @param dlgButtonYes : PositiveButton
     * @param dlgButtonNo : NegativeButton
     * @return : AlertDialog.show()
     */
    private AlertDialog createRequestDialog(CharSequence dlgTitle, CharSequence dlgMsg, CharSequence dlgButtonYes, CharSequence dlgButtonNo) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle(dlgTitle);
        dlg.setMessage(dlgMsg);
        dlg.setPositiveButton(dlgButtonYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //:: 10초 지연
                for(int k=0; k<10; k++){
                    try{
                        // TODO: 서버로부터의 응답을 기다림
                        sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO:
                    }
                }

                mTxtResult.setText("원격 데이터 요청 완료");
            }
        });

        dlg.setNegativeButton(dlgButtonNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTxtResult.setText("원격 데이터 요청 취소");
            }
        });

        //:: AlertDialog.show() 를 리턴.
        return dlg.show();
    }

    /**
     * AlertDialog 객체를 만들어 화면에 띄운 후,
     * [예] 버튼을 누르면 10초 동안 서버로부터의 응답을 기다린 후 처리하여 UI 멈춤 현상 개선효과
     * [예] 버튼이 눌려진 상태로 네트워킹 처리시간동안 팝업창이 사라지지 않는 현상 개선
     * @param dlgTitle : Dialog Title
     * @param dlgMsg : Dialog Message
     * @param dlgButtonYes : PositiveButton
     * @param dlgButtonNo : NegativeButton
     * @return : AlertDialog.show()
     */
    private AlertDialog createRequestDelayedDialog(CharSequence dlgTitle, CharSequence dlgMsg, CharSequence dlgButtonYes, CharSequence dlgButtonNo) {
        mHandler = new RequestHandler();
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle(dlgTitle);
        dlg.setMessage(dlgMsg);
        dlg.setPositiveButton(dlgButtonYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mHandler.sendEmptyMessageDelayed(0, 20);
            }
        });

        dlg.setNegativeButton(dlgButtonNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTxtResult.setText("원격 데이터 요청 취소");
                if(!mBtnRequest.isEnabled()) {
                    mBtnRequest.setClickable(true);
                    mBtnRequest.setEnabled(true);
                }
            }
        });

        //:: AlertDialog.show() 를 리턴.
        return dlg.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startThread();
    }

    private void startThread() {
        switch (mIdxJob){
            case 0:
                startThreadWidthHandler();
                break;

            case 1:
                startThreadWithRunnable();
                break;

            case 3:
                break;

            case 4:
                break;

            default:
                break;
        }
    }

    private void startThreadWithRunnable() {
        mBarResult.setProgress(0);
        Thread thread = new Thread(new Runnable(){
            private final int cnt = 0;
            @Override
            public void run() {
                while(mIsRun && cnt < mCount){
                    try{
                        sleep(1000);
                        // post()만 호출
                        mHandler.post(mRunnable);

                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }
                }
            }
        });

        mIsRun = true;
        thread.start();
    }

    private void startThreadWidthHandler() {
        mBarResult.setProgress(0);
        Thread thread = new Thread(new Runnable() {
            private final int cnt = 0;
            @Override
            public void run() {
                while(mIsRun && cnt < mCount){
                    try{
                        sleep(1000);
                        // Message 객체를 보낸다.
                        Message msg = mHandler.obtainMessage();
                        mHandler.sendMessage(msg);

                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }
                }
            }
        });

        mIsRun = true;
        thread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mIsRun = false;
    }

    @Override
    public void onDetachedFromWindow() {
        mIsRun = false;
        super.onDetachedFromWindow();
    }




    /**
     * ProgressHandler
     *  - 초기화 : initProgressHandler() 메서드
     *  - 스레드 호출 : startThreadWidthHandler() 메서드
     */
    private class ProgressHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            mBarResult.incrementProgressBy(5);
            if(mBarResult.getProgress() == mBarResult.getMax()){
                mTxtResult.setText("Handler done");
            } else {
                mTxtResult.setText("From Thread to Handler ... " + mBarResult.getProgress());
            }
        }
    }


    /**
     * ProgressRunnable
     *  - 초기화 : initProgressRunnableWithHandler() 메서드
     *  - 스레드 호출 : startThreadWithRunnable() 메서드
     */
    private class ProgressRunnable implements Runnable {

        @Override
        public void run() {
            mBarResult.incrementProgressBy(5);
            if(mBarResult.getProgress() == mBarResult.getMax()){
                mTxtResult.setText("Runnable done");
            } else {
                mTxtResult.setText("From Thread to Runnable ... " + mBarResult.getProgress());
            }
        }
    }


    /**
     * AlertDialog 에서 [예] 버튼이 눌려 있는 상태로 팝업이 열린상태로 10초 대기하는 현상 개선
     *  - 대화상자가 사라지기 전에 네트워킹 기능이 수행되는 시간으로 UI 갱신 지연으로 발생되는 문제
     *    (해결1) sendMessageAtTime(msg, atTime) : 특정 시간에 원격 요청
     *    (해결2) sendMessageDelayed(msg, delayTime) : 일정 시간 후에 원격 요청
     */
    private class RequestHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            for(int i=0; i<10; i++){
                try{
                    sleep(1000);
                } catch (InterruptedException e) {
                    //
                }
            }
            mTxtResult.setText("원격 데이터 요청 완료 ...");
            mBtnRequest.setClickable(true);
            mBtnRequest.setEnabled(true);
        }
    }


    /**
     * AlertDialog 에서 [예] 버튼이 눌려 있는 상태로 10초 대기하는 현상 개선
     *  - 대화상자가 사라지기 전에 네트워킹 기능이 수행되는 시간으로 UI 갱신 지연으로 발생되는 문제
     *    (해결1) postAtTime(msg, atTime) : 특정 시간에 원격 요청
     *    (해결2) postDelayed(msg, delayTime) : 일정 시간 후에 원격 요청
     */
    private class RequestRunnable implements Runnable {

        @Override
        public void run() {
            for(int i=0; i<10; i++){
                try{
                    sleep(1000);
                } catch (InterruptedException e) {
                    //
                }
            }
            mTxtResult.setText("원격 데이터 요청 완료");
        }
    }


    /**
     * 별도 생성 스레드 *
     *
     *  루펴(Looper) : 무한 순환방식으로 메시지큐에 들어오는 메시지를 하나씩 실행
     *   - 메인 스레드는 메시지큐와 루퍼를 내부적으로 처리하므로 핸들러를 통해 메시지 전달 가능
     *  별도 스레드 생성 : 메시지 전송 방식으로 스레드에 데이터 전달후 순차적 작업 수행을 위해 루퍼 생성 필요
     *  - 메인 스레드가 아닌, 별도의 스레드를 생성한 경우 루퍼가 없으므로 루퍼 생성후 실행.
     */
    private class LooperWithHandlerThread extends Thread {
        /**
         * 별도 생성 스레드 내에서 메시지 전송 핸들러
         */
        private LooperHandler mLPHandler;
        public LooperHandler getHandler() { return mLPHandler; }

        public LooperWithHandlerThread() {
           mLPHandler = new LooperHandler();
        }

        /**
         * 별도 생성 스레드는 루퍼가 없으므로 run() 메서드에서 루퍼 생성 및 실행.
         */
        @Override
        public void run() {
            Looper.prepare();
            Looper.loop();
        }
    }


    /**
     * 별도 생성 스레드 내의 핸들러 *
     */
    private class LooperHandler extends Handler {

        /**
         * 스레드 내의 핸들러에서 메인 스레드의 핸들러(mHandler)로 메시지 전송
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            Message msgSendToHandlerInMainThread = Message.obtain();
            msgSendToHandlerInMainThread.obj = msg.obj + "\r\n[from Mike] Good Morning!";

            mHandler.sendMessage(msgSendToHandlerInMainThread);
        }
    }


    /**
     * 메인 스레드 내의 핸들러 *
     */
    private class LooperMainHandler extends Handler {
        /**
         * LooperHandler 에서 보낸 메시지를 받아 메인 스레드의 핸들러 내에서 UI 처리
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            String str = (String)msg.obj;
            mTxtResult.setText(str);
        }
    }
}

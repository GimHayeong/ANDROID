package org.hyg.intentbyseraph0;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 자바 서버에 접속할 안드로이드 클라이언트
 *  - Activity 에 접근하기 위해 Handler 필요
 *  - 메니페스트에 <use-permission android:name="android.permission.INTERNET"></use-permission> 추가 필요
 */
public class AndroidSocketClientMainActivity extends AppCompatActivity {

    private static final String TAG = "AndroidSocketClientMain";

    /**
     * MainActivity(Main Thread) 의 UI 로 별도 생성 스레드(RequestThread)가 접근하려면 Handler 필요
     */
    Handler mHandler = new Handler();;

    private Button mBtnConnectServer;
    private EditText mEdtServerIP;
    private TextView mTxtMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_socket_client_main);

        init();
    }

    private void init() {
        mBtnConnectServer = (Button)findViewById(R.id.btnConnectServer);
        mEdtServerIP = (EditText)findViewById(R.id.edtServerIp);
        mTxtMsg = new TextView(this);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //:: request(); ==> Thread 를 만들어 서버 접속 및 응답 대기
                RequestThread thread = new RequestThread();
                thread.start();
            }
        };
        mBtnConnectServer.setOnClickListener(OnButtonClick);
    }


    /**
     * 사용자로부터 서버 연결 요청을 받은 경우(버튼 터치 이벤트), 연결 요청 처리
     * (응답 시간을 예측할 수 없으므로 Thread 를 만들어 사용) ==> Thread 로 메서드 위치 이동
     *  - 소켓 연결을 위한 Socket 객체 생성
     *  - 데이터 쓰기 목적 스트림 객체 생성
     *  - 데이터 읽기 목적 스트림 객체 생성
     *
    private void request() {
        try{
            int port = 5001;
            //:: 접속할 서버 (localhost = 클라이언트와 같은 컴퓨터에 있는 서버로 접속)
            //:: 안드로이드는 실행되고 있는 컴퓨터를 localhost를 찾을 수 없으므로 서버 프로그램이 실행되고 있는 IP 정보를 확인해서 입력
            String serverIp = "xxx.xxx.xxx";

            //:: 클라이언트 소켓
            Socket socket = new Socket(serverIp, port);

            //:: Client ==> Server
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.writeUTF("Hello Android Town. - from Client.");
            outStream.flush();

            //:: Client <== Server
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
            Log.d(TAG, inStream.readUTF());

            socket.close();

        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    */



    /**
     * 서버 접속을 위한 스레드 필수
     *  - 소켓 연결을 위한 Socket 객체 생성
     *  - 쓰기 목적 스트림 객체 생성
     *  - 읽기 목적 스트림 객체 생성(읽은 데이터 로그 기록)
     *  - 스트림 닫기
     */
    class RequestThread extends Thread {

        @Override
        public void run() {
            request();
        }

        /**
         * 사용자로부터 서버 연결 요청을 받은 경우(버튼 터치 이벤트), 연결 요청 처리
         * (응답 시간을 예측할 수 없으므로 Thread 를 만들어 사용) ==> Thread 로 메서드 위치 이동됨
         * MainActivity(Main Thread) 의 UI 로 별도 생성 스레드(RequestThread)가 접근하려면 Handler 필요
         *  - 소켓 연결을 위한 Socket 객체 생성
         *  - 데이터 쓰기 목적 스트림 객체 생성
         *  - 데이터 읽기 목적 스트림 객체 생성
         */
        private void request() {
            try{
                int port = 5001;
                //:: 접속할 서버 (localhost = 클라이언트와 같은 컴퓨터에 있는 서버로 접속)
                //:: 안드로이드는 실행되고 있는 컴퓨터를 localhost를 찾을 수 없으므로 서버 프로그램이 실행되고 있는 IP 정보를 확인해서 입력
                String serverIp = "localhost";

                //:: 클라이언트 소켓
                Socket socket = new Socket(serverIp, port);

                //:: Client ==> Server
                ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
                outStream.writeUTF("Hello Android Town. - from Client.");
                outStream.flush();

                //:: Client <== Server
                ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
                setTextViewInMainThread(inStream.readUTF());

                socket.close();

            } catch(Exception ex){
                ex.printStackTrace();
            }
        }

        /**
         * 별도 생성 스레드에서 메인 스레드의 UI 속성에 접근하기 위해 Handler 이용
         * @param msg
         */
        private void setTextViewInMainThread(String msg){
            final String mMsg = msg;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTxtMsg.append(mMsg + "\n");
                }
            });
        }
    }
}

package org.hyg.intentbyseraph0;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Http 로 접속해 웹사이트의 HTML 데이터를 읽어 오는 메인 엑티비티
 *  - Http 프로토콜 : 요청할 때 내부적으로 소켓 연결을 새로 하고 응답을 받으면 연결 끊음.
 *  - HttpURLConnection 타압으로 형변환하여 URLConnection 객체 생성 : openConnection() 메서드 이용.
 *  - 전송 방식 : setRequestMethod() 메서드 이용
 *  - 전송 파라메터 : setRequestProperty() 메서드 이용
 */
public class HttpMainActivity extends AppCompatActivity {

    private static String mDefaultUrl = "http://dic.daum.net/search.do";
    /**
     * 별도 생성 스레드에서 메인 스레드의 UI 접근 목적 핸들러
     */
    private final Handler mHandler = new Handler();

    private EditText mEdtUrl;
    private Button mBtnRequest;
    private TextView mTxtHtmlTag;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_main);

        init();
    }

    private void init() {
        mEdtUrl = (EditText)findViewById(R.id.edtUrl);
        mBtnRequest = (Button)findViewById(R.id.btnRequest);
        mTxtHtmlTag = (TextView)findViewById(R.id.txtHtmlTag);
        mWebView = (WebView)findViewById(R.id.wvHtmlView);

        mEdtUrl.setText(mDefaultUrl);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String url = mEdtUrl.getText().toString();

                ConnectHttpThread thread = new ConnectHttpThread(url);
                thread.start();
            }
        };
        mBtnRequest.setOnClickListener(OnButtonClick);
    }

    class ConnectHttpThread extends Thread{
        private String mUrl;
        private final Handler mHandler = new Handler();

        public ConnectHttpThread(String address){
            mUrl = address;
            Log.d("ConnectHttpThread:", address);
        }

        @Override
        public void run() {
            try{
                final String output = request(mUrl);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("ConnectHttpThread:", "request message: " + output);
                        mTxtHtmlTag.setText(output);

                        WebSettings webSet = mWebView.getSettings();
                        webSet.setJavaScriptEnabled(true);
                        mWebView.loadData(output, "text/html", "utf-8");

                    }
                });

            } catch (Exception ex){ Log.e("ConnectHttpThread", "run()", ex); }
        }

        /**
         *  - 파라메터로 전달받은 웹사이트 주소로 URL 객체 생성
         *  - URL 객체의 openConnection() 메서드로 서버로 접속할 URLConnection 을 HttpURLConnection 타입으로 형변환한 객체 생성
         *  - getResponseCode() 메서드를 통해 서버에 접속해서 정보 요청
         *  - getInputStream() 메서드를 통해 응답결과 읽기 위한 스트림 객체 생성(줄 단위로 읽기 위해 BufferReader 객체 생성)
         *  - while() 문에서 결과 문자열 읽기
         * @param address : Url(웹사이트 주소)
         * @return : 웹 서버로부터 받은 HTML 태그
         */
        private String request(String address) {
            StringBuilder sb = new StringBuilder();
            try{
                //:: 웹서버에 연결
                URL url = new URL(address);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                if(conn != null) {
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setConnectTimeout(10000);

                    conn.setRequestProperty("q", "응답");
                    conn.setRequestProperty("dic", "eng");
                    conn.setRequestProperty("search_first", "Y");

                    int resCode = conn.getResponseCode();
                    if(resCode == HttpURLConnection.HTTP_OK){
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        String line = null;
                        while(true){
                            line = reader.readLine();
                            if (line == null) { break; }
                            //:: 한 줄씩 메인 스레드의 UI에 반영 setTextViewInMainThread(line);
                            sb.append(line + "\n");
                        }

                        reader.close();
                    }
                    conn.disconnect();
                }
            } catch (MalformedURLException e) {
                Log.e("Http", "Exception in processing response.", e);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //:: 전체 데이터를 서버로부터 받아 메인 스레드의 UI에 반영
            return sb.toString();
        }

        /**
         * HttpURL 대신 HttpClient 사용하는 예
         * @return
         *
        private String request(){
            StringBuilder sb = new StringBuilder();
            String url = "http://m.naver.com";
            try{
                //:: 웹 클라이언트
                HttpClient client = new DefaultHttpClient();

                //:: POST 방식으로 호출
                HttpPost httpPost = new HttpPost(url);

                //:: POST 방식으로 전달할 파라메터
                List<NameValuePair> fields = new ArrayList<NameValuePair>(1);
                fields.add(new BasicNameValuePair("data", "test"));
                httpPost.setEntity(new UrlEncodedFormEntity(fields));

                HttpResponse response = client.execute(httpPost);

                //:: 응답
                InputStream inStream = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                String line = null;
                while(true){
                    line = reader.readLine();
                    if(line == null) { break; }
                }

                sb.append(line);
            } catch (Exception ex) {

            }

            return sb.toString();
        }
        */



        /**
         * 별도 생성 스레드에서 메인 스레드의 UI 속성에 접근하기 위해 Handler 이용
         * @param msg
         */
        private void setTextViewInMainThread(String msg){
            /*final String mMsg = msg;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTxtHtmlTag.setText(mMsg + "\n");
                }
            });
            */
        }


    }
}

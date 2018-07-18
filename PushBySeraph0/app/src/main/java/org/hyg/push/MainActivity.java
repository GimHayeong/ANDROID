package org.hyg.push;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    /**
     * Volley 객체를 만들고 요청을 위한 데이터 설정 인터페이스
     */
    public interface SendResponseListener {
        public void onRequestStarted();
        public void onRequestCompleted();
        public void onRequestWithError(VolleyError error);
    }

    private final String TAG = "MAIN";

    private EditText mEdtMessage;
    private Button mBtnSend;
    private TextView mTxtMessage, mTxtLog;

    /**
     * 단말의 등록 ID
     */
    private String mRegId;

    /**
     * 데이터 전송에 사용하는 Volley의 큐 객체
     *  - Volley.newRequestQueue() 메서드로 큐 객체 생성
     */
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
    }

    private void init() {
        mEdtMessage = (EditText)findViewById(R.id.edtMessage);
        mBtnSend = (Button)findViewById(R.id.btnSend);
        mTxtMessage = (TextView)findViewById(R.id.txtMessage);
        mTxtLog = (TextView)findViewById(R.id.txtLog);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = mEdtMessage.getText().toString();
                send(input);
            }
        };
        mBtnSend.setOnClickListener(OnButtonClick);

        mQueue = Volley.newRequestQueue(getApplicationContext());
    }

    /**
     * 등록 ID 확인하여 변수에 저장
     */
    public void confirmRegistrationId() {
        mRegId = FirebaseInstanceId.getInstance().getToken();
    }

    /**
     * 서비스로부터 인덴트를 받았을 때 처리
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        if(intent != null){
            processIntent(intent);
        }

        super.onNewIntent(intent);
    }

    /**
     *
     * @param intent
     */
    private void processIntent(Intent intent) {
        String from = intent.getStringExtra("from");
        if(from == null) { Log.d(TAG, "from is null."); return;}

        String contents = intent.getStringExtra("contents");
        Log.d(TAG, "[Data] from : " + from + " , " + contents);
        mTxtMessage.setText("[" + from + "]로부터 수신한 데이터 : " + contents);
    }

    /**
     * JSON 객체에 담아 Google Play 클라우드 서버에 보내어 메시지를 받을 단말에 전송
     * @param input : 보낼 메시지
     */
    private void send(String input) {
        /**
         * 전송할 JSON 객체
         */
        JSONObject json;
        /**
         * 전송 메시지
         */
        JSONObject data;
        /**
         * 수신 단말의 등록된 ID
         */
        JSONArray regIds;

        json = new JSONObject();

        try{
            json.put("priority", "high");

            data = new JSONObject();
            data.put("contents", input);

            json.put("data", data);

            regIds = new JSONArray();
            regIds.put(0, mRegId);

            json.put("registration_ids", regIds);

        } catch (Exception ex) {
            mTxtLog.append("Ex" +
                    ":" +
                    ex.getMessage());
        }

        sendData(json, new SendResponseListener(){

            @Override
            public void onRequestStarted() {
                //TODO:
                mTxtLog.append("RequestStart()");
            }

            @Override
            public void onRequestCompleted() {
                //TODO:
                mTxtLog.append("onRequestCompleted()");
            }

            @Override
            public void onRequestWithError(VolleyError error) {
                //TODO:
                mTxtLog.append("onRequestWithError()" + error.getMessage());
            }
        });
    }

    /**
     * 클라우드 서버로 메시지 전송
     *   - 방법1) Volley 라이브러리 :
     *           JsonObjectRequest(요청방식
     *                           , 클라우드서버주소
     *                           , 요청데이터를 담고 있는 객체
     *                           , 성공응답리스너
     *                           , 오류응답리스너) { 메서드 재정의 ... };
     *   - 방법2) oKHttp 라이브러리
     * @param json : 요청데이터를 담고 있는 객체
     * @param listener
     */
    private void sendData(JSONObject json, final SendResponseListener listener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST
                , "http://fcm.gooleapis.com/fcm/send"
                , json
                , new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            listener.onRequestCompleted();
                        }
                    }
                , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onRequestWithError(error);
                    }
                }){
            /**
             * 요청을 위한 파라미터 설정 및 반환 메서드 재정의
             * @return
             * @throws AuthFailureError
             */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            /**
             * 요청을 위한 헤더 설정 및 반환 메서드 재정의
             * @return
             * @throws AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "key=AAAAEtv5380:APA91bGkFcnpRbs3_TDTSfUeE4x4v3D9p9-FBHdNQnYgs0ZW6N73TwZJpvCjoLhCIweLgxC46EgH8XPQL_QhpPauqMm7PnvhzBMCI7ekD4X4-rQEaRTtIwoHKPD3eUGiE7Pn0oqdTzG5");

                return headers;
            }

            /**
             * 컨텐츠 타입 반환 메서드 재정의
             * @return
             */
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setShouldCache(false);
        listener.onRequestStarted();;
        mQueue.add(request);
    }




}

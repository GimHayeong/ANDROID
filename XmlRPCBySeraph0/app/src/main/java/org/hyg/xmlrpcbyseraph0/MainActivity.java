package org.hyg.xmlrpcbyseraph0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.nio.channels.IllegalBlockingModeException;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "XML-RPC";
    public static final String SQL_STATEMENT = "SELECT * FROM tbl_SAMPLE";

    /**
     * 호출할 함수명
     */
    private String mFuncName = "echo";

    /**
     * 접속할 서버 아이피
     */
    private String mHostIp = "121.124.17.87";

    /**
     * 결과 표시
     */
    private TextView mTxtResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mTxtResult = (TextView)findViewById(R.id.txtResult);

        /**
         * Connect 버튼 클릭 이벤트
         */
        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch();
            }
        };
        ((Button)findViewById(R.id.btnConnect)).setOnClickListener(OnButtonClick);
    }

    /**
     *
     */
    private void launch() {
        try{
            //IBMLClient client = new IBMLClient();

            Vector params = new Vector();
            params.add("Hello Android Town!");

            /**
             * echo.execute(params) 호출
             */
            Vector response = null;
            //response = (Vector)client.execute(mFuncName + ".execute", params);

            processResponse(response);

        } catch (Exception ex){ }
    }

    private void launchForMDB(){
        try{
            //IBML.setKeepAlive(true);
            //MDBCCallback callback = new MDBCCallback();

            //IBMLClient client = new IBMLClient(mHostIp);

            Vector params = new Vector();

            Integer cid = new Integer(10001);
            Integer sid = new Integer(10001);
            params.add(cid);
            params.add(sid);
            params.add(SQL_STATEMENT);

            /**
             * echo.executeAsync(params) 호출하여 별도 스레드에서 응답을 기다리다 응답이 오면 callback 함수 호출
             */
            //client.executeAsync(mFuncName + ".execute", params, callback);

        } catch (Exception ex) { }
    }

    /**
     *
     * @param response
     */
    private void processResponse(Vector response) throws IllegalBlockingModeException {
        for(int i=0; i<response.size(); i++){
            Object obj = response.get(i);
            String msg;
            if(obj instanceof String){
                msg = "#" + i + " (String):" + obj;
            } else if(obj instanceof IllegalBlockingModeException) {
                IllegalBlockingModeException ex = (IllegalBlockingModeException)obj;
                //msg = "#" + i + " (IBMLError):" + ex.getCode() + ", " + ex.getMessage();
            } else {
                msg = "#" + i + ":" + response.get(i);
            }
        }
    }
}

package org.hyg.intentbyseraph0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SmsActivity extends AppCompatActivity {

    EditText edtRcvPhone, edtRcvMsg, edtRcvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        edtRcvPhone = (EditText)findViewById(R.id.edtRcvPhone);
        edtRcvMsg = (EditText)findViewById(R.id.edtRcvMsg);
        edtRcvDate = (EditText)findViewById(R.id.edtRcvDate);

        Button btnOk = (Button)findViewById(R.id.btnOk);

        View.OnClickListener onButtonClick = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        };

        btnOk.setOnClickListener(onButtonClick);

        Intent intent = getIntent();
        getSmsData(intent);
    }

    /**
     * 메모리에 SmsActivity 가 생성된 경우 onCreate 메서드가 아닌 onNewIntent 메서드에서 Intent 객체 데이터 로드
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        getSmsData(intent);

        super.onNewIntent(intent);
    }

    private void getSmsData(Intent intent) {
        if(intent != null){
            String sender = intent.getStringExtra("sender");
            String contents = intent.getStringExtra("contents");
            String receivedDate = intent.getStringExtra("receivedDate");

            edtRcvPhone.setText(sender);
            edtRcvMsg.setText(contents);
            edtRcvDate.setText(receivedDate);
        }
    }


}

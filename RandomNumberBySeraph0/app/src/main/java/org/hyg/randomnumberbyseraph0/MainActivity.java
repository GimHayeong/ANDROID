package org.hyg.randomnumberbyseraph0;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random m_rnd = new Random();
    int m_cnt;
    int m_rndNum;

    EditText m_edNum;
    TextView m_txtCnt;
    TextView m_txtResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("숫자 맞추기");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.fab).setOnClickListener(onButtonClick);
        findViewById(R.id.btnOK).setOnClickListener(onButtonClick);

        m_edNum = findViewById(R.id.edtNum);
        m_txtCnt = findViewById(R.id.txtCnt);
        m_txtResult = findViewById(R.id.txtHint);

        init();
    }

    private void init() {
        m_rndNum = m_rnd.nextInt(501)+500;
        m_cnt = 0;
        clearFields();
    }

    private void clearFields() {
        m_txtCnt.setText("입력횟수: " + m_cnt);
        m_txtResult.setText("");
        m_edNum.setText("");
    }

    Button.OnClickListener onButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.fab:
                    init();
                    break;
                case R.id.btnOK:
                    checkValue();
                    break;
            }
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    };

    private void checkValue() {
        String strTmp = m_edNum.getText().toString();
        if(strTmp.equals("")){
            m_txtResult.setText("500~1000 사이의 숫자를 입력하세요.");
            return;
        }

        int nNum = Integer.parseInt(strTmp);
        if(nNum == m_rndNum){
            strTmp = "정답입니다.";
        } else if (nNum > m_rndNum) {
            strTmp = nNum + " 보다는 작습니다.";
        } else {
            strTmp = nNum + " 보다는 큽니다.";
        }

        m_cnt++;
        m_txtCnt.setText("입력횟수: " + m_cnt);
        m_txtResult.setText(strTmp);

        if(nNum != m_rndNum){
            m_edNum.setText("");
            m_edNum.requestFocus();
        }
    }
}

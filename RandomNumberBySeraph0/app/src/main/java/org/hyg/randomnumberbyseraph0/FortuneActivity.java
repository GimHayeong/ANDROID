package org.hyg.randomnumberbyseraph0;

import android.app.Activity;
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

/**
 * Created by shiny on 2018-02-14.
 */

public class FortuneActivity extends AppCompatActivity {

    TextView m_txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("제비뽑기");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.btnOne).setOnClickListener(onButtonClick);
        findViewById(R.id.btnTwo).setOnClickListener(onButtonClick);
        findViewById(R.id.btnThree).setOnClickListener(onButtonClick);
        findViewById(R.id.btnFour).setOnClickListener(onButtonClick);

        m_txtResult = findViewById(R.id.txtResult);
    }


    Button.OnClickListener onButtonClick = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.fab){
                Snackbar.make(view, "good bye!!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                finish();
            }else{
                String strTag = findViewById(view.getId()).getTag().toString();
                checkValue(strTag);
            }
        }
    };

    private void checkValue(String strTag){
        int nTag = Integer.parseInt(strTag);

        int nRnd = new Random().nextInt(4)+1;
        String strMsg = strTag + "번 버튼";

        if(nTag == nRnd){
            strMsg += "축하합니다! 당첨되셨습니다.";
        } else {
            strMsg += "안타깝습니다. 다음 기회에 도전하세요.";
        }

        m_txtResult.setText(strMsg);
    }
}

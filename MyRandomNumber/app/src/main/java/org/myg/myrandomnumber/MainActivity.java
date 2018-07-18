package org.myg.myrandomnumber;

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
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random rnd =new Random();
    int count = 0;
    int num;

    EditText edNum;
    TextView txtCount;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("숫자 맞추기");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.fab).setOnClickListener(onButtonClick);
        findViewById(R.id.button).setOnClickListener(onButtonClick);

        //위젯 읽기
        edNum = (EditText) findViewById(R.id.editText);
        txtCount = (TextView) findViewById(R.id.textCount);
        txtResult = (TextView) findViewById(R.id.textResult);

        //난수 만들기
        num = rnd.nextInt(501) + 500;

        //위젯초기화 함수 호출
        clearFields();
    }

    private void clearFields() {
        txtCount.setText("입력횟수 : " + count);
        txtResult.setText("");
        edNum.setText("");
    }

    Button.OnClickListener onButtonClick = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.fab:
                    num = rnd.nextInt(501) + 500;
                    count = 0;
                    clearFields();
                    break;
                case R.id.button:
                    checkValue();
            }

            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }
    };

    private void checkValue() {
        //입력 받은 값 읽기
        String str = edNum.getText().toString();

        //빈 문자인지 판정
        if(str.equals("")){
            txtResult.setText("500~1000 사이의 숫자를 입력하세요");
            return;
        }

        //문자열을 정수로 변환
        int n = Integer.parseInt(str);

        //정답 확인
        if(n == num){
            str = "정답입니다";
        }else if(n > num){
            str = n + "보다는 적습니다";
        }else {
            str = n + "보다는 큽니다";
        }

        //입력 횟수 증가
        count++;

        //판정결과 표시
        txtCount.setText("입력횟수 : " + count);
        txtResult.setText(str);

        //정답이 아닌 경우 다음 입력을 위해 입력한 숫자를 지우고 포커스 이동
        if(n != num){
            edNum.setText("");
            edNum.requestFocus();
            //Toast.makeText(getApplicationContext(), str,Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

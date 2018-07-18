package org.hyg.rockpaperbyseraph0;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int[] m_imgSrcs = {R.drawable.img_1, R.drawable.img_2, R.drawable.img_3};
    int m_you;
    int m_com;
    int m_youWin = 0;
    int m_comWin = 0;

    ImageView w_imgYou;
    ImageView w_imgCom;

    TextView w_txtYou;
    TextView w_txtCom;
    TextView w_txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.fab).setOnClickListener(onButtonClick);
        for(int i=0; i<m_imgSrcs.length; i++){
            findViewById(R.id.btnRock0 + i).setOnClickListener(onButtonClick);
        }

        w_imgYou = (ImageView)findViewById(R.id.imgYou);
        w_imgCom = (ImageView)findViewById(R.id.imgComputer);

        w_txtYou = (TextView)findViewById(R.id.txtYou);
        w_txtCom = (TextView)findViewById(R.id.txtComputer);
        w_txtResult = (TextView)findViewById(R.id.txtResult);

        init();
    }

    //게임초기화
    private void init() {
        m_youWin = 0;
        m_comWin = 0;

        w_txtYou.setText("당신: 0");
        w_txtCom.setText("단말기: 0");
        w_txtResult.setText("");

        w_imgYou.setImageResource(R.drawable.question);
        w_imgCom.setImageResource(R.drawable.question);
    }

    Button.OnClickListener onButtonClick = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.fab){
                return;
            } else {
                String tag = view.getTag().toString();
                m_you = Integer.parseInt(tag);
                SetGameResult();
            }
        }
    };

    private void SetGameResult() {
        m_com = new Random().nextInt(m_imgSrcs.length);
        int ko = m_you - m_com;

        String strKo = "";
        if(ko == 0){
            strKo = "비겼습니다.";
        } else if (ko == 1 || ko == -2){
            strKo = "당신이 이겼습니다.";
            m_youWin++;
        } else {
            strKo = "당신이 졌습니다.";
            m_comWin++;
        }

        SetImages();

        w_txtYou.setText("당신: " + m_youWin);
        w_txtCom.setText("단말기: " + m_comWin);
        w_txtResult.setText(strKo);
    }

    private void SetImages() {
        w_imgYou.setImageResource(m_imgSrcs[m_you]);
        w_imgCom.setImageResource(m_imgSrcs[m_com]);

        Bitmap bmpOrg;
        Bitmap bmpRev;

        bmpOrg = BitmapFactory.decodeResource(getResources(), m_imgSrcs[m_com]);
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        bmpRev = Bitmap.createBitmap(bmpOrg, 0, 0, bmpOrg.getWidth(), bmpOrg.getHeight(), matrix, false);
        w_imgCom.setImageBitmap(bmpRev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 방법1. menu_xml 작성하기
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        // return true;

        // 방법2. 코드 직접 추가
        // 그룹번호
        int grpNum = 0;
        // 메뉴아이디
        int mnuId = 0;
        //표시순서
        int seq = 0;

        menu.add(grpNum, mnuId, seq, "Restart");
        menu.add(grpNum, ++mnuId, ++seq, "Quit");
        menu.add(grpNum, ++mnuId, ++seq, "About");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch(item.getItemId()){
            case 0:
                init();
                break;

            case 1:
                finishAffinity();
                break;

            case 2:
                View view = findViewById(R.id.btnRock0);
                Snackbar.make(view, "가위바위보 Ver 1.0", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        //OK 텍스트를 클릭하면 종료
                        finish();
                    }
                }).show();
                break;
        }

        return true;
    }
}

package org.hyg.yutnoribyseraph0;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

    Random m_rnd = new Random();
    String[] m_yutNames = {"모", "도", "개", "걸", "윷"};
    int[] m_yuts = new int[4];
    int[] m_yutImgs = {R.drawable.yut_0, R.drawable.yut_1};
    ImageView[] w_imgViews = new ImageView[4];
    TextView w_txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("윳 놀 이");

        findViewById(R.id.fab).setOnClickListener(onButtonClick);
        findViewById(R.id.btnOK).setOnClickListener(onButtonClick);

        for(int i=0; i<w_imgViews.length; i++) {
            w_imgViews[i] = findViewById(R.id.imgYut0 + i);
        }

        w_txtResult = findViewById(R.id.txtResult);
    }

    Button.OnClickListener onButtonClick;

    {
        onButtonClick = new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.fab:
                        break;

                    case R.id.btnOK:
                        setGameResult();
                        break;
                }
            }
        };
    }

    private void setGameResult() {
        int sum = 0;

        for(int i=0; i<w_imgViews.length; i++){

            //0과 1의발생비율 1:1
            //int n = m_rnd.nextInt(m_yuts.length);

            //0과 1의발생비율 3:2
            //int n = m_rnd.nextInt(m_yutNames.length) % 2;

            //0과 1의발생비율 3:2
            //int n = (m_rnd.nextInt(m_yutNames.length) + 1) % 2;

            int n = m_rnd.nextInt(m_yuts.length);
            if(n == 0){
                //3:2
                n = m_rnd.nextInt(m_yutNames.length) % 2;
            } else {
                //2:3
                n = (m_rnd.nextInt(m_yutNames.length) + 1) % 2;
            }
            
            sum += n;
            w_imgViews[i].setImageResource(m_yutImgs[n]);
        }

        w_txtResult.setText(m_yutNames[sum]);
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

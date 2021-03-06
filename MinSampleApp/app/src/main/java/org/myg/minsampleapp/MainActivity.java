package org.myg.minsampleapp;

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

public class MainActivity extends AppCompatActivity {
    EditText edName;
    EditText edPword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("환영인사");

        edName = (EditText) findViewById(R.id.editText3);
        edPword = (EditText) findViewById(R.id.editText4);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(onButtonClick);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(onButtonClick);

        findViewById(R.id.button2).setOnClickListener(onButtonClick);

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "안드로이드에 오신 여러분을 환영합니다.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    Button.OnClickListener onButtonClick = new Button.OnClickListener(){
        String msg = "";

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button:
                    msg = "여러분을 환영합니다";
                    break;
                case R.id.button2:
                    msg = "이름 : " + edName.getText() + ", 비밀번호 : " + edPword.getText();
                    break;
                case R.id.fab:
                    msg = "Float Action Button 누르셨습니다.";
                    break;
            }
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    };

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

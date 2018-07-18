package org.hyg.sampleappbyseraph0;

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
    EditText edPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Welcome Android");

        findViewById(R.id.fab).setOnClickListener(onButtonClick);
        findViewById(R.id.btnWelcome).setOnClickListener(onButtonClick);
        findViewById(R.id.btnConfirm).setOnClickListener(onButtonClick);

        edName = findViewById(R.id.txtName);
        edPwd = findViewById(R.id.txtPwd);
    }

    Button.OnClickListener onButtonClick = new Button.OnClickListener(){

        String strMsg = "";

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.fab:
                    strMsg = "Flat Action Button 을 누르셨습니다.";
                    break;
                case R.id.btnWelcome:
                    strMsg = "여러분을 환영합니다.";
                    break;
                case R.id.btnConfirm:
                    strMsg = "이름: " + edName.getText() + "\n비밀번호: " + edPwd.getText();
                    break;
            }

            Snackbar.make(view, strMsg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

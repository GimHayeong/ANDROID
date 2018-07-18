package org.hyg.intentbyseraph0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        View.OnClickListener onButtonClick = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (view.getId()){
                    case R.id.btnBackLogin:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivityForResult(intent, 101);
                        break;

                    case R.id.btnBackMenu:
                        intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        intent.putExtra("sender", "Custom");
                        startActivityForResult(intent, 101);
                        break;

                }
            }
        };

        ((Button)findViewById(R.id.btnBackLogin)).setOnClickListener(onButtonClick);
        ((Button)findViewById(R.id.btnBackMenu)).setOnClickListener(onButtonClick);
    }
}

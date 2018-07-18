package org.hyg.intentbyseraph0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        View.OnClickListener onButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch(view.getId()){
                    case R.id.btnCustom:
                        intent = new Intent(getApplicationContext(), CustomActivity.class);
                        startActivityForResult(intent, 101);
                        break;

                    case R.id.btnSale:
                        intent = new Intent(getApplicationContext(), SaleActivity.class);
                        startActivityForResult(intent, 101);
                        break;

                    case R.id.btnProduct:
                        intent = new Intent(getApplicationContext(), ProductActivity.class);
                        startActivityForResult(intent, 101);
                        break;
                }
            }
        };

        ((Button)findViewById(R.id.btnCustom)).setOnClickListener(onButtonClick);
        ((Button)findViewById(R.id.btnSale)).setOnClickListener(onButtonClick);
        ((Button)findViewById(R.id.btnProduct)).setOnClickListener(onButtonClick);

        Intent intent = getIntent();
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        if(intent != null) {
            String sender = intent.getStringExtra("sender");
            if(sender != null && sender.trim() == "Login"){
                Log.d("BACK", "To. " + sender);
                finish();
            } else {
                //Toast.makeText(this, "From. " + sender, Toast.LENGTH_LONG);
                Log.d("BACK", "From. " + sender);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);

        super.onNewIntent(intent);
    }
}

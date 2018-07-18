package org.hyg.intentbyseraph0;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    public static final String KEY_MENU_DATA = "data";
    private TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        View.OnClickListener onButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("name", "mike");
                setResult(RESULT_OK, intent);

                finish();
            }
        };

        ((Button)findViewById(R.id.btnBack)).setOnClickListener(onButtonClick);

        tvData = (TextView)findViewById(R.id.tvData);

        Intent intent = getIntent();
        displayIntentData(intent);
    }

    private void displayIntentData(Intent intent) {
        if(intent != null){
            try {
                Bundle bundle = intent.getExtras();
                MenuData data = (MenuData) bundle.getParcelable("data");

                //** MenuData data = (MenuData)intent.getParcelableExtra("data");
                //** MenuData data = getIntent().getParcelableExtra("data");

                tvData.setText("전달 받은 데이터\nNumber : " + data.getNumber() + "\nMessage : " + data.getMessage());
            } catch (Exception ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


}

package org.hyg.samplebyseraph0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnNewTitleButtonClick(View view) {
        Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
        //intent.putExtra("name", "insert");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Result", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void OnCloseTitleButtonClick(View view) {
        finish();
    }
}

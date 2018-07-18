package org.hyg.intentbyseraph0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SpinnerMainActivity extends AppCompatActivity {

    private TextView mTxtSelectedWord;
    private String[] mWords = { "mike", "angel", "crow", "john", "ginnie", "sally", "cohen", "rice" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_main);

        init();
    }

    private void init() {
        mTxtSelectedWord = (TextView)findViewById(R.id.txtSelectedWord);

        Spinner spnWord = (Spinner)findViewById(R.id.spnWord);

        ArrayAdapter<String> adtWord = new ArrayAdapter<String>(this
                                                              , android.R.layout.simple_spinner_item
                                                              , mWords);

        adtWord.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnWord.setAdapter(adtWord);

        spnWord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTxtSelectedWord.setText(mWords[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mTxtSelectedWord.setText("");
            }
        });
    }
}

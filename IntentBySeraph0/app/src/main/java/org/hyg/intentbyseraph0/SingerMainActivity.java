package org.hyg.intentbyseraph0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SingerMainActivity extends AppCompatActivity {

    private EditText mEdtSearch;
    private ListView mSingerList;
    private SingerAdapter mSingerAdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer_main);

        init();
    }

    private void init() {
        mSingerList = (ListView)findViewById(R.id.listView);
        mSingerAdt = new SingerAdapter(getApplicationContext());
        mSingerAdt.addItem(new SingerItem("소녀시대", "010-1000-1000", 20, R.drawable.singer_1));
        mSingerAdt.addItem(new SingerItem("걸스데이", "010-2000-2000", 22, R.drawable.singer_2));
        mSingerAdt.addItem(new SingerItem("여자친구", "010-3000-3000", 21, R.drawable.singer_3));
        mSingerAdt.addItem(new SingerItem("티아라", "010-4000-4000", 24, R.drawable.singer_4));
        mSingerAdt.addItem(new SingerItem("방탄소년단", "010-5000-5000", 22, R.drawable.singer_5));
        mSingerAdt.addItem(new SingerItem("원앤원", "010-6000-6000", 21, R.drawable.singer_6));
        mSingerAdt.addItem(new SingerItem("엠블랙", "010-7000-7000", 24, R.drawable.singer_7));
        mSingerAdt.addItem(new SingerItem("씨앤블루", "010-8000-8000", 25, R.drawable.singer_8));

        mSingerList.setAdapter(mSingerAdt);

        mSingerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SingerItem itm = (SingerItem)mSingerAdt.getItem(i);
                Toast.makeText(getApplicationContext(), "선택: " + itm.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }


}

package org.hyg.intentbyseraph0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class GridviewMainActivity extends AppCompatActivity {

    private EditText mEdtAddSinger;
    private GridView mGrdSinger;
    private SingerAdapter mAdtSinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview_main);

        init();
    }

    private void init() {
        mGrdSinger = (GridView)findViewById(R.id.grdSinger);
        mAdtSinger = new SingerAdapter(getApplicationContext());
        mAdtSinger.addItem(new SingerItem("소녀시대", "010-1000-1000", 20, R.drawable.singer_1));
        mAdtSinger.addItem(new SingerItem("걸스데이", "010-2000-2000", 22, R.drawable.singer_2));
        mAdtSinger.addItem(new SingerItem("여자친구", "010-3000-3000", 21, R.drawable.singer_3));
        mAdtSinger.addItem(new SingerItem("티아라", "010-4000-4000", 24, R.drawable.singer_4));
        mAdtSinger.addItem(new SingerItem("방탄소년단", "010-5000-5000", 22, R.drawable.singer_5));
        mAdtSinger.addItem(new SingerItem("원앤원", "010-6000-6000", 21, R.drawable.singer_6));
        mAdtSinger.addItem(new SingerItem("엠블랙", "010-7000-7000", 24, R.drawable.singer_7));
        mAdtSinger.addItem(new SingerItem("씨앤블루", "010-8000-8000", 25, R.drawable.singer_8));

        mGrdSinger.setAdapter(mAdtSinger);

        mEdtAddSinger = (EditText)findViewById(R.id.txtAddName);

        mGrdSinger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SingerItem itm = (SingerItem)mAdtSinger.getItem(i);
                Toast.makeText(getApplicationContext(), "선택: " + itm.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

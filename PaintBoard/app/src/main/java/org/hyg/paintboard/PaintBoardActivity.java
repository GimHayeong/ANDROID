package org.hyg.paintboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PaintBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        PaintBoard board = new PaintBoard(this);
        setContentView(board);
    }
}

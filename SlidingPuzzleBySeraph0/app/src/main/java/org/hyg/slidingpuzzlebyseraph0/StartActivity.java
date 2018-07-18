package org.hyg.slidingpuzzlebyseraph0;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class StartActivity extends AppCompatActivity {

    MediaPlayer m_player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                            , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        m_player = MediaPlayer.create(this, R.raw.greensleeves);
        m_player.setLooping(true);
        m_player.start();


        findViewById(R.id.btnStart).setOnClickListener(onButtonClick);
        findViewById(R.id.btnQuit).setOnClickListener(onButtonClick);

        findViewById(R.id.rbMusicOn).setOnClickListener(onButtonClick);
        findViewById(R.id.rbMusicOff).setOnClickListener(onButtonClick);
    }

    private View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnStart:
                    setSettings();
                    m_player.pause();
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;

                case R.id.btnQuit:
                    android.os.Process.killProcess(android.os.Process.myPid());
                    break;

                case R.id.rbMusicOn:
                    m_player.start();
                    break;

                case R.id.rbMusicOff:
                    m_player.pause();
                    break;
            }
        }
    };

    /**
     * 환경설정 저장
     */
    private void setSettings() {
        RadioGroup group = (RadioGroup)findViewById(R.id.rgBoardSize);
        int checkedId = group.getCheckedRadioButtonId();

        View radio = group.findViewById(checkedId);
        Settings.Size = group.indexOfChild(radio) + 3;

        checkedId = ((RadioGroup)findViewById(R.id.rgMusic)).getCheckedRadioButtonId();
        Settings.IsMusic = (checkedId == R.id.rbMusicOn);

        checkedId = ((RadioGroup)findViewById(R.id.rgSound)).getCheckedRadioButtonId();
        Settings.IsSound = (checkedId == R.id.rbSoundOn);

        checkedId = ((RadioGroup)findViewById(R.id.rgVib)).getCheckedRadioButtonId();
        Settings.IsVib = (checkedId == R.id.rbVibOn);
    }

    /**
     * 환경설정 읽기
     */
    private void getSettings() {
        RadioGroup group = (RadioGroup)findViewById(R.id.rgBoardSize);
        ((RadioButton)group.getChildAt(Settings.Size - 3)).setChecked(true);

        ((RadioButton)group.getChildAt(R.id.rbMusicOn)).setChecked(Settings.IsMusic);
        ((RadioButton)group.getChildAt(R.id.rbSoundOn)).setChecked(Settings.IsSound);
        ((RadioButton)group.getChildAt(R.id.rbVibOn)).setChecked(Settings.IsVib);
    }

    /**
     * [BACK] 키를 누르면 게임 종료
     */
    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}

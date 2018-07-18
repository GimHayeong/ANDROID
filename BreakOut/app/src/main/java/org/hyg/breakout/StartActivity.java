package org.hyg.breakout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class StartActivity extends AppCompatActivity {

    /**
     * 미디어 재생
     */
    MediaPlayer m_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Statusbar 감추기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Actionbar 감추기
        getSupportActionBar().hide();

        // 배경음악
        m_player = MediaPlayer.create(this, R.raw.greensleeves);
        m_player.setLooping(true);
        m_player.start();

        findViewById(R.id.btnStart).setOnClickListener(onButtonClick);
        findViewById(R.id.btnQuit).setOnClickListener(onButtonClick);

        findViewById(R.id.rbBgSoundOn).setOnClickListener(onButtonClick);
        findViewById(R.id.rbBgSoundOff).setOnClickListener(onButtonClick);
    }

    /**
     * 시작 버튼을 터치하면 게임을 시작
     * 종료 버튼을 터치하면 게임을 종료
     * 배경음악 ON 터치하면 배경음악 재생
     * 배경음악 OFF 터치하면 배경음악 재생중지
     */
    View.OnClickListener onButtonClick = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.btnQuit:
                    android.os.Process.killProcess(android.os.Process.myPid());
                    break;

                case R.id.btnStart:
                    setSettings();
                    m_player.pause();
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;

                case R.id.rbBgSoundOn:
                    m_player.start();
                    break;

                case R.id.rbBgSoundOff:
                    m_player.pause();
                    break;
            }
        }
    };

    /**
     * 환경설정 저장
     */
    private void setSettings(){
        int checkedId = ((RadioGroup)findViewById(R.id.rgSetBgSound)).getCheckedRadioButtonId();
        Settings.setIsMusic(checkedId == R.id.rbBgSoundOn);

        checkedId = ((RadioGroup)findViewById(R.id.rgSetSound)).getCheckedRadioButtonId();
        Settings.setIsSound(checkedId == R.id.rbSoundOn);

        checkedId = ((RadioGroup)findViewById(R.id.rgSetVib)).getCheckedRadioButtonId();
        Settings.setIsVib(checkedId == R.id.rbVibOn);
    }

    /**
     * 저장된 환경설정을 읽어와 각 라디오버튼에 반영
     */
    private void getSettings(){
        ((RadioButton)findViewById(R.id.rbBgSoundOn)).setChecked(Settings.getIsMusic());
        ((RadioButton)findViewById(R.id.rbSoundOn)).setChecked(Settings.getIsSound());
        ((RadioButton)findViewById(R.id.rbVibOn)).setChecked(Settings.getIsVib());
    }

    /**
     * StartActivity 가 다시 View 에 나타날때 호출되는 콜백함수
     *  : 저장된 환경설정을 읽어오고 배경음악 설정에 따른 재생여부 결정
     */
    @Override
    protected void onResume() {
        super.onResume();

        getSettings();
        if(Settings.getIsMusic()){
            m_player.seekTo(0);
            m_player.start();
        }
    }
}

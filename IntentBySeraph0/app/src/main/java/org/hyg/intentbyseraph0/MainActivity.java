package org.hyg.intentbyseraph0;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_MENU = 101;

    EditText edtPhoneNumber, edtName, edtId, edtPwd;
    ProgressDialog dlgProgress;
    Animation m_flow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkSmsPermission();

        setProgressBar();

        View.OnClickListener onButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch(view.getId()){
                    case R.id.btnPopup:
                        /*intent = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_MENU);*/

                        intent = new Intent();
                        ComponentName compNm = new ComponentName("org.hyg.intentbyseraph0", "org.hyg.intentbyseraph0.MenuActivity");
                        intent.setComponent(compNm);
                        startActivityForResult(intent, REQUEST_CODE_MENU);
                        break;

                    case R.id.btnCall:
                        String data = edtPhoneNumber.getText().toString();
                        intent = new Intent(Intent.ACTION_DIAL, Uri.parse(data));
                        startActivity(intent);
                        break;

                    case R.id.btnPDF:
                        String filename = edtPhoneNumber.getText().toString();
                        if(filename.length()>0){
                            //openPDF(filename.trim());
                        } else {
                            Toast.makeText(getApplicationContext(), "PDF파일명을 입력하세요.", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case R.id.btnObj:
                        intent = new Intent(getApplicationContext(), MenuActivity.class);
                        MenuData objData = new MenuData(100, "Hello, Android!");
                        intent.putExtra("data", objData);

                        startActivityForResult(intent, REQUEST_CODE_MENU);
                        break;

                    case R.id.btnSend:
                        String name = edtName.getText().toString();
                        intent = new Intent(getApplicationContext(), MyService.class);
                        intent.putExtra("command", "show");
                        intent.putExtra("name", name);
                        startService(intent);
                        break;

                    case R.id.btnLogin:
                        String id = edtId.getText().toString();
                        String pwd = edtPwd.getText().toString();
                        if(id.length() <= 0){
                            Toast.makeText(getApplicationContext(), "Please, Input your ID.", Toast.LENGTH_LONG).show();
                            return;
                        } else if(pwd.length() <= 0){
                            Toast.makeText(getApplicationContext(), "Please, Input your password.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        startActivity(intent);

                        break;

                    case R.id.btnShow:
                        dlgProgress = new ProgressDialog(MainActivity.this);
                        dlgProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dlgProgress.setMessage("데이터를 확인하는 중입니다.");
                        if(!isFinishing()) {
                            dlgProgress.show();
                        }

                        //CheckTypeTask task = new CheckTypeTask();
                        //task.execute();
                        break;

                    case R.id.btnHide:
                        if(dlgProgress != null){
                            dlgProgress.dismiss();
                        }
                        edtName.startAnimation(m_flow);
                        break;
                }

            }
        };
        ((Button)findViewById(R.id.btnPopup)).setOnClickListener(onButtonClick);
        ((Button)findViewById(R.id.btnCall)).setOnClickListener(onButtonClick);
        ((Button)findViewById(R.id.btnPDF)).setOnClickListener(onButtonClick);
        ((Button)findViewById(R.id.btnObj)).setOnClickListener(onButtonClick);
        ((Button)findViewById(R.id.btnSend)).setOnClickListener(onButtonClick);
        ((Button)findViewById(R.id.btnLogin)).setOnClickListener(onButtonClick);
        ((Button)findViewById(R.id.btnShow)).setOnClickListener(onButtonClick);
        ((Button)findViewById(R.id.btnHide)).setOnClickListener(onButtonClick);

        edtPhoneNumber = (EditText)findViewById(R.id.edtPhoneNumber);
        edtName = (EditText)findViewById(R.id.edtName);
        edtId = (EditText)findViewById(R.id.edtID);
        edtPwd = (EditText)findViewById(R.id.edtPWD);

        m_flow = AnimationUtils.loadAnimation(this, R.anim.flow);
        m_flow.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(getApplicationContext(), "애니메이션 종료", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        SeekBar seekBar = ((SeekBar)findViewById(R.id.skbApple));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int brightness;
                if(i<10){
                    brightness = 10;
                } else if(i>100) {
                    brightness = 100;
                } else {
                    brightness = i;
                }

                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.screenBrightness = (float)brightness / 100;
                getWindow().setAttributes(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Intent svcIntent = getIntent();
        receiveIntent(svcIntent, "onCreate");
    }

    /**
     * 프로그레스바 초기화
     */
    private void setProgressBar() {
        ProgressBar pgbApple = (ProgressBar)findViewById(R.id.pgbApple);
        pgbApple.setIndeterminate(false);
        pgbApple.setMax(100);
        pgbApple.setProgress(80);
    }

    /**
     * SMS 권한 체크
     */
    private void checkSmsPermission() {
        int pmsSms = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);

        if(pmsSms == PackageManager.PERMISSION_GRANTED){
            Log.d("MainActivity", "SMS 수신 권한 있음");
        } else {
            Log.d("MainActivity", "SMS 수신 권한 없음");

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)){
                Log.d("MainActivity", "SMS 권한 설명 필요");
            } else {
                // 권한 요청 대화상자 띄우기
                // 대화상자 결과 ==> onRequestPermissionResult 콜백 메서드에서 확인
                ActivityCompat.requestPermissions(this
                                                 , new String[] {Manifest.permission.RECEIVE_SMS}
                                                 , 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("MainActivity", "SMS 권한 사용자 승인");
                } else {
                    Log.d("MainActivity", "SMS 권한 사용자 거부");
                }
                return;
        }
    }

    /**
     * PDF 뷰어를 통해 PDF 파일 열기
     *  : 기기에서 실행 가능(기기에만 PDF뷰어가 설치되어 있고 SD로부터 파일을 읽고 쓸 수 있다)
     * @param filename
     */
    private void openPDF(String filename) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filePath = dirPath + File.separator + filename;
        File file = new File(filePath);

        if(file.exists()){
            Uri uri = Uri.fromFile(file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException ex){
                Toast.makeText(this, "PDF 파일을 보기 위한 뷰어 앱이 없습니다.", Toast.LENGTH_LONG).show();
            } catch (Exception ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_MENU){

            StringBuffer sb = new StringBuffer();
            sb.append("request code : ");
            sb.append(requestCode);
            sb.append(", result code : ");
            sb.append(resultCode);

            if(resultCode == RESULT_OK){

                String name = data.getExtras().getString("name");

                sb.append("\nname : ");
                sb.append(name);

                Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveState();

    }

    @Override
    protected void onResume() {
        super.onResume();

        restoreState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        clearState();

    }

    /**
     * MainActivity 가 메모리에 만들어져 있는 상태에서 서비스로부터 MainActivity 를 띄우는 경우,
     *  onCreate()의 getIntent() 메서드 호출이 아닌 onNewIntent()로 전달됨.
     * @param intent : 서비스로부터 전달받은 인텐트
     */
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("MAIN", "onNewIntent");

        receiveIntent(intent, "onNewIntent");

        super.onNewIntent(intent);
    }

    private void receiveIntent(Intent intent, String call) {
        if(intent != null){
            String cmd = intent.getStringExtra("command");
            String name = intent.getStringExtra("name");

            Log.d("MAIN", "[" + call + "] command: " + cmd + ", name: " + name);
            edtName.setText(name);
        }
    }

    /**
     * SharedPreferences 로 앱내부 파일에 내용 저장
     */
    private void saveState() {
        SharedPreferences spf = getSharedPreferences("spf", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("name", edtPhoneNumber.getText().toString());
        edit.commit();
    }


    /**
     * SharedPreferences 로 앱내부 파일의 내용 읽기
     */
    private void restoreState() {
        SharedPreferences spf = getSharedPreferences("spf", Activity.MODE_PRIVATE);
        if(spf != null && spf.contains("name")) {
            String name = spf.getString("name", "");
            edtPhoneNumber.setText(name);
        }
    }

    /**
     * SharedPreferences 로 앱내부 파일의 내용 삭제
     */
    private void clearState() {
        SharedPreferences spf = getSharedPreferences("spf", Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.clear();
        edit.commit();
    }

    /**
     * 스낵바를 이용해 메시지 띄우기
     * @param view
     * @param msg
     */
    private void showSnackbar(View view, String msg){
        //Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    /**
     * 대화상자로 메시지 표시
     * @param strTitle
     * @param strMsg
     */
    private void showDialogBox(String strTitle, String strMsg){
        // Parcel 객체를 읽어 대화상자 만들기 위한 빌더 객체 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(strTitle);
        builder.setMessage(strMsg);
        //builder.setIcon(R.drawable.ic_dialog_alert);

        // [Yes] [Cancel] [No] 버튼 추가
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: [Yes] button
            }
        });

        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: [Cancel] button
            }
        });

        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // TODO: [No] button
            }
        });

        // 다이얼로그 객체 생성
        AlertDialog dialog = builder.create();
        dialog.show();
    }







    private class CheckTypeTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog;
        Context m_context;
        int m_style;
        String m_message;

        public CheckTypeTask() {
            this(MainActivity.this, ProgressDialog.STYLE_SPINNER, "로딩중입니다...");
        }

        public CheckTypeTask(Context context, int style, String strMsg){
            m_context = context;
            m_style = style;
            m_message = strMsg;

            asyncDialog = new ProgressDialog(context);
        }

        /**
         * ProgressDialog 객체를 생성하고 시작
         */
        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(m_style);
            asyncDialog.setMessage(m_message);

            asyncDialog.show();

            super.onPreExecute();
        }

        /**
         * 진행정도
         * @param voids
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                for(int i=0; i<5; i++){
                    sleep(500);
                }
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }

            return null;
        }

        /**
         * ProgressDialog 종료
         * @param aVoid
         */
        @Override
        protected void onPostExecute(Void aVoid) {

            asyncDialog.dismiss();

            super.onPostExecute(aVoid);
        }
    }

}

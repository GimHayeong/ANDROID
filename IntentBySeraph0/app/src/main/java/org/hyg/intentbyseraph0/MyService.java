package org.hyg.intentbyseraph0;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "MyService";
    public MyService() {
    }

    /**
     * 다른 구성요소와의 유기적 연결위해 바인딩할 경우 관련 처리할 때...
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * [Service life-cycle]
     */
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate() 호출됨");
    }

    /**
     * [Service life-cycle]
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestory() 호출됨");
    }

    /**
     * 전달받은 인텐트 객체 처리
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     *   전달받은 인텐트 객체가 null 일때, 서비스 비정상 종료시 시스템이 서비스 자동 재시작하도록 하려면 Service.START_STICKY 반환
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand() 호출됨");

        if(intent == null){
            return Service.START_STICKY;
        } else {
            writeLog(intent);

        }

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 1/100초에 한번씩 5회 로그 출력
     * @param intent
     */
    private void writeLog(Intent intent) {
        String cmd = intent.getStringExtra("command");
        String name = intent.getStringExtra("name");

        Log.d(TAG, "command: " + cmd + ", name: " + name);

        for(int i=0; i<5; i++){
            try{
                Thread.sleep(10);
            } catch (Exception ex) {}
            Log.d(TAG, "Waiting: " + i + " seconds.");
        }

        sendToActivity(intent, cmd, name);
    }


    /**
     * 서비스측에서 액티비티쪽으로 인텐트 전달
     *  : FLAG_ACTIVITY_NEW_TASK - 화면이 없는 서비스에서 화면이 있는 액티비티를 띄우려면 새로운 태스크를 만들어야 함
     *  : FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_CLEAR_TOP - MainActivity가 이미 만들어져 있으면 재사용.
     * @param intent
     */
    private void sendToActivity(Intent intent, String cmd, String name) {
        Intent showIntent = new Intent(getApplicationContext(), MainActivity.class);
        showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                          | Intent.FLAG_ACTIVITY_SINGLE_TOP
                          | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        showIntent.putExtra("command", cmd);
        showIntent.putExtra("name", name + " Success!!!");
        startActivity(showIntent);
        //stopService(intent);
    }

}

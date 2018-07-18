package org.hyg.push;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class Seraph0FirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FMS";

    public Seraph0FirebaseMessagingService() {
    }

    /**
     * Google 클라우드 서버에서 보내오는 메시지를 받아 처리
     *   - 푸시 메시지를 받았을 때 그 내용을 확인한 후 액티비트 쪽으로 보내는 메서드 호출
     * @param remoteMessage
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived() 호출됨");

        /**
         * 메시지를 전송한 발신자 코드
         */
        String from;
        /**
         * 푸시 메시지 데이터 Map 객체
         */
        Map<String, String> data;
        /**
         * 푸시 메시지 데이터 Map 객체에 포함된 발신데이터
         */
        String contents;

        from = remoteMessage.getFrom();
        data = remoteMessage.getData();
        contents = data.get("contents");

        Log.v(TAG, "from: " + from + ", contents : " + contents);

        sendToActivity(getApplicationContext(), from, contents);
    }

    /**
     * 인텐트 객체를 만들고 단말기 액티비티로 보내는 메서드
     *  - 인텐트 객체를 만들고 startActivity() 메서드 => context 의 onNewIntent() 로 인덴트 전달
     * @param context
     * @param from
     * @param contents
     */
    private void sendToActivity(Context context, String from, String contents) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("contents", contents);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(intent);
    }
}

package org.hyg.intentbyseraph0;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        writeLog("onReceive() 메서드 호출됨.");

        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = parseSmsMessage(bundle);

        if(msgs != null && msgs.length > 0){
            String sender = msgs[0].getOriginatingAddress();
            String contents = msgs[0].getMessageBody().toString();
            Date receivedDate = new Date(msgs[0].getTimestampMillis());

            sendToActivity(context, sender, contents, receivedDate);
        }
    }

    private void sendToActivity(Context context, String sender, String contents, Date receivedDate) {
        Intent intent = new Intent(context, SmsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("sender", sender);
        intent.putExtra("contents", contents);
        intent.putExtra("receivedDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(receivedDate));

        context.startActivity(intent);
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle) {
        Object[] objs = (Object[])bundle.get("pdus");
        SmsMessage[] msgs = new SmsMessage[objs.length];

        for(int i=0; i<objs.length; i++){
            // API 23 이상인 경우와 아닌 경우 PUD 포맷 메시지 복원
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                String format = bundle.getString("format");
                msgs[i] = SmsMessage.createFromPdu((byte[])objs[i], format);
            } else {
                msgs[i] = SmsMessage.createFromPdu((byte[])objs[i]);
            }
        }

        return msgs;
    }



    private void writeLog(String strLog) {
        Log.i("SmsReceiver", strLog);
    }
}

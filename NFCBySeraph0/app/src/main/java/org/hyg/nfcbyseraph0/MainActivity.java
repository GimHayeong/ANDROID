package org.hyg.nfcbyseraph0;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.net.ssl.StandardConstants;

public class MainActivity extends AppCompatActivity {

    private NdefMessage mNdefMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private NdefMessage createTagMessage(String msg, int type) {
        NdefRecord[] ndefRecords = new NdefRecord[1];

        if(type == 1) {
            ndefRecords[0] = createTextRecord(msg, Locale.KOREAN, true);
        } else if (type == 2) {
            ndefRecords[1] = createUriRecord(msg.getBytes());
        }

        NdefMessage mNdefMessage = new NdefMessage(ndefRecords);

        return  mNdefMessage;
    }


    private NdefRecord createTextRecord(String msg, Locale locale, boolean isEncodeUtf8) {
        final byte[] btsLang = locale.getLanguage().getBytes(StandardCharsets.US_ASCII);
        final Charset encodingUtf = isEncodeUtf8 ? StandardCharsets.UTF_8 : StandardCharsets.UTF_16;
        final byte[] btsText = msg.getBytes(encodingUtf);
        final int bitUtf = isEncodeUtf8 ? 0 : (1 << 7);
        final char status = (char)(bitUtf + btsLang.length);

        //TODO:
        return null;
    }

    private NdefRecord createUriRecord(byte[] bytes) {
        //TODO:
        return null;
    }
}

package org.hyg.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class Seraph0FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FIID";

    public Seraph0FirebaseInstanceIDService() {
    }

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "onTakenRefresh() 호출됨");

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed Token: " + refreshedToken);
    }
}

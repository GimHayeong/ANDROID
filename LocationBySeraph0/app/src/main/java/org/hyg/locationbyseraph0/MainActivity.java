package org.hyg.locationbyseraph0;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTxtMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mTxtMsg = (TextView) findViewById(R.id.txtMsg);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startLastLocationService();
                    //startLocationServiceByListener();
                    Log.d("Location:", "startService");
                } catch (Exception ex) { Log.d("Location", ex.getMessage()); }
            }
        };
        ((Button)findViewById(R.id.btnLocation)).setOnClickListener(OnButtonClick);
    }

    /**
     * 최근 위치
     *   - 위치 관리자 객체 참조 : getSystemService(Context.LOCATION_SERVICE)
     *   - 최근 위치 정보 : getLastKnownLocation(LocationManager.GPS_PROVIDER)
     *                     getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
     *   - 위도 : getLatitude()
     *   - 경도 : getLongitude()
     *
     * (테스트시 최근위치값이 없으면 위치정보 조회 불가)
     */
    private void startLastLocationService() {
        LocationManager mgrLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Location", "check permission.");
            return;
        }

        Location location = mgrLocation.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null) {
            mTxtMsg.setText(location.getLatitude() + ", " + location.getLongitude());
        } else { Log.d("Location", "Location is null."); }
    }

    /**
     * 리스너를 통해 위치를 업데이트
     * (위치 정보가 변경될때 호출되는 이벤트를 통해 조회 및 처리)
     *
     * (테스트시 최근위치값이 없으면 위치값 업데이트 후 조회하므로, 위치정보 조회 가능)
     */
    private void startLocationServiceByListener() {
        LocationManager mgrLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Location", "check permission.");
            return;
        }
        mgrLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
    }


    /***********************************************************************************************
     * LocationListener 를 구현
     **********************************************************************************************/
    private class GPSListener implements LocationListener {
        /**
         * 위치정보가 변경될 때 호출
         * @param location
         */
        @Override
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            ((TextView)findViewById(R.id.txtMsg)).setText(latitude + ", " + longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}

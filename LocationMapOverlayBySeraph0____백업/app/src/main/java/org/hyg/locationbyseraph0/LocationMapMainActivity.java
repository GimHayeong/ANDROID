package org.hyg.locationbyseraph0;

import android.*;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class LocationMapMainActivity extends AppCompatActivity {

    private final String TAG = "LocationMap";

    private SupportMapFragment mSmfMap;
    private GoogleMap mGgMap;
    private Button mBtnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map_main);

        init();
    }

    private void init() {
        mBtnLocation = (Button)findViewById(R.id.btnLocation);
        mSmfMap = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.smfMap);
        mSmfMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "GoogleMap is ready.");

                mGgMap = googleMap;
            }
        });

        try{
            MapsInitializer.initialize(this);
        } catch (Exception ex) { Log.d(TAG, ex.getMessage()); }

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMyLocation();
            }
        };
        mBtnLocation.setOnClickListener(OnButtonClick);
    }

    private void startLocationServiceByListener() {
        LocationManager mgrLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GoogleMapListener gpsListener = new GoogleMapListener();
        long minTime = 10000;
        float minDistance = 0;


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Location", "check permission.");
            return;
        }
        mgrLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
    }

    private void requestMyLocation() {
    }

    private class GoogleMapListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            showCurrentLocation(latitude, longitude);
        }

        /**
         * 현재 위치를 이용해 LatLng 객체 생성하고
         * 현재 위치를 지도의 중심으로 표시
         * @param latitude : 위도
         * @param longitude : 경도
         */
        private void showCurrentLocation(Double latitude, Double longitude) {
            LatLng curPoint = new LatLng(latitude, longitude);
            mGgMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
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

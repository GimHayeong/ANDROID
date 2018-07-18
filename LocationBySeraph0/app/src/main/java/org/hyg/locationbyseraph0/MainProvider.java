package org.hyg.locationbyseraph0;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.RemoteViews;

import java.util.List;

/**
 * Created by shiny on 2018-03-31.
 */

public class MainProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int size = appWidgetIds.length;

        for (int i = 0; i < size; i++) {
            int appWidgetId = appWidgetIds[i];

            //String uriBegin = "geo: " + ycoord + ", " + xcoord;

            //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //PendingIntent ittPeding = PendingIntent.getActivity(context, 0, intent, 0);

            //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_main);
            //views.setOnClickPendingIntent(R.id.txtInfo, ittPeding);
            //appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        //context.startService(new Intent(context, GPSLocationService.class));
    }

    public static class GPSLocationService extends Service {
        public static final String TAG = "GPSLocationService";
        private String info;
        private double xcoord, ycoord;

        private LocationManager manager = null;
        private LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateCoordinates(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        private void updateCoordinates(double latitude, double longitude) {
            if(info.length() <= 0){
                info = "" + latitude + ", " + longitude + "\n";
            } else {
                info += "\n" + latitude + ", " + longitude + "\n";
            }

            RemoteViews views = new RemoteViews(getPackageName(), R.layout.activity_main);
            //views.setTextViewText(R.id.txtInfo, info);

            ComponentName thisWidget = new ComponentName(this, MainProvider.class);

            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, views);

            xcoord = latitude;
            ycoord = latitude;

        }

        @Override
        public void onCreate() {
            super.onCreate();

            manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

        @Override
        public void onStart(Intent intent, int startId) {
            super.onStart(intent, startId);

            startListening();
        }

        private void startListening() {
            final Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            final String bestProvider = manager.getBestProvider(criteria, true);
            if (bestProvider != null && bestProvider.length() > 0) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                manager.requestLocationUpdates(bestProvider, 500, 0, listener);
            } else {
                final List<String> providers = manager.getProviders(true);

                for(final String provider : providers){
                    manager.requestLocationUpdates(provider, 500, 10, listener);
                }
            }
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}

package com.example.leeseungchan.chulbalhama.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.leeseungchan.chulbalhama.Activities.MainActivity;
import com.example.leeseungchan.chulbalhama.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class LocationHelper {



    LocationListener gpsLocationListener;
    LocationManager lm;
    DistanceCalc calc;
    Context context;
    NotificationManager manager;
    public static final String CHANNEL_ID = "location_noti_channel";
    private int updateInterval = 5000;


    private boolean activityRecognitionStart = false;


    double lastLongitude;
    double lastLatitude;

    String userState = "HOME";
    String lastState = "";

    public LocationHelper(final Context context){
        this.context = context;
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        calc = new DistanceCalc();
        createNotificationChannel();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0, notificationIntent, 0);

        final Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("허허")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();

        gpsLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                synchronized (notification){
                    Log.e("noti", "noti!");
                    manager.notify(3, notification);
                }

                String provider = location.getProvider();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                longitude = calc.formattingPoint(longitude);
                latitude = calc.formattingPoint(latitude);
//                if(calc.distance(schoolLocation.getLatitude(),schoolLocation.getLongitude(),currentLocation.getLatitude(),currentLocation.getLongitude(),"meter") < 50 && userState=="OnTheRoad"){
//                    //TODO 이 때 시간이 시작시간보다 늦으면 지각이므로 알람을 안띄우게
//                    userState = "SCHOOL";
//                    Toast.makeText(context, "학교에 거의 도착했습니다!", Toast.LENGTH_SHORT).show();
//                } else if (calc.distance(homeLocation.getLatitude(),homeLocation.getLongitude(),currentLocation.getLatitude(),currentLocation.getLongitude(),"meter") > 50 && userState =="HOME"){
//                    userState ="OnTheRoad";
//                    Toast.makeText(context, "집에서 나왔습니다!", Toast.LENGTH_SHORT).show();
//                    //TODO 여기에 ~초이상 머무르면 뜨는 알람 설정 (사용자가 집에서 나왔다.)
//                } else if (calc.distance(homeLocation.getLatitude(),homeLocation.getLongitude(),currentLocation.getLatitude(),currentLocation.getLongitude(),"meter") < 50){
//                    //유저가 집에 있다.
//                    userState ="HOME";
//                    if(false){
//                        Toast.makeText(context, "책을 가지고 나가세요!", Toast.LENGTH_SHORT).show();
//                    }
//                }
                if(!activityRecognitionStart)
                {


                }

                lastLatitude = latitude;
                lastLongitude = longitude;

                lastState=userState;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }

    public void getLocation(){
        if(ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED){
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            String provider = location.getProvider();

            // 위치 정보 얻기 누른 시점 위도, 경도
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            longitude = calc.formattingPoint(longitude);
            latitude = calc.formattingPoint(latitude);
            lastLatitude = latitude;
            lastLongitude = longitude;

            Log.d("최초 위치 정보", "위도 : " + longitude + ", 경도 : " + latitude  );
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        updateInterval,
                        0,
                        gpsLocationListener);
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        updateInterval,
                        0,
                        gpsLocationListener);
        }
    }

    public void setUpdateInterval(int interval){
        this.updateInterval = interval;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}

package com.example.leeseungchan.chulbalhama.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.example.leeseungchan.chulbalhama.Activities.MainActivity;
import com.example.leeseungchan.chulbalhama.R;

import java.util.Calendar;

public class HamaService extends Service {
    public static final String CHANNEL_ID = "service_channel";
    NotificationManager manager;
    Notification notification;
    LocationUpdateThread locationThread;
    LocationHelper locationHelper;
    int count=0;
    boolean startupdate=false;
    Calendar car;

    public class HamaServiceBinder extends Binder {
        public HamaService getService(){
            return HamaService.this;
        }
    }

    private final IBinder mBinder = new HamaServiceBinder();

    public interface ICallback {
    }

    private ICallback mCallback;

    //액티비티에서 콜백 함수를 등록하기 위함.
    public void registerCallback(ICallback cb) {
        mCallback = cb;
    }

    //액티비티에서 서비스 함수를 호출하기 위한 함수 생성
    public void setLocationHelper(LocationHelper helper){
    }

    @Override
    public void onCreate() {
        Log.d("HamaService", "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("HamaService", "onStartCommand");
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        //GPS 수집 관련 로직
        Log.d("INService", "onBind");
        HamaHandler hamaHandler = new HamaHandler();
        locationThread = new LocationUpdateThread(hamaHandler);
        locationThread.start();
        locationHelper = new LocationHelper(getApplicationContext());
        locationHelper.getLocation();
        startForeground(1, notification);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID) //CHANNEL_ID 채널에 지정한 아이디
                    .setContentTitle("background machine")
                    .setContentText("알림입니다")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setOngoing(true).build();

            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    class HamaHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg){
            if (locationHelper != null) {
                locationHelper.setUpdateInterval(adjustTimeInterval());
            }
        }
    }

    public int adjustTimeInterval(){
        car = Calendar.getInstance();
        int datOfWeek = car.get(Calendar.DAY_OF_WEEK);
        int hour = car.get(Calendar.HOUR);
        int minute = car.get(Calendar.MINUTE);
        Toast.makeText(getApplicationContext(), "현재시간은 " + hour +"시" + minute + "분 입니다.", Toast.LENGTH_SHORT).show();
        //TODO DB 시간 조회 후 Time Interval 재설정
        return 3000;
    }
}
package com.example.leeseungchan.chulbalhama.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.example.leeseungchan.chulbalhama.Activities.MainActivity;
import com.example.leeseungchan.chulbalhama.DBHelper;
import com.example.leeseungchan.chulbalhama.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HamaService extends Service {
    public static final String CHANNEL_ID = "service_channel";
    NotificationManager manager;
    Notification notification;
    LocationUpdateThread locationThread;
    LocationHelper locationHelper;
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
                .setContentTitle("하마 서비스~")
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

    public int adjustTimeInterval() {
        int interval;
        long diff = 0;
        long diff2 = 0;
        long sec = 0;
        long sec2 = 0;
        boolean isHome = true;

        /* 오늘의 요일은? */
        car = Calendar.getInstance();
        int dayOfWeeks = car.get(Calendar.DAY_OF_WEEK);
        int dayId = 0;
        switch (dayOfWeeks) {
            case 1:
                dayId = 6; // 일
                break;
            case 2:
                dayId = 0; //월
                break;
            case 3:
                dayId = 1; //화
                break;
            case 4:
                dayId = 2; //수
                break;
            case 5:
                dayId = 3; //목
                break;
            case 6:
                dayId = 4; //금
                break;
            case 7:
                dayId = 5; //토
                break;
        }

        /* 현재 시간은? */
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        car = Calendar.getInstance();
        String currentTime = format.format(car.getTime());
        Log.d("CurrentTime?", currentTime);

        /* 오늘 출발 시간 조회 */
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String daySql = "select departure_time, destination_id from day_of_week where _id = ?";
        Cursor c = db.rawQuery(daySql, new String[]{Integer.toString(dayId + 1)}, null);
        c.moveToNext();
        try {
            String todayDepartureTime = c.getString(0);
            Log.d("DepartureTime", todayDepartureTime);

            /* 오늘 도착 예정 시간 조회*/
            int destination_id = c.getInt(1);
            String arrivalSql = "select time from destinations where _id = ?";
            Cursor c2 = db.rawQuery(arrivalSql, new String[]{Integer.toString(destination_id)}, null);
            c2.moveToNext();
            String arrivalTime = c2.getString(0);
            Log.d("Destination Time", arrivalTime);

            /* 현재 시간과 오늘 출발 시간 비교 */
            Date curretnDateTime;
            Date departureDateTime;
            Date arrivalDateTime;

            try {
                curretnDateTime = format.parse(currentTime);
                departureDateTime = format.parse(todayDepartureTime);
                arrivalDateTime = format.parse(arrivalTime);
                diff = Math.abs(curretnDateTime.getTime() - departureDateTime.getTime());
                diff2 = Math.abs(curretnDateTime.getTime() - arrivalDateTime.getTime());
            } catch (Exception e) {
            }

            sec = diff / 1000;
            sec2 = diff2 / 1000;
            Log.d("Sec Diff", Long.toString(sec));
            /* 집에 있으면 */
            if (isHome) {
                /* 출발하기 바로 전 */
                if (sec < 1800) {
                    return 180000;
                    /* 아니면 */
                } else {
                    return 36000000;
                }
                /* 집에서 나왔으면*/
            } else {
                /* 도착하기 조금 전*/
                if (sec2 < 1200) {
                    return 180000;
                    /* 이동중엔*/
                } else {
                    return 9000000;
                }
            }
        } catch (Exception e){ Log.e("HamaService", "Exception"); }
        return 180000;
    }
}
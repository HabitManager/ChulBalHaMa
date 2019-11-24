package com.example.leeseungchan.chulbalhama.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;


import com.example.leeseungchan.chulbalhama.Activities.MainActivity;
import com.example.leeseungchan.chulbalhama.DBHelper;
import com.example.leeseungchan.chulbalhama.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class HamaService extends Service implements GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, GoogleApiClient.ConnectionCallbacks {
    static long countTime = 0;
    static long activityTimes[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    static int lastAction = -1;

    public static final String TAG = MainActivity.class.getSimpleName();
    protected GoogleApiClient googleApiClient; // activity recognition handle
    protected ActivityDetectionBroadcastReceiver mBroadcastReceiver;


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

    public void setLocationHelper(LocationHelper helper) {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("onCreate", "service Creation");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("HamaService", "onStartCommand");


        mBroadcastReceiver = new ActivityDetectionBroadcastReceiver();
        buildGoogleApiClient();
        googleApiClient.connect();//추가
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));


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

        public void handleMessage(android.os.Message msg) {
            if (locationHelper != null) {
                locationHelper.setUpdateInterval(adjustTimeInterval());
            }
            requestActivityUpdates();
            Log.e("HAMAHandler", "Request Activity");


        }
    }

    public int adjustTimeInterval() {

        car = Calendar.getInstance();
        int datOfWeek = car.get(Calendar.DAY_OF_WEEK);
        int hour = car.get(Calendar.HOUR);
        int minute = car.get(Calendar.MINUTE);

        Toast.makeText(getApplicationContext(), "현재시간은 " + hour + "시" + minute + "분 입니다.", Toast.LENGTH_SHORT).show();
        //TODO DB 시간 조회 후 Time Interval 재설정
        return 3000;
    }

    public void requestActivityUpdates() {
        if (!googleApiClient.isConnected()) {
            Log.e("Client Not ", "request Sucess");
            //Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        //System.out.println(this);

        //requestCreated=true;

        //Toast.makeText(this, "Request Generated. Connection success", Toast.LENGTH_SHORT).show();
        ActivityRecognition.ActivityRecognitionApi
                .requestActivityUpdates(googleApiClient, Constants.DETECTION_INTERVAL_IN_MILLISECONDS, getActivityPendingIntent())
                .setResultCallback(this);
        Log.e("ACTIVITY_RECOG", "request Sucess");
        countTime = System.currentTimeMillis();
        lastAction = -1;
    }

    public String removeActivityUpdates() {//지정된 PendingIntent 에 대한 모든 활동 업데이트를 제거.
        if (!googleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return null;
        }
        Toast.makeText(this, "Remove Connection", Toast.LENGTH_SHORT).show();

        ActivityRecognition
                .ActivityRecognitionApi
                .removeActivityUpdates(googleApiClient, getActivityPendingIntent())
                .setResultCallback(this);

        String showingMsg = "";
        for (int i = 0; activityTimes.length > i; i++) {
            if (i == 6)
                continue;
            if (i == 3) {
                if (lastAction == i) {
                    activityTimes[i + 1] += activityTimes[i] + (System.currentTimeMillis() - countTime);
                } else {
                    activityTimes[i + 1] += activityTimes[i];
                }
                continue;
            }
            if (lastAction == i)
                showingMsg += getActivityString(i) + " : " + (activityTimes[i] + (System.currentTimeMillis() - countTime)) + "ms \n";
            else
                showingMsg += getActivityString(i) + " : " + activityTimes[i] + "ms \n";
            activityTimes[i] = 0;
        }
        showingMsg += "학교에 도착하셨습니다. 설문을 수행하시겠습니까? (Y/N)";
        return showingMsg;
    }


    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver {// 리시버
        protected static final String TAG = "receiver";
        private int firstTimeCall = 0;
        float[] confidenceOfActivity = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int maxIdx = 0;
        long accumulatedTime = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<DetectedActivity> updatedActivities = intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);
            String strStatus = "";
            for (DetectedActivity activity : updatedActivities) {
                int activityType = activity.getType(); // activity 타입 추출
                if (activityType == 6)
                    continue;
                confidenceOfActivity[activityType] = activity.getConfidence(); // confidence 기록
                maxIdx = confidenceOfActivity[activityType] > confidenceOfActivity[maxIdx] ? activityType : maxIdx; // 더크다면 큰걸로 기록
                strStatus += getActivityString(activity.getType()) + activity.getConfidence() + "%\n";
            }
            if (maxIdx != -1) {
                accumulatedTime = System.currentTimeMillis() - countTime;
                countTime = System.currentTimeMillis();
                strStatus += "Possibly what you act : " + getActivityString(maxIdx) + confidenceOfActivity[maxIdx] + "% \n";
                if (lastAction != -1)
                    activityTimes[lastAction] += accumulatedTime;
                if (
                        (lastAction == 3 || lastAction == 0) &&  //이전 행동이 멈춰있거나 교통수단에 탑승한 상태 일떄
                                (maxIdx == 3 || maxIdx == 0)  //멈춰있거나 교통수단에 탑승한 상태 일때.
                )
                    strStatus += "일정시간 이상 정지하고 계십니다. 독서(습관)를 수행하세요. \n";
                lastAction = maxIdx;
            }
            if (firstTimeCall == 0) {
                strStatus += "집에서 나왔습니다!";
                firstTimeCall = 1;
            }
            Log.e(TAG, strStatus);
            Toast.makeText(getApplicationContext(), strStatus, Toast.LENGTH_SHORT).show();//appcontext에 토스트
            //detectedActivities.setText(strStatus);

        }

    }

    public String getActivityString(int detectedActivityType) {
        Resources resources = this.getResources();
        switch (detectedActivityType) {
            case DetectedActivity
                    .IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity
                    .ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity
                    .ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity
                    .RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity
                    .STILL:
                return resources.getString(R.string.still);
            case DetectedActivity
                    .TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity
                    .UNKNOWN:
                return resources.getString(R.string.unknown);
            case DetectedActivity
                    .WALKING:
                return resources.getString(R.string.walking);
            default:
                return resources.getString(R.string.unidentifiable_activity, detectedActivityType);
        }
    }

    public synchronized void buildGoogleApiClient() { // 빌더
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API) // Activity Recognition API request
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)//google API client instance 와 리스너를 연관시킴.
                .build();
    }

    public PendingIntent getActivityPendingIntent() {
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnectionSuspended(int i) { //  sensor 커넥션에 문제가 생길시에 호출
        Log.e(TAG, "Connection suspended");
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { // google play connection 에 실패할시에 호출
        Log.e(TAG, "Connection failed: ConnectionResult.getErrorCode()= " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {  // 모든게 잘동작할때, normal activity recognition call
        Log.e(TAG, "Connected to GoogleApiClient");
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Log.e(TAG, "Successfully added activity detection.");
        } else {
            Log.e(TAG, "Error adding or removing activity detection.");
        }
    }

    public void seviceExitProcess() {
        removeActivityUpdates();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        googleApiClient.disconnect();
    }






    static Calendar getNextSchedule(Calendar current){

//        DBHelper helper = new DBHelper(this);
//        SQLiteDatabase db = helper.getWritableDatabase();
//        Cursor cursor = db.rawQuery("select * from history order by _id", null);

        int dow=current.DAY_OF_WEEK;
        Calendar dueDate = Calendar.getInstance();



        dueDate.set(Calendar.HOUR_OF_DAY,dueDate.HOUR); // 오전 6시 30분으로 예약 가정
        dueDate.set(Calendar.MINUTE, dueDate.MINUTE);
        dueDate.set(Calendar.SECOND, dueDate.SECOND);
        return Calendar.getInstance();
    }

    static void StartWorker()
    {
        Log.d("mwm", "MainActivity::StartWorker()");

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        //종료
        Calendar currentDate = Calendar.getInstance();
        Calendar dueDate= getNextSchedule(currentDate);

        dueDate.set(Calendar.HOUR_OF_DAY,6); // 오전 6시 30분으로 예약 가정
        dueDate.set(Calendar.MINUTE, 30);
        dueDate.set(Calendar.SECOND, 0);

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24); // 예정시간이 이미 지났다면, 다음날로 지정
        }

        long timeDiff = dueDate.getTimeInMillis() - currentDate.getTimeInMillis();

        Log.e("workmanager Settings","enqueue"+dueDate.toString());
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(WorkManagerTasks.class)
                .setConstraints(constraints)
                .addTag("myRequest")
                .setInitialDelay(timeDiff, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance().enqueueUniqueWork("uniqueWork", ExistingWorkPolicy.REPLACE,workRequest);

    }


}
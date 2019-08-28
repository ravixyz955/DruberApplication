package com.example.user.druberapplication.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.user.druberapplication.FlightPathsActivity;
import com.example.user.druberapplication.R;
import com.example.user.druberapplication.constant.Constant;

public class InFlightService extends Service {
    private static final String LOG_TAG = "ForegroundService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constant.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Start Foreground Intent ");
            Intent notificationIntent = new Intent(this, FlightPathsActivity.class);
            notificationIntent.setAction(Constant.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_flight_takeoff_white_24dp);
            Bitmap icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Flight Path")
                    .setTicker("Flight Path")
                    .setContentText("Flight Path")
                    .setSmallIcon(R.drawable.ic_flight_takeoff_white_24dp)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();
            startForeground(Constant.NOTIFICATION_ID.FOREGROUND_SERVICE,
                    notification);
//            IconActivity.getInstance().updateUI(String.valueOf(intent.getIntExtra("count", 0)));
            Log.d("KJKJK", "onStartCommand: " + intent.getIntExtra("count", 0));
        } else if (intent.getAction().equals(
                Constant.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }
}

package org.project.smsandcallreceiver.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import org.json.JSONArray;
import org.project.smsandcallreceiver.App;
import org.project.smsandcallreceiver.R;
import org.project.smsandcallreceiver.ReceiversActivity;
import org.project.smsandcallreceiver.helpers.InternalStorage;
import org.project.smsandcallreceiver.helpers.ServerHelper;
import org.project.smsandcallreceiver.threads.SmsAndCallThread;
import org.project.smsandcallreceiver.threads.SingletonThreadStopper;

import java.io.IOException;

public class BackgroundService extends Service {
    private static final String ANDROID_CHANNEL_ID = "com.example.android.smsmessaging";
    private static final int NOTIFICATION_ID = 45;

    private PowerManager.WakeLock wakeLock = null;
    private SmsAndCallThread smsAndCallThread = null;

    public final String TAG = BackgroundService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).
                createNotificationChannel(new NotificationChannel(ANDROID_CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH));

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();
    }

    @Override
    public void onDestroy() {
//        SmsReceiver.disableBroadcastReceiver();
//        CallReceiver.disableBroadcastReceiver();
        SingletonThreadStopper singleton = SingletonThreadStopper.INSTANCE;
        singleton.setRunCallAndSmsReceiversThread(false);
        ServerHelper.getRequest(getString(R.string.stop_service));
        InternalStorage singletonStorage = InternalStorage.INSTANCE;
        try {
            singletonStorage.saveStatusBackgroundService(false);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        wakeLock.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        SmsReceiver.enableBroadcastReceiver();
//        CallReceiver.enableBroadcastReceiver();

        SingletonThreadStopper singleton = SingletonThreadStopper.INSTANCE;
        singleton.setRunCallAndSmsReceiversThread(true);
        smsAndCallThread = new SmsAndCallThread("thread_SmsAndCallReceivers");
        smsAndCallThread.start();

        Intent notificationIntent = new Intent(getApplicationContext(), ReceiversActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(App.getInstance(),
                (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this, ANDROID_CHANNEL_ID).
                setContentIntent(contentIntent).
                setContentTitle(getString(R.string.app_name)).
                        setContentText(getString(R.string.notification_label)).
                        setAutoCancel(true).
                        setSmallIcon(R.mipmap.ic_launcher).build();
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notification);
        startForeground(NOTIFICATION_ID, notification);

        ServerHelper.getRequest(getString(R.string.start_service));

        InternalStorage singletonStorage = InternalStorage.INSTANCE;
        try {
            singletonStorage.saveStatusBackgroundService(true);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        return START_STICKY;
    }
}

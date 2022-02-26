package org.project.smsandcallreceiver.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import org.project.smsandcallreceiver.App;
import org.project.smsandcallreceiver.R;
import org.project.smsandcallreceiver.ReceiversActivity;
import org.project.smsandcallreceiver.helpers.ChatIDTelegramHelper;
import org.project.smsandcallreceiver.helpers.ServerHelper;
import org.project.smsandcallreceiver.receivers.CallReceiver;
import org.project.smsandcallreceiver.receivers.SmsReceiver;

public class BackgroundService extends Service {
    private static final String ANDROID_CHANNEL_ID = "com.example.android.smsmessaging";
    private static final int NOTIFICATION_ID = 45;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).
                createNotificationChannel(new NotificationChannel(ANDROID_CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH));
    }

    public void onDestroy() {
        SmsReceiver.disableBroadcastReceiver();
        CallReceiver.disableBroadcastReceiver();
        ServerHelper.getRequest(ChatIDTelegramHelper.RangeChatID, getString(R.string.stop_service));
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        SmsReceiver.enableBroadcastReceiver();
        CallReceiver.enableBroadcastReceiver();

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

        ServerHelper.getRequest(ChatIDTelegramHelper.RangeChatID, getString(R.string.start_service));
        return START_STICKY;
    }
}

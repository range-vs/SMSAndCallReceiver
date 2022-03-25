package org.project.smsandcallreceiver.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import org.project.smsandcallreceiver.App;
import org.project.smsandcallreceiver.R;
import org.project.smsandcallreceiver.ReceiversActivity;
import org.project.smsandcallreceiver.helpers.ChatIDTelegramHelper;
import org.project.smsandcallreceiver.helpers.InternalStorage;
import org.project.smsandcallreceiver.helpers.ServerHelper;

import java.io.IOException;

public class BootBroadcast extends BroadcastReceiver {

    public final String TAG = BootBroadcast.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        InternalStorage singletonStorage = InternalStorage.INSTANCE;
        boolean value = false;
        try {
            value = singletonStorage.readStatusBackgroundService();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        if(value) {
            context.startForegroundService(new Intent(context, BackgroundService.class));
            ServerHelper.getRequest(context.getString(R.string.reboot_label));
        }
    }
}

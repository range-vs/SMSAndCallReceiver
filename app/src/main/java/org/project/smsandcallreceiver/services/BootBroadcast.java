package org.project.smsandcallreceiver.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.project.smsandcallreceiver.App;
import org.project.smsandcallreceiver.R;
import org.project.smsandcallreceiver.helpers.ChatIDTelegramHelper;
import org.project.smsandcallreceiver.helpers.ServerHelper;

public class BootBroadcast extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        context.startForegroundService(new Intent(context, BackgroundService.class));
        ServerHelper.getRequest(ChatIDTelegramHelper.RangeChatID, context.getString(R.string.reboot_label));
    }
}

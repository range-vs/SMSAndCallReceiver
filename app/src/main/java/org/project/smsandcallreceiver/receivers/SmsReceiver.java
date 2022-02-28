package org.project.smsandcallreceiver.receivers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import org.project.smsandcallreceiver.App;
import org.project.smsandcallreceiver.helpers.ChatIDTelegramHelper;
import org.project.smsandcallreceiver.helpers.ServerHelper;

import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";

    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        StringBuilder strMessage = new StringBuilder();
        String format = bundle.getString("format");
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            boolean isVersionM = Build.VERSION.SDK_INT >= 23;
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                if (isVersionM) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (i == 0) {
                    strMessage.append("[" + new Date() + "]").append("SMS from ").append(msgs[i].getOriginatingAddress()).append(": ");
                }
                strMessage.append(msgs[i].getMessageBody());
            }
            strMessage.append("\n");
            Log.d(TAG, "onReceiveSMS: " + strMessage);
            Toast.makeText(App.getInstance(), strMessage.toString(), Toast.LENGTH_LONG).show();
            ServerHelper.getRequest(strMessage.toString());
        }
    }

    public static void enableBroadcastReceiver() {
        ComponentName receiver = new ComponentName(App.getInstance(), SmsReceiver.class);
        PackageManager pm = App.getInstance().getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Toast.makeText(App.getInstance(), "Enabled sms broadcast receiver", Toast.LENGTH_LONG).show();
    }

    public static void disableBroadcastReceiver() {
        ComponentName receiver = new ComponentName(App.getInstance(), SmsReceiver.class);
        PackageManager pm = App.getInstance().getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Toast.makeText(App.getInstance(), "Disabled sms broadcast receiver", Toast.LENGTH_LONG).show();
    }
}

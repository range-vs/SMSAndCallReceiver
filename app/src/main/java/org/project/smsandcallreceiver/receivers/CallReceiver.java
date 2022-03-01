package org.project.smsandcallreceiver.receivers;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import org.project.smsandcallreceiver.App;
import org.project.smsandcallreceiver.ReceiversActivity;
import org.project.smsandcallreceiver.helpers.ChatIDTelegramHelper;
import org.project.smsandcallreceiver.helpers.ServerHelper;

import java.util.Date;

public class CallReceiver extends PhonecallReceiver {
    private static final String TAG = CallReceiver.class.getSimpleName();

    public void onIncomingCallReceived(Context ctx, String number, Date start, String operator, String target_number) {
        StringBuilder strMessage = new StringBuilder();
        strMessage.append("[" + new Date() + "]").append("Current SIM: ").append(operator).append(". ")
                .append("Target number: ").append(target_number).append(". ").append("Incoming call from ").append(number).append("\n");
        Log.d(TAG, "onReceiveIncomingCall: " + strMessage);
        ReceiversActivity.instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.getInstance(), strMessage.toString(), Toast.LENGTH_LONG).show();
            }
        });
        ServerHelper.getRequest(strMessage.toString());
    }

    public void onIncomingCallAnswered(Context ctx, String number, Date start, String operator, String target_number) {
    }

    public void onIncomingCallEnded(Context ctx, String number, Date start, Date end, String operator, String target_number) {
    }

    public void onOutgoingCallStarted(Context ctx, String number, Date start, String operator, String target_number) {
    }

    public void onOutgoingCallEnded(Context ctx, String number, Date start, Date end, String operator, String target_number) {
    }

    public void onMissedCall(Context ctx, String number, Date start, String operator, String target_number) {
    }

    public static void enableBroadcastReceiver() {
        ComponentName receiver = new ComponentName(App.getInstance(), CallReceiver.class);
        PackageManager pm = App.getInstance().getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Toast.makeText(App.getInstance(), "Enabled call broadcast receiver", Toast.LENGTH_LONG).show();
    }

    public static void disableBroadcastReceiver() {
        ComponentName receiver = new ComponentName(App.getInstance(), CallReceiver.class);
        PackageManager pm = App.getInstance().getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Toast.makeText(App.getInstance(), "Disabled call broadcast receiver", Toast.LENGTH_LONG).show();
    }
}

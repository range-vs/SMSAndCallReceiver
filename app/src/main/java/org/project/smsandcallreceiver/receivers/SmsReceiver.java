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
import org.project.smsandcallreceiver.ReceiversActivity;
import org.project.smsandcallreceiver.helpers.Logger;
import org.project.smsandcallreceiver.helpers.ServerHelper;
import org.project.smsandcallreceiver.helpers.telephony.SIMData;
import org.project.smsandcallreceiver.helpers.telephony.TelephonyLogs;

import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";

    private Object object = new Object();

    public void _onReceive(Context context, Intent intent) {
        synchronized (object) {

//            Bundle bundle = intent.getExtras();
//            SIMData simData = TelephonyLogs.getSMSLog(context);
//
//            StringBuilder strMessage = new StringBuilder();
//            String format = bundle.getString("format");
//            Object[] pdus = (Object[]) bundle.get(pdu_type);
//            if (pdus != null) {
//                boolean isVersionM = Build.VERSION.SDK_INT >= 23;
//                SmsMessage[] msgs = new SmsMessage[pdus.length];
//                for (int i = 0; i < msgs.length; i++) {
//                    if (isVersionM) {
//                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
//                    } else {
//                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                    }
//                    if (i == 0) {
//                        strMessage.append("[" + new Date() + "]").append("Current SIM: ").append(simData.getOperatorSimName()).append(". ")
//                                .append("Target number: ").append(simData.getNumber()).append(". ").append("SMS from ").append(msgs[i].getOriginatingAddress()).append(": ");
//                    }
//                    strMessage.append(msgs[i].getMessageBody());
//                }
//                strMessage.append("\n");
//                strMessage = new StringBuilder(Logger.generateMsg(strMessage.toString()));
//                Log.d(TAG, strMessage.toString());
//                Logger.writeLog(strMessage.toString());
//                StringBuilder finalStrMessage = strMessage;
//                ReceiversActivity.instance.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(App.getInstance(), finalStrMessage.toString(), Toast.LENGTH_LONG).show();
//                    }
//                });
//                ServerHelper.getRequest(strMessage.toString());
//            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Runnable r = ()->{
            try{
                Thread.sleep(10000);
            }
            catch(InterruptedException e){
                Logger.writeLog("threadHookSMS _onReceive has been interrupted");
                System.out.println("threadHookSMS _onReceive has been interrupted");
            }
            _onReceive(context, intent);
        };
        Thread threadHookSMS = new Thread(r,"threadHookSMS");
        threadHookSMS.start();
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

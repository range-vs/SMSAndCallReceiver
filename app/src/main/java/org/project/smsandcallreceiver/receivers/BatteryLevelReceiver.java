package org.project.smsandcallreceiver.receivers;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

import org.project.smsandcallreceiver.App;
import org.project.smsandcallreceiver.helpers.BatteryHelper;
import org.project.smsandcallreceiver.helpers.ServerHelper;

public class BatteryLevelReceiver extends BroadcastReceiver {

    private static final String TAG = BatteryLevelReceiver.class.getSimpleName();

    private int lowTargetBatteryLevel = 20;
    private int highTargetBatteryLevel = 80;

    private static BatteryLevelReceiver batteryLevelReceiver = null;
    private static int lastBatteryLevel = -1;

    private enum StatusBattery {
        ENABLE_CHARGING,
        DISABLE_CHARGING
    }
    private static StatusBattery lastStatusBattery;
    
    @Override
    public void onReceive(Context context, Intent intent) {

        int batteryLevel = BatteryHelper.getButteryLevel(context);

        if(lastBatteryLevel != batteryLevel) {
            lastBatteryLevel = batteryLevel;
            if (batteryLevel == lowTargetBatteryLevel) {
                if(lastStatusBattery == StatusBattery.ENABLE_CHARGING){
                    return;
                }
                lastStatusBattery = StatusBattery.ENABLE_CHARGING;
                ServerHelper.getRequest("Current battery level: " + batteryLevel + "%. Enable charging");
            } else if (batteryLevel == highTargetBatteryLevel) {
                if(lastStatusBattery == StatusBattery.DISABLE_CHARGING){
                    return;
                }
                lastStatusBattery = StatusBattery.DISABLE_CHARGING;
                ServerHelper.getRequest("Current battery level: " + batteryLevel + "%. Disable charging");
            } else if (batteryLevel % 10 == 0) {
                ServerHelper.getRequest("Current battery level: " + batteryLevel + "%");
            }
        }

    }

    public static void enableBroadcastReceiver() {
        batteryLevelReceiver = new BatteryLevelReceiver();
        App.getInstance().registerReceiver(batteryLevelReceiver, new IntentFilter( Intent.ACTION_BATTERY_CHANGED ) );

        Toast.makeText(App.getInstance(), "Enabled battery level broadcast receiver", Toast.LENGTH_LONG).show();
    }

    public static void disableBroadcastReceiver() {
        App.getInstance().unregisterReceiver(batteryLevelReceiver);

        Toast.makeText(App.getInstance(), "Disabled battery level broadcast receiver", Toast.LENGTH_LONG).show();
    }

}

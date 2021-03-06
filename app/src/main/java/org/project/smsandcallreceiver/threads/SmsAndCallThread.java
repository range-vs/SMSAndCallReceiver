package org.project.smsandcallreceiver.threads;

import android.util.Log;
import android.widget.Toast;

import org.project.smsandcallreceiver.App;
import org.project.smsandcallreceiver.ReceiversActivity;
import org.project.smsandcallreceiver.helpers.Logger;
import org.project.smsandcallreceiver.helpers.ServerHelper;
import org.project.smsandcallreceiver.helpers.telephony.SIMData;
import org.project.smsandcallreceiver.helpers.telephony.TelephonyLogs;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


public class SmsAndCallThread extends Thread {

    private static final String TAG = SmsAndCallThread.class.getSimpleName();

    private LinkedHashMap<String, SIMData> callLogs;
    private LinkedHashMap <String, SIMData> smsLogs;

    public SmsAndCallThread(String name){
        super(name);
        callLogs = new LinkedHashMap <>();
        smsLogs = new LinkedHashMap <>();
    }

    public void run() {

        // стартовые значения при запуске службы
        callLogs = TelephonyLogs.getCallsLog(App.getInstance());
        smsLogs = TelephonyLogs.getSMSLog(App.getInstance());

//        int countMinutes = 0;
        while (true) {
            SingletonThreadStopper singleton = SingletonThreadStopper.INSTANCE;
            if(!singleton.isRunCallAndSmsReceiversThread()){
                return;
            }
            try {
                LinkedHashMap <String, SIMData> callLogsNew = TelephonyLogs.getCallsLog(App.getInstance());
                LinkedHashMap <String, SIMData> smsLogsNew = TelephonyLogs.getSMSLog(App.getInstance());
                if(mergeCallLogs(callLogs, callLogsNew)){
                    callLogs = callLogsNew;
                }
                if(mergeSmsLogs(smsLogs, smsLogsNew)){
                    smsLogs = smsLogsNew;
                }
                Thread.sleep(60000); // ждем 1 минуту
//                ++countMinutes;
//                if (countMinutes == 240) { // четыре часа
//                    ServerHelper.getRequest("Current battery level: " + BatterHelper.getButteryLevel(App.getInstance()) + "%");
//                    countMinutes = 0;
//                }
            }
            catch(InterruptedException e){
                Log.e(TAG, e.toString());
                //System.out.println("Thread has been interrupted");
            }
        }
    }

    public static boolean mergeCallLogs(HashMap<String, SIMData> callLogsOld, HashMap<String, SIMData> callLogsNew){
        if(callLogsNew.size() == 0){
            return false;
        }
//        if(callLogsOld.size() == 0){
//            callLogsOld.putAll(callLogsNew);
//            return true;
//        }
        Optional<String> firstElementOld = callLogsOld.keySet().stream().findFirst();
        int i = 0;
        for(Map.Entry<String, SIMData> elemNew: callLogsNew.entrySet()){
            if(firstElementOld.isPresent() && firstElementOld.get().equals(elemNew.getKey())){
                if(i == 0){
                    return false;
                }
                break;
            }
            String callDay = new SimpleDateFormat("yyyy MM dd HH:mm:ss").format(new Date(Long.parseLong(elemNew.getKey())));
            StringBuilder strMessage = new StringBuilder();
            strMessage.append("[" + callDay + "]").append("Current SIM: ").append(elemNew.getValue().getOperatorSimName()).append(". ")
                    .append("Target number: ").append(elemNew.getValue().getCurrentSimPhoneNumber()).append(". ")
                    .append("Incoming call ended from ").append(elemNew.getValue().getIncomingPhoneNumber()).append("\n");
            strMessage = new StringBuilder(Logger.generateMsg(strMessage.toString()));
            Log.d(TAG, strMessage.toString());
            Logger.writeLog(strMessage.toString());
            StringBuilder finalStrMessage = strMessage;
            ReceiversActivity.instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(App.getInstance(), finalStrMessage.toString(), Toast.LENGTH_LONG).show();
                }
            });
            ServerHelper.getRequest(strMessage.toString());
            i++;
        }
        return true;
    }

    public static boolean mergeSmsLogs(HashMap<String, SIMData> smsLogsOld, HashMap<String, SIMData> smsLogsNew){
        if(smsLogsNew.size() == 0){
            return false;
        }
//        if(smsLogsOld.size() == 0){
//            smsLogsOld.putAll(smsLogsNew);
//            return true;
//        }
        Optional<String> firstElementOld = smsLogsOld.keySet().stream().findFirst();
        int i = 0;
        for(Map.Entry<String, SIMData> elemNew: smsLogsNew.entrySet()){
            if(firstElementOld.isPresent() && firstElementOld.get().equals(elemNew.getKey())){
                if(i == 0){
                    return false;
                }
                break;
            }
            String callDay = new SimpleDateFormat("yyyy MM dd HH:mm:ss").format(new Date(Long.parseLong(elemNew.getKey())));
            StringBuilder strMessage = new StringBuilder();
            strMessage.append("[" +  callDay + "]").append("Current SIM: ").append(elemNew.getValue().getOperatorSimName()).append(". ")
                    .append("Target number: ").append(elemNew.getValue().getCurrentSimPhoneNumber()).append(". ")
                    .append("Incoming SMS from ").append(elemNew.getValue().getIncomingPhoneNumber()).append(". ")
                    .append("Message: ").append(elemNew.getValue().getSmdMessage()).
                    append("\n");
            strMessage = new StringBuilder(Logger.generateMsg(strMessage.toString()));
            Log.d(TAG, strMessage.toString());
            Logger.writeLog(strMessage.toString());
            StringBuilder finalStrMessage = strMessage;
            ReceiversActivity.instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(App.getInstance(), finalStrMessage.toString(), Toast.LENGTH_LONG).show();
                }
            });
            ServerHelper.getRequest(strMessage.toString());
            i++;
        }
        return true;
    }


}


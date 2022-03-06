package org.project.smsandcallreceiver.helpers.telephony;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.Telephony;
import android.telephony.SubscriptionInfo;

import org.project.smsandcallreceiver.App;

import java.util.List;

public class TelephonyLogs {

    public static SIMData getCallsLog(Context context){
        SIMData output = null;
        List<SubscriptionInfo> infoList = DualSim.getSimsInfo(context);

        String[] projection = new String[] {
                CallLog.Calls.TYPE,
                CallLog.Calls.PHONE_ACCOUNT_ID
        };
        int slot = -1;
        Cursor managedCursor =  App.getInstance().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DATE + " DESC limit 1;");
        while (managedCursor.moveToFirst()) {
            String type = managedCursor.getString(0); // type
            String target = managedCursor.getString(1); // sim

            int dircode = Integer.parseInt(type);
            if(dircode == CallLog.Calls.INCOMING_TYPE || dircode == CallLog.Calls.MISSED_TYPE){
                for(SubscriptionInfo infoSim: infoList){
                    if(String.valueOf(infoSim.getSubscriptionId()).equals(target) || target.contains(String.valueOf(infoSim.getSubscriptionId()))){
                        slot = infoSim.getSimSlotIndex();
                        output = new SIMData(infoSim.getNumber(), DualSim.getOutput(App.getInstance(), "getSimOperatorName", slot));
                        break;
                    }
                }
                break;
            }
        }
        managedCursor.close();
        return output;
    }

    public static SIMData getSMSLog(Context context) {
        SIMData output = null;
        List<SubscriptionInfo> infoList = DualSim.getSimsInfo(context);

        String[] projection = new String[]{
                Telephony.Sms.TYPE,
                Telephony.Sms.SUBSCRIPTION_ID
        };
        int slot = -1;
        Cursor managedCursor = App.getInstance().getContentResolver().query(Telephony.Sms.CONTENT_URI, projection, null, null, Telephony.Sms.DATE + " DESC limit 1;");
        while (managedCursor.moveToFirst()) {
            String type = managedCursor.getString(0); // type
            String target = managedCursor.getString(1); // sim

            int dircode = Integer.parseInt(type);
            if (dircode == Telephony.Sms.MESSAGE_TYPE_INBOX || dircode == CallLog.Calls.MISSED_TYPE) {
                for (SubscriptionInfo infoSim : infoList) {
                    if (String.valueOf(infoSim.getSubscriptionId()).equals(target) || target.contains(String.valueOf(infoSim.getSubscriptionId()))) {
                        slot = infoSim.getSimSlotIndex();
                        output = new SIMData(infoSim.getNumber(), DualSim.getOutput(App.getInstance(), "getSimOperatorName", slot));
                        break;
                    }
                }
                break;
            }
        }
        managedCursor.close();
        return output;
    }

}

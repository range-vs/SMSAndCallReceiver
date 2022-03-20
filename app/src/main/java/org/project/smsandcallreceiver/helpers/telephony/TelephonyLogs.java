package org.project.smsandcallreceiver.helpers.telephony;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.Telephony;
import android.telephony.SubscriptionInfo;

import org.project.smsandcallreceiver.App;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TelephonyLogs {

    private static final int countRecords = 50; // количество крайних записей для забора из журнала

    public static LinkedHashMap<String, SIMData> getCallsLog(Context context){
        LinkedHashMap <String, SIMData> data = new LinkedHashMap <String, SIMData>();
        SIMData output = null;
        List<SubscriptionInfo> infoList = DualSim.getSimsInfo(context);

        String[] projection = new String[] {
                CallLog.Calls.TYPE,
                CallLog.Calls.PHONE_ACCOUNT_ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE
        };
        int slot = -1;
        Cursor managedCursor =  App.getInstance().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DATE + " DESC limit " + countRecords + ";");
        while (!managedCursor.isLast()) {
            String type = managedCursor.getString(0); // type
            String target = managedCursor.getString(1); // sim
            String number = managedCursor.getString(2); // number
            String date = managedCursor.getString(3); // date

            int dircode = Integer.parseInt(type);
            if(dircode == CallLog.Calls.INCOMING_TYPE || dircode == CallLog.Calls.MISSED_TYPE){
                for(SubscriptionInfo infoSim: infoList){
                    if(String.valueOf(infoSim.getSubscriptionId()).equals(target) || target.contains(String.valueOf(infoSim.getSubscriptionId()))){
                        slot = infoSim.getSimSlotIndex();
                        output = new SIMData(infoSim.getNumber(),
                                DualSim.getOutput(App.getInstance(), "getSimOperatorName", slot),
                                number,
                                "");
                        data.put(date, output);
                        break;
                    }
                }
            }
            managedCursor.moveToNext();
        }
        managedCursor.close();
        return data;
    }

    public static LinkedHashMap <String, SIMData> getSMSLog(Context context) {
        LinkedHashMap <String, SIMData> data = new LinkedHashMap <String, SIMData>();
        SIMData output = null;
        List<SubscriptionInfo> infoList = DualSim.getSimsInfo(context);

        String[] projection = new String[]{
                Telephony.Sms.TYPE,
                Telephony.Sms.SUBSCRIPTION_ID,
                Telephony.Sms.ADDRESS,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE
        };
        int slot = -1;
        Cursor managedCursor = App.getInstance().getContentResolver().query(Telephony.Sms.CONTENT_URI, projection, null, null, Telephony.Sms.DATE + " DESC limit " + countRecords + ";");
        while (!managedCursor.isLast()) {
            String type = managedCursor.getString(0); // type
            String target = managedCursor.getString(1); // sim
            String number = managedCursor.getString(2); // number
            String msg = managedCursor.getString(3); // msg
            String date = managedCursor.getString(4); // date

            int dircode = Integer.parseInt(type);
            if (dircode == Telephony.Sms.MESSAGE_TYPE_INBOX || dircode == CallLog.Calls.MISSED_TYPE) {
                for (SubscriptionInfo infoSim : infoList) {
                    if (String.valueOf(infoSim.getSubscriptionId()).equals(target) || target.contains(String.valueOf(infoSim.getSubscriptionId()))) {
                        slot = infoSim.getSimSlotIndex();
                        output = new SIMData(infoSim.getNumber(),
                                DualSim.getOutput(App.getInstance(), "getSimOperatorName", slot),
                                number,
                                msg);
                        data.put(date, output);
                        break;
                    }
                }
            }
            managedCursor.moveToNext();
        }
        managedCursor.close();
        return data;
    }

}

package org.project.smsandcallreceiver.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import org.project.smsandcallreceiver.helpers.Logger;
import org.project.smsandcallreceiver.helpers.telephony.SIMData;
import org.project.smsandcallreceiver.helpers.telephony.TelephonyLogs;

import java.util.Date;

public abstract class PhonecallReceiver extends BroadcastReceiver {

    private static Date callStartTime;
    private static boolean isIncoming;
    private static int lastState = 0;
    private static String savedNumber;

    private Object object = new Object();

    public abstract void onIncomingCallAnswered(Context context, String str, Date date, String operator, String target_number);

    public abstract void onIncomingCallEnded(Context context, String str, Date date, Date date2, String operator, String target_number);

    public abstract void onIncomingCallReceived(Context context, String str, Date date, String operator, String target_number);

    public abstract void onMissedCall(Context context, String str, Date date, String operator, String target_number);

    public abstract void onOutgoingCallEnded(Context context, String str, Date date, Date date2, String operator, String target_number);

    public abstract void onOutgoingCallStarted(Context context, String str, Date date, String operator, String target_number);

    public void _onReceive(Context context, Intent intent) {
        synchronized (object) {
//            SIMData simData = TelephonyLogs.getCallsLog(context);
//
//            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
//                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
//                return;
//            }
//            String stateStr = intent.getExtras().getString("state");
//            String number = intent.getExtras().getString("incoming_number");
//            int state = 0;
//            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//                state = 0;
//            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
//                state = 2;
//            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//                state = 1;
//            }
//            onCallStateChanged(context, state, number, simData.getOperatorSimName(), simData.getNumber());
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Runnable r = ()->{
            try{
                Thread.sleep(10000);
            }
            catch(InterruptedException e){
                Logger.writeLog("threadHookCalls _onReceive has been interrupted");
                System.out.println("threadHookCalls _onReceive has been interrupted");
            }
            _onReceive(context, intent);
        };
        Thread threadHookCalls = new Thread(r,"threadHookCalls");
        threadHookCalls.start();
    }

    public void onCallStateChanged(Context context, int state, String number, String operator, String target_number) {
        int i;
        if (number != null && !number.isEmpty() && (i = lastState) != state) {
            if (state != 0) {
                if (state == 1) {
                    isIncoming = true;
                    Date date = new Date();
                    callStartTime = date;
                    savedNumber = number;
                    onIncomingCallReceived(context, number, date, operator, target_number);
                } else if (state == 2) {
                    if (i != 1) {
                        isIncoming = false;
                        Date date2 = new Date();
                        callStartTime = date2;
                        onOutgoingCallStarted(context, savedNumber, date2, operator, target_number);
                    } else {
                        isIncoming = true;
                        Date date3 = new Date();
                        callStartTime = date3;
                        onIncomingCallAnswered(context, savedNumber, date3, operator, target_number);
                    }
                }
            } else if (i == 1) {
                onMissedCall(context, savedNumber, callStartTime, operator, target_number);
            } else if (isIncoming) {
                onIncomingCallEnded(context, savedNumber, callStartTime, new Date(), operator, target_number );
            } else {
                onOutgoingCallEnded(context, savedNumber, callStartTime, new Date(), operator, target_number);
            }
            lastState = state;
        }
    }
}

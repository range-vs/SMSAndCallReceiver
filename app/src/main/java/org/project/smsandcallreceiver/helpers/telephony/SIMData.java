package org.project.smsandcallreceiver.helpers.telephony;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SIMData {

    private String currentSimPhoneNumber;
    private String operatorSimName;
    private String incomingPhoneNumber;
    private String smdMessage;

    public SIMData() {
        currentSimPhoneNumber = operatorSimName = incomingPhoneNumber = smdMessage = null;
    }

    public SIMData(String currentSimPhoneNumber, String operatorSimName, String incomingPhoneNumber, String smdMessage) {
        setCurrentSimPhoneNumber(currentSimPhoneNumber);
        setOperatorSimName(operatorSimName);
        setIncomingPhoneNumber(incomingPhoneNumber);
        setSmdMessage(smdMessage);
    }

    public String getCurrentSimPhoneNumber() {
        return currentSimPhoneNumber;
    }

    public void setCurrentSimPhoneNumber(String currentSimPhoneNumber) {
        if(isValidPhoneNumber(currentSimPhoneNumber)){
            currentSimPhoneNumber = "+" + currentSimPhoneNumber;
        }
        this.currentSimPhoneNumber = currentSimPhoneNumber;
    }

    public String getOperatorSimName() {
        return operatorSimName;
    }

    public void setOperatorSimName(String operatorSimName) {
        this.operatorSimName = operatorSimName;
    }

    public String getIncomingPhoneNumber() {
        return incomingPhoneNumber;
    }

    public void setIncomingPhoneNumber(String incomingPhoneNumber) {
//        if(isValidPhoneNumber(incomingPhoneNumber)){
//            incomingPhoneNumber = "+" + incomingPhoneNumber;
//        }
        this.incomingPhoneNumber = incomingPhoneNumber;
    }

    public String getSmdMessage() {
        return smdMessage;
    }

    public void setSmdMessage(String smdMessage) {
        this.smdMessage = smdMessage;
    }

    public static boolean isValidPhoneNumber(String s) {
        Pattern p = Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$");
        if(s == null || s.equals("")){
            return false;
        }
        Matcher m = p.matcher(s);
        return m.matches();
    }
}

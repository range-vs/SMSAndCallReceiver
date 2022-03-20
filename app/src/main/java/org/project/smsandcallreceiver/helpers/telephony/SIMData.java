package org.project.smsandcallreceiver.helpers.telephony;

public class SIMData {

    private String currentSimPhoneNumber;
    private String operatorSimName;
    private String incomingPhoneNumber;
    private String smdMessage;

    public SIMData() {
        currentSimPhoneNumber = operatorSimName = incomingPhoneNumber = smdMessage = null;
    }

    public SIMData(String currentSimPhoneNumber, String operatorSimName, String incomingPhoneNumber, String smdMessage) {
        this.currentSimPhoneNumber = currentSimPhoneNumber;
        this.operatorSimName = operatorSimName;
        this.incomingPhoneNumber = incomingPhoneNumber;
        this.smdMessage = smdMessage;
    }

    public String getCurrentSimPhoneNumber() {
        return currentSimPhoneNumber;
    }

    public void setCurrentSimPhoneNumber(String currentSimPhoneNumber) {
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
        this.incomingPhoneNumber = incomingPhoneNumber;
    }

    public String getSmdMessage() {
        return smdMessage;
    }

    public void setSmdMessage(String smdMessage) {
        this.smdMessage = smdMessage;
    }
}

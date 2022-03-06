package org.project.smsandcallreceiver.helpers.telephony;

public class SIMData {

    private String phone;
    private String operator;

    public SIMData() {
        phone = operator = null;
    }

    public SIMData(String phone, String operator) {
        this.phone = phone;
        this.operator = operator;
    }

    public String getNumber() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}

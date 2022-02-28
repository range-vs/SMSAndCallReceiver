package org.project.smsandcallreceiver.threads;

public enum SingletonThreadStopper {

    INSTANCE;

    private boolean isRun = true;

    public void setStatusRun(boolean sr){
        isRun = sr;
    }

    public boolean isRun(){
        return isRun;
    }

}

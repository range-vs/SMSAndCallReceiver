package org.project.smsandcallreceiver.threads;

public enum SingletonThreadStopper {

    INSTANCE;

    private boolean isRunInternetThread = true;
    private boolean isRunCallAndSmsReceiversThread = true;

    public boolean isRunInternetThread() {
        return isRunInternetThread;
    }

    public void setRunInternetThread(boolean runInternetThread) {
        isRunInternetThread = runInternetThread;
    }

    public boolean isRunCallAndSmsReceiversThread() {
        return isRunCallAndSmsReceiversThread;
    }

    public void setRunCallAndSmsReceiversThread(boolean runCallAndSmsReceiversThread) {
        isRunCallAndSmsReceiversThread = runCallAndSmsReceiversThread;
    }
}

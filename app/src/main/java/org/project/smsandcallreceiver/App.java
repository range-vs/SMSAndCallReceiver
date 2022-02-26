package org.project.smsandcallreceiver;

import android.app.Application;

public class App extends Application {
    private static App singleton;

    public static App getInstance() {
        return singleton;
    }

    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
}

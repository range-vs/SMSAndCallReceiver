package org.project.smsandcallreceiver.threads;

import android.util.Log;

import org.project.smsandcallreceiver.helpers.ServerHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class InternetThread extends Thread {

    private static final String TAG = InternetThread.class.getSimpleName();

    public InternetThread(String name){
        super(name);
    }

    public void run() {

        while (true) {
            SingletonThreadStopper singleton = SingletonThreadStopper.INSTANCE;
            if(!singleton.isRunInternetThread()){
                return;
            }
            try {
                if(isOnline()) {
                    ServerHelper.getRequestSavedData();
                }
                Thread.sleep(300000); // ждем 5 минут
            }
            catch(InterruptedException e){
                //System.out.println("Thread has been interrupted");
                Log.e(TAG, e.toString());
            }
        }
    }

    private boolean isOnline() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("api.telegram.org", 443), 30000); // полминуты на проверку
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}

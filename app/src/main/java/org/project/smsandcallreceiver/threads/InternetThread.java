package org.project.smsandcallreceiver.threads;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.project.smsandcallreceiver.helpers.ServerHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class InternetThread extends Thread {

    public InternetThread(String name){
        super(name);
    }

    public void run() {

        while (true) {
            SingletonThreadStopper singleton = SingletonThreadStopper.INSTANCE;
            if(!singleton.isRun()){
                return;
            }
            try {
                if(isOnline()) {
                    ServerHelper.getRequestSavedData();
                }
                Thread.sleep(300000); // ждем 5 минут
            }
            catch(InterruptedException e){
                System.out.println("Thread has been interrupted");
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

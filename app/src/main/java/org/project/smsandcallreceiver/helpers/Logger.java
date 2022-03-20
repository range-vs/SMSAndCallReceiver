package org.project.smsandcallreceiver.helpers;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Logger {

    private static String filename = "log_sms_call_receiver.txt";
    private static int counter = 0;

    public static void remove(){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),filename);
        if(file.exists()){
            file.delete();
        }
    }

    public static String generateMsg(String text){
        return counter++ + ") " + text;
    }

    public static void writeLog(String text){
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),filename);
            FileOutputStream stream = new FileOutputStream(file, true);
            stream.write(text.getBytes());
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

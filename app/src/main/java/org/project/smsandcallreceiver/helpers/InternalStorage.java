package org.project.smsandcallreceiver.helpers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.project.smsandcallreceiver.App;
import org.project.smsandcallreceiver.ReceiversActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;

public enum  InternalStorage {

    INSTANCE;

    public final String fileName = "requests.json";
    public final String fileName_backgroundService = "back_service.dat";
    public final String TAG = ReceiversActivity.class.getSimpleName();

    private Object object = new Object();

    public void saveData(JSONArray json) throws IOException {
        synchronized (object) {
            FileOutputStream out = App.getInstance().openFileOutput(fileName, MODE_PRIVATE);
            out.write(json.toString().getBytes());
            out.close();
        }
    }

    public JSONArray readData() throws IOException, JSONException {
        synchronized (object) {
            JSONArray jsonArray = null;
            FileInputStream in = App.getInstance().openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            jsonArray = new JSONArray(sb.toString());
            return jsonArray;
        }
    }

    public void saveStatusBackgroundService(boolean status) throws IOException {
        synchronized (object) {
            FileOutputStream out = App.getInstance().openFileOutput(fileName_backgroundService, MODE_PRIVATE);
            out.write(String.valueOf(status).getBytes());
            out.close();
        }
    }

    public boolean readStatusBackgroundService() throws IOException {
        synchronized (object) {
            FileInputStream in = App.getInstance().openFileInput(fileName_backgroundService);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            if(sb.toString().equals("")){
                throw new IOException("[fileName_backgroundService] Value is not valid!");
            }
            boolean val = Boolean.parseBoolean(sb.toString());
            return val;
        }
    }

}

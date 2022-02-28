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
    public final String TAG = ReceiversActivity.class.getSimpleName();

    public void saveData(JSONArray json) throws IOException {
        FileOutputStream out = App.getInstance().openFileOutput(fileName, MODE_PRIVATE);
        out.write(json.toString().getBytes());
        out.close();
    }

    public JSONArray readData() throws IOException, JSONException {
        JSONArray jsonArray = null;
        FileInputStream in = App.getInstance().openFileInput(fileName);
        BufferedReader br= new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String s = null;
        while((s= br.readLine())!= null)  {
            sb.append(s);
        }
        jsonArray = new JSONArray(sb.toString());
        return jsonArray;
    }

}

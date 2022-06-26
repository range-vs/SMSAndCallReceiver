package org.project.smsandcallreceiver.helpers;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.project.smsandcallreceiver.App;
import org.project.smsandcallreceiver.R;

import java.io.File;

public class ServerHelper {

    private static String telegramBotID = App.getInstance().getString(R.string.telegram_bot_id);
    private static String endPoint = "https://api.telegram.org/bot" + telegramBotID + "/sendMessage?";

    public static void init(){
//        NetworkRequest networkRequest = new NetworkRequest.Builder()
//                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
//                .build();
//        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
//            @Override
//            public void onAvailable(@NonNull Network network) {
//                super.onAvailable(network);
//                getRequestSavedData(chat_id);
//            }
//
//            @Override
//            public void onLost(@NonNull Network network) {
//                super.onLost(network);
//            }
//
//            @Override
//            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
//                super.onCapabilitiesChanged(network, networkCapabilities);
//                //final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
//            }
//        };
//        ConnectivityManager connectivityManager = App.getInstance().getSystemService(ConnectivityManager.class);
//        connectivityManager.requestNetwork(networkRequest, networkCallback);
    }

    public static void getRequest(String text) {
        InternalStorage singleton = InternalStorage.INSTANCE;

        text = text.replace("+", "%2b");
        String url = endPoint + "chat_id=" + ChatIDTelegramHelper.RangeChatID + "&text=" + text;
        RequestQueue queue = Volley.newRequestQueue(App.getInstance());

        String finalText = text;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Log.e("ServerHelper", "REQUEST OK: " + response);
        }, error -> {
            Log.e("ServerHelper", "REQUEST ERROR: " + error);
            try {
                JSONArray json = singleton.readData();
                json.put(finalText);
                singleton.saveData(json);
            }catch (Exception e){
                Log.d(singleton.TAG, "Requests saved error", e);
            }
        });
        queue.add(stringRequest);
    }

    public static void getRequestSavedData(){
        InternalStorage singleton = InternalStorage.INSTANCE;

        try {
            while(true) {
                JSONArray json = singleton.readData();
                while(json.length() != 0){
                    String firstText = json.getString(json.length() - 1);
                    json.remove(json.length() - 1);
                    singleton.saveData(json);
                    getRequest(firstText);
                }
                json = singleton.readData();
                if(json.length() == 0){
                    break;
                }
            }
        }catch (Exception e){
            Log.d(singleton.TAG, "Requests saved error", e);
        }
    }
}

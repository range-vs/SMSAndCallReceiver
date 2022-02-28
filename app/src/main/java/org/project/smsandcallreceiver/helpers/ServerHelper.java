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

    public static void init(String chat_id){
        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                getRequestSavedData(chat_id);
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                //final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
            }
        };
        ConnectivityManager connectivityManager = App.getInstance().getSystemService(ConnectivityManager.class);
        connectivityManager.requestNetwork(networkRequest, networkCallback);
    }

    public static void getRequest(String chat_id, String text) {

        String url = endPoint + "chat_id=" + chat_id + "&text=" + text;
        RequestQueue queue = Volley.newRequestQueue(App.getInstance());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Log.e("ServerHelper", "REQUEST OK");
        }, error -> {
            Log.e("ServerHelper", "REQUEST ERROR");
            try {
                JSONArray json = InternalStorage.readData();
                json.put(text);
                InternalStorage.saveData(json);
            }catch (Exception e){
                Log.d(InternalStorage.TAG, "Requests saved error", e);
            }
        });
        queue.add(stringRequest);
    }

    public static void getRequestSavedData(String chat_id){
        try {
            while(true) {
                JSONArray json = InternalStorage.readData();
                while(json.length() != 0){
                    String firstText = json.getString(json.length() - 1);
                    json.remove(json.length() - 1);
                    InternalStorage.saveData(json);
                    getRequest(chat_id, firstText);
                }
                json = InternalStorage.readData();
                if(json.length() == 0){
                    break;
                }
            }
        }catch (Exception e){
            Log.d(InternalStorage.TAG, "Requests saved error", e);
        }
    }
}

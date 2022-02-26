package org.project.smsandcallreceiver.helpers;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.project.smsandcallreceiver.App;
import org.project.smsandcallreceiver.R;

public class ServerHelper {

    private static String telegramBotID = App.getInstance().getString(R.string.telegram_bot_id);
    private static String endPoint = "https://api.telegram.org/bot" + telegramBotID + "/sendMessage?";

    public static void getRequest(String chat_id, String text) {

        String url = endPoint + "chat_id=" + chat_id + "&text=" + text;
        RequestQueue queue = Volley.newRequestQueue(App.getInstance());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.e("ServerHelper", "REQUEST OK");
                }, error -> {
                    Log.e("ServerHelper", "REQUEST ERROR");
                }
        );
        queue.add(stringRequest);
    }
}

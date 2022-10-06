package com.sprtech.quickbite.FCM;

import android.content.Context;
import android.os.StrictMode;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMNotificationSender {

    private static final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private static final String fcmServerKey = "key=AAAAfw1e_TQ:APA91bG-WGICDXZaJ2X-_-_Y2RGpB556-9ocIRcZ0Ad3BIgub3FBF2wYCVAWnzlUYlujn2kQbd9vEw-9CasP_3c0eZnjHCtmjtEmlwqAjs9LTWYg6NkVYgjMZkSNQYIAN5VWsTAN-AMx";

    public static void sendNotification(Context context, String token, String title, String body) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", token);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            mainObj.put("notification", notiObject);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    postUrl,
                    mainObj,
                    response -> {

                    }, error -> {

                    }){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> map = new HashMap<>();
                    map.put("Content-Type", "application/json");
                    map.put("Authorization", fcmServerKey);
                    return map;
                }
            };

            requestQueue.add(jsonObjectRequest);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

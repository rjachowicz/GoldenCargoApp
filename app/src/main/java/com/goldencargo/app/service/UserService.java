package com.goldencargo.app.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final String TAG = "UserService";
    private static final String BASE_URL = "http://10.0.2.2:8080/api/users/";
    private static final String PREF_NAME = "AuthPreferences";
    private static final String JWT_KEY = "jwt_token";

    private final Context context;
    private final SharedPreferences sharedPreferences;

    public UserService(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void sendFcmTokenToBackend(String fcmToken,
                                      ResponseListener<JSONObject> onSuccess,
                                      ErrorListener onError) {

        String url = BASE_URL + "registerToken";
        Log.d(TAG, "sendFcmTokenToBackend -> " + url);

        JSONObject body = new JSONObject();
        try {
            body.put("fcmToken", fcmToken);
        } catch (JSONException e) {
            Log.e(TAG, "JSON error: " + e.getMessage());
            if (onError != null) {
                onError.onError(e);
            }
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                response -> {
                    if (onSuccess != null) {
                        onSuccess.onResponse(response);
                    }
                },
                error -> {
                    if (onError != null) {
                        onError.onError(error);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                String token = sharedPreferences.getString(JWT_KEY, null);
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    public interface ResponseListener<T> {
        void onResponse(T response);
    }

    public interface ErrorListener {
        void onError(Exception error);
    }
}

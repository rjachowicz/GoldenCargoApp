package com.goldencargo.app.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static final String TAG = "AuthService";
    private static final String PREF_NAME = "AuthPreferences";
    private static final String JWT_KEY = "jwt_token";
    private final RequestQueue queue;
    private final String apiUrl;
    private final SharedPreferences sharedPreferences;

    public AuthService(Context context, String apiUrl) {
        this.queue = Volley.newRequestQueue(context);
        this.apiUrl = apiUrl;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void loginUser(String userName, String password,
                          Response.Listener<JSONObject> successListener,
                          Response.ErrorListener errorListener) {
        JSONObject loginData = new JSONObject();
        try {
            loginData.put("userName", userName);
            loginData.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating login data", e);
            return;
        }

        sendPostRequest(loginData, response -> {
            try {
                // Zakładamy, że token znajduje się w odpowiedzi pod kluczem "token"
                String token = response.getString("token");
                saveToken(token);
                successListener.onResponse(response);
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing token from response", e);
            }
        }, errorListener);
    }

    private void sendPostRequest(JSONObject data,
                                 Response.Listener<JSONObject> successListener,
                                 Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AuthService.this.apiUrl + "/auth/login",
                data,
                successListener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(request);
    }

    private void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(JWT_KEY, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(JWT_KEY, null);
    }

    public void logoutUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(JWT_KEY);
        editor.apply();
    }
}

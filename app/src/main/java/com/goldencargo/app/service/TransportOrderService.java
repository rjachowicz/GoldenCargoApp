package com.goldencargo.app.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class TransportOrderService {

    private static final String API_URL = "http://10.0.2.2:8080/api";
    private static final String TRANSPORT_ORDERS = "/transport-orders/all";
    private static final String TRANSPORT_ORDERS_TODO = "/transport-orders/todo";
    private static final String TAG = "TransportOrderService";
    private static final String PREF_NAME = "AuthPreferences";
    private static final String JWT_KEY = "jwt_token";

    private final RequestQueue queue;
    private final SharedPreferences sharedPreferences;

    public TransportOrderService(Context context) {
        this.queue = Volley.newRequestQueue(context);
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void fetchTransportOrders(Response.Listener<JSONArray> onSuccess, Response.ErrorListener onError) {
        Log.d(TAG, "Sending request to API: " + API_URL + TRANSPORT_ORDERS);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                API_URL + TRANSPORT_ORDERS,
                null,
                onSuccess,
                onError
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

        queue.add(jsonArrayRequest);
    }

    public void fetchTransportOrdersToDo(Response.Listener<JSONArray> onSuccess, Response.ErrorListener onError) {
        Log.d(TAG, "Sending request to API: " + API_URL + TRANSPORT_ORDERS_TODO);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                API_URL + TRANSPORT_ORDERS_TODO,
                null,
                onSuccess,
                onError
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

        queue.add(jsonArrayRequest);
    }
}

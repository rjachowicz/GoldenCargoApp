package com.goldencargo.app.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TransportOrderService {

    private static final String API_URL = "http://10.0.2.2:8080/api";
    private static final String TRANSPORT_ORDERS = "/transport-orders/all";
    private static final String TRANSPORT_ORDERS_TODO = "/transport-orders/todo";
    private static final String TRANSPORT_ORDERS_PENDING = "/transport-orders/pending";
    private static final String START_NEW_TRANSPORT = "/transport-orders/start-transport";
    private static final String END_TRANSPORT = "/transport-orders/end-transport";
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

    public void fetchTransportOrdersToDo(Response.Listener<JSONObject> onSuccess,
                                         Response.ErrorListener onError) {
        Log.d(TAG, "Sending request to API: " + API_URL + TRANSPORT_ORDERS_TODO);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
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
        jsonObjectRequest.setShouldCache(false);
        queue.add(jsonObjectRequest);
    }

    public void fetchTransportOrdersPending(Response.Listener<JSONObject> onSuccess,
                                            Response.ErrorListener onError) {
        Log.d(TAG, "Sending request to API: " + API_URL + TRANSPORT_ORDERS_PENDING);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_URL + TRANSPORT_ORDERS_PENDING,
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
        jsonObjectRequest.setShouldCache(false);
        queue.add(jsonObjectRequest);
    }


    public void startTransport(Long transportOrderId,
                               String date,
                               Response.Listener<JSONObject> onSuccess,
                               Response.ErrorListener onError) {
        Log.d(TAG, "Starting transport for order ID: " + transportOrderId);

        String url = API_URL + START_NEW_TRANSPORT;

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("transportOrderId", transportOrderId);
            jsonBody.put("date", date);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonBody,
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

            queue.add(request);
        } catch (Exception e) {
            Log.e(TAG, "JSON Exception: " + e.getMessage());
        }
    }

    public void endTransport(Long transportOrderId,
                             String date,
                             Response.Listener<JSONObject> onSuccess,
                             Response.ErrorListener onError) {
        Log.d(TAG, "Ending transport for order ID: " + transportOrderId);
        String url = API_URL + END_TRANSPORT;
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("transportOrderId", transportOrderId);
            jsonBody.put("date", date);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonBody,
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

            queue.add(request);
        } catch (Exception e) {
            Log.e(TAG, "JSON Exception: " + e.getMessage());
        }
    }

}

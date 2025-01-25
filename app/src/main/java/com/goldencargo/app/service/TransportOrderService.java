package com.goldencargo.app.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransportOrderService {

    private static final String API_URL = "http://10.0.2.2:8080/api";
    private static final String TRANSPORT_ORDERS = "/transport-orders/get";
    private static final String TAG = "TransportOrderService";

    private final RequestQueue queue;

    public TransportOrderService(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    public void fetchTransportOrders(Response.Listener<JSONArray> onSuccess, Response.ErrorListener onError) {
        Log.d(TAG, "Sending request to API: " + API_URL + TRANSPORT_ORDERS);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                API_URL + TRANSPORT_ORDERS,
                null,
                onSuccess,
                onError
        );

        queue.add(jsonArrayRequest);
    }

    public List<Map<String, String>> parseTransportOrders(JSONArray jsonArray) throws Exception {
        List<Map<String, String>> dataList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("transportOrderId", "ID: " + obj.getString("transportOrderId"));
            dataMap.put("name", "Name: " + obj.getString("name"));
            dataMap.put("driverName", "Driver: " + obj.getString("driverName"));
            dataMap.put("status", "Status: " + obj.getString("status"));

            dataList.add(dataMap);
        }

        return dataList;
    }
}

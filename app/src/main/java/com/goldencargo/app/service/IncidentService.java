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

public class IncidentService {

    private static final String API_URL = "http://10.0.2.2:8080/api/incidents";
    private static final String TAG = "IncidentService";
    private static final String PREF_NAME = "AuthPreferences";
    private static final String JWT_KEY = "jwt_token";

    private final RequestQueue queue;
    private final SharedPreferences sharedPreferences;

    public IncidentService(Context context) {
        this.queue = Volley.newRequestQueue(context);
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void fetchMyIncidents(Response.Listener<JSONArray> onSuccess,
                                 Response.ErrorListener onError) {
        String url = API_URL + "/incidents";
        Log.d(TAG, "Sending GET request to: " + url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                onSuccess,
                onError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return buildHeaders();
            }
        };

        queue.add(request);
    }

    public void createIncident(String incidentType,
                               String description,
                               long vehicleId,
                               Response.Listener<JSONObject> onSuccess,
                               Response.ErrorListener onError) {
        String url = API_URL + "/create";
        Log.d(TAG, "Sending POST request to: " + url);

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("incidentType", incidentType);
            jsonBody.put("description", description);
            JSONObject vehicleObj = new JSONObject();
            vehicleObj.put("vehicleId", vehicleId);
            jsonBody.put("vehicle", vehicleObj);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonBody,
                    onSuccess,
                    onError
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    return buildHeaders();
                }
            };

            queue.add(request);
        } catch (Exception e) {
            Log.e(TAG, "JSON Exception: " + e.getMessage());
        }
    }


    public void fetchVehiclesForUser(Response.Listener<JSONArray> onSuccess,
                                     Response.ErrorListener onError) {
        String url = API_URL + "/vehicles";
        Log.d(TAG, "Sending GET request to: " + url);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                onSuccess,
                onError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return buildHeaders();
            }
        };
        queue.add(request);
    }


    private Map<String, String> buildHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String token = sharedPreferences.getString(JWT_KEY, null);
        if (token != null) {
            headers.put("Authorization", "Bearer " + token);
        }

        return headers;
    }
}

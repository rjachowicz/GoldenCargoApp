package com.goldencargo.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldencargo.app.adapters.TransportOrderAdapter;
import com.goldencargo.app.service.TransportOrderService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TransportOrdersActivity extends AppCompatActivity {

    private static final String TAG = "TransportOrdersActivity";
    private TransportOrderAdapter adapterCompleted, adapterNew, adapterPending;
    private TransportOrderService transportOrderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_orders);

        transportOrderService = new TransportOrderService(this);

        RecyclerView recyclerCompleted = findViewById(R.id.recycler_completed);

        recyclerCompleted.setLayoutManager(new LinearLayoutManager(this));

        adapterCompleted = new TransportOrderAdapter(new ArrayList<>());
        adapterNew = new TransportOrderAdapter(new ArrayList<>());
        adapterPending = new TransportOrderAdapter(new ArrayList<>());

        recyclerCompleted.setAdapter(adapterCompleted);
        fetchTransportOrders();
    }

    private void fetchTransportOrders() {
        Log.d(TAG, "Started fetching data from API.");

        transportOrderService.fetchTransportOrders(
                response -> {
                    Log.i(TAG, "Data fetched successfully: " + response.toString());
                    displayTransportOrders(response);
                },
                error -> {
                    Log.e(TAG, "Error while fetching data: " + error.getMessage());
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );
    }

    private void displayTransportOrders(JSONArray jsonArray) {
        List<JSONObject> completedList = new ArrayList<>();
        List<JSONObject> newList = new ArrayList<>();
        List<JSONObject> pendingList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String status = obj.getString("status");

                switch (status) {
                    case "COMPLETED":
                        completedList.add(obj);
                        break;
                    case "NEW":
                        newList.add(obj);
                        break;
                    case "PENDING":
                        pendingList.add(obj);
                        break;
                }
            }

            adapterCompleted.updateData(completedList);
            adapterNew.updateData(newList);
            adapterPending.updateData(pendingList);

        } catch (Exception e) {
            Log.e(TAG, "Error parsing transport orders", e);
        }
    }
}

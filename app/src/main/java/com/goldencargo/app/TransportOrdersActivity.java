package com.goldencargo.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.goldencargo.app.service.TransportOrderService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransportOrdersActivity extends AppCompatActivity {

    private static final String TAG = "TransportOrdersActivity";
    private ListView listView;
    private TransportOrderService transportOrderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_orders);

        listView = findViewById(R.id.list_view_driver_reports);
        transportOrderService = new TransportOrderService(this);

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
        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                Map<String, String> dataMap = new HashMap<>();
                dataMap.put("name", obj.getString("name"));
                dataMap.put("driver", "Driver: " + obj.getString("driverName") + " (" + obj.getString("driverLicenseNumber") + ")");
                dataMap.put("vehicle", "Vehicle: " + obj.getString("vehicleDetails"));
                dataMap.put("route", "Route: " + obj.getString("startLocation") + " -> " + obj.getString("endLocation"));
                dataMap.put("status", "Status: " + obj.getString("status"));

                dataList.add(dataMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing data", Toast.LENGTH_LONG).show();
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                dataList,
                R.layout.list_item_transport_order,
                new String[]{"name", "driver", "vehicle", "route", "status"},
                new int[]{R.id.item_name, R.id.item_driver, R.id.item_vehicle, R.id.item_route, R.id.item_status}
        );

        listView.setAdapter(adapter);
    }

}

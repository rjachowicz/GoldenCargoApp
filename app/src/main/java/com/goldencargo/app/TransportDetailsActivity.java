package com.goldencargo.app;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;

public class TransportDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private com.goldencargo.app.DetailsAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_details);

        recyclerView = findViewById(R.id.recyclerViewDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String jsonString = getIntent().getStringExtra("transportData");
        if (jsonString != null) {
            try {
                JSONObject data = new JSONObject(jsonString);
                List<Map.Entry<String, String>> dataList = new ArrayList<>();
                Iterator<String> keys = data.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = data.optString(key, "N/A");
                    dataList.add(new AbstractMap.SimpleEntry<>(formatKey(key), value));
                }

                adapter = new com.goldencargo.app.DetailsAdapter(dataList);
                recyclerView.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String formatKey(String key) {
        return key.replaceAll("([a-z])([A-Z])", "$1 $2")
                .replace("_", " ")
                .toUpperCase();
    }
}

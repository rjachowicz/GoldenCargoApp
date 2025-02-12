package com.goldencargo.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldencargo.app.adapters.IncidentAdapter;
import com.goldencargo.app.data.IncidentItem;
import com.goldencargo.app.service.IncidentService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IncidentActivity extends AppCompatActivity {

    private static final String TAG = "IncidentActivity";
    private IncidentService incidentService;
    private IncidentAdapter incidentAdapter;
    private final List<IncidentItem> incidentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident);

        incidentService = new IncidentService(this);

        RecyclerView recyclerIncidents = findViewById(R.id.recyclerIncidents);
        recyclerIncidents.setLayoutManager(new LinearLayoutManager(this));

        incidentAdapter = new IncidentAdapter(incidentList);
        recyclerIncidents.setAdapter(incidentAdapter);

        findViewById(R.id.btnCreateIncident).setOnClickListener(view -> {
            Intent intent = new Intent(IncidentActivity.this, CreateIncidentActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchIncidents();
    }

    private void fetchIncidents() {
        incidentService.fetchMyIncidents(response -> {
            Log.d(TAG, "Incidents response: " + response.toString());
            parseIncidentData(response);
        }, error -> {
            Log.e(TAG, "Error fetching incidents: " + error.getMessage());
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void parseIncidentData(JSONArray jsonArray) {
        try {
            incidentList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                long incidentId = obj.optLong("incidentId", -1);
                String incidentType = obj.optString("incidentType", "N/A");
                String date = obj.optString("date", "");
                String description = obj.optString("description", "");

                incidentList.add(new IncidentItem(incidentId, incidentType, date, description));
            }
            incidentAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, "Error parsing incident data: " + e.getMessage());
        }
    }
}

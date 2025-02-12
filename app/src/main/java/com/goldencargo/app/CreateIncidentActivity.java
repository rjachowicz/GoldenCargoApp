package com.goldencargo.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.goldencargo.app.data.VehicleItem;
import com.goldencargo.app.service.IncidentService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateIncidentActivity extends AppCompatActivity {

    private static final String TAG = "CreateIncidentActivity";
    private IncidentService incidentService;

    private Spinner spinnerVehicles;
    private EditText etIncidentType, etIncidentDescription;

    private final List<VehicleItem> vehicleList = new ArrayList<>();
    private long selectedVehicleId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_incident);

        spinnerVehicles = findViewById(R.id.spinnerVehicles);
        etIncidentType = findViewById(R.id.etIncidentType);
        etIncidentDescription = findViewById(R.id.etIncidentDescription);
        Button btnCreateIncident = findViewById(R.id.btnCreateIncident);

        incidentService = new IncidentService(this);

        btnCreateIncident.setOnClickListener(this::createIncident);

        spinnerVehicles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VehicleItem selected = vehicleList.get(position);
                selectedVehicleId = selected.getVehicleId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedVehicleId = -1;
            }
        });

        fetchVehicles();
    }

    private void fetchVehicles() {
        incidentService.fetchVehiclesForUser(response -> {
            Log.d(TAG, "Vehicles response: " + response.toString());
            parseVehicles(response);
        }, error -> {
            Log.e(TAG, "Error fetching vehicles: " + error.getMessage());
            Toast.makeText(this, "Error fetching vehicles", Toast.LENGTH_SHORT).show();
        });
    }

    private void parseVehicles(JSONArray jsonArray) {
        try {
            vehicleList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                long vId = obj.optLong("vehicleId", -1);
                String regNumber = obj.optString("registrationNumber", "Unknown");
                vehicleList.add(new VehicleItem(vId, regNumber));
            }
            ArrayAdapter<VehicleItem> adapter =
                    new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerVehicles.setAdapter(adapter);

        } catch (Exception e) {
            Log.e(TAG, "Error parsing vehicles: " + e.getMessage());
        }
    }

    private void createIncident(View view) {
        String incidentType = etIncidentType.getText().toString().trim();
        String description = etIncidentDescription.getText().toString().trim();

        if (selectedVehicleId == -1) {
            Toast.makeText(this, "Please select a vehicle", Toast.LENGTH_SHORT).show();
            return;
        }
        if (incidentType.isEmpty()) {
            etIncidentType.setError("Please enter incident type");
            return;
        }
        if (description.isEmpty()) {
            etIncidentDescription.setError("Please enter description");
            return;
        }

        incidentService.createIncident(
                incidentType,
                description,
                selectedVehicleId,
                response -> {
                    Log.d(TAG, "Incident created: " + response.toString());
                    finish();
                },
                error -> {
                    Log.e(TAG, "Error creating incident: " + "t");
                    Toast.makeText(this, "Error creating incident", Toast.LENGTH_SHORT).show();
                }
        );
    }
}

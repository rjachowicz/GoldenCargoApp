package com.goldencargo.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.VolleyError;
import com.goldencargo.app.service.TransportOrderService;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private CardView cardTransport;
    private TextView tvShortInfo;
    private Button btnDetails;
    private Button btnStart;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardTransport = findViewById(R.id.cardTransport);
        tvShortInfo = findViewById(R.id.tvShortInfo);
        btnDetails = findViewById(R.id.btnDetails);
        btnStart = findViewById(R.id.btnStart);
        cardTransport.setVisibility(View.GONE);

        TransportOrderService transportOrderService = new TransportOrderService(this);
        Log.d(TAG, "Fetching transport order data...");
        transportOrderService.fetchTransportOrdersToDo(
                response -> {
                    Log.d(TAG, "Response received: " + response.toString());
                    parseAndDisplayData(response);
                },
                (VolleyError error) -> {
                    Log.e(TAG, "Error fetching data: " + error.getMessage());
                    Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
        );

        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(this::showDropdownMenu);
    }

    private void parseAndDisplayData(JSONObject response) {
        try {
            int transportId = response.optInt("transportId", -1);
            String driverName = response.optString("driverName", "No driver");
            String createdAt = response.optString("transportCreatedAt", "N/A");

            String shortText = "Transport ID: " + transportId + "\n" +
                    "Driver: " + driverName + "\n" +
                    "Created at: " + createdAt;
            tvShortInfo.setText(shortText);

            cardTransport.setVisibility(View.VISIBLE);

            btnDetails.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, TransportDetailsActivity.class);
                intent.putExtra("transportData", response.toString());
                startActivity(intent);
            });

            btnStart.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, "Start clicked. (Status -> PENDING)", Toast.LENGTH_SHORT).show();
                // wywołanie transportOrderService.updateStatus(...)
            });

        } catch (Exception e) {
            Log.e(TAG, "Error parsing data: " + e.getMessage());
            Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
        }
    }
    private void showDropdownMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_logistics) {
                Toast.makeText(this, "Logistics clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_routes) {
                Toast.makeText(this, "Routes clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_locations) {
                Toast.makeText(this, "Locations clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_transports) {
                Toast.makeText(this, "Transports clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_transport_orders) {
                Intent intent = new Intent(MainActivity.this, TransportOrdersActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }
}

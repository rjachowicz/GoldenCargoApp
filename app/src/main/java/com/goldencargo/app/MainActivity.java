package com.goldencargo.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean isMenuVisible = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button buttonTransportOrders = findViewById(R.id.button_transport_orders);
        ImageButton menuButton = findViewById(R.id.menuButton);
        RelativeLayout menuContainer = findViewById(R.id.menuContainer);

        Button buttonOption1 = findViewById(R.id.buttonOption1);
        Button buttonOption2 = findViewById(R.id.buttonOption2);
        Button buttonOption3 = findViewById(R.id.buttonOption3);

        menuButton.setOnClickListener(v -> {
            if (isMenuVisible) {
                menuContainer.setVisibility(View.GONE);
            } else {
                menuContainer.setVisibility(View.VISIBLE);
            }
            isMenuVisible = !isMenuVisible;
        });

        buttonOption1.setOnClickListener(v -> {
        });

        buttonOption2.setOnClickListener(v -> {
        });

        buttonOption3.setOnClickListener(v -> {
            // Logika dla "Option 3"
        });
        buttonTransportOrders.setOnClickListener(v -> {
        });
    }
}

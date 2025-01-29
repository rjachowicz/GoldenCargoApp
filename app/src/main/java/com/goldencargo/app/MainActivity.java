package com.goldencargo.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton menuButton = findViewById(R.id.menuButton);

        menuButton.setOnClickListener(v -> showDropdownMenu(v));
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

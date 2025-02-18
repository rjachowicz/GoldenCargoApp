package com.goldencargo.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.goldencargo.app.service.TransportOrderService;
import com.goldencargo.app.service.UserService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private CardView cardTransportTodo;
    private TextView tvTodoInfo;
    private Button btnTodoDetails;
    private Button btnTodoStart;

    private CardView cardTransportPending;
    private TextView tvPendingInfo;
    private Button btnPendingDetails;
    private Button btnPendingFinish;

    private TransportOrderService transportOrderService;
    private UserService userService;

    private static final int REQUEST_NOTIFICATION_PERMISSION = 100;

    @SuppressLint({"SetTextI18n", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);

        cardTransportTodo = findViewById(R.id.cardTransportTodo);
        tvTodoInfo = findViewById(R.id.tvTodoInfo);
        btnTodoDetails = findViewById(R.id.btnTodoDetails);
        btnTodoStart = findViewById(R.id.btnTodoStart);

        cardTransportPending = findViewById(R.id.cardTransportPending);
        tvPendingInfo = findViewById(R.id.tvPendingInfo);
        btnPendingDetails = findViewById(R.id.btnPendingDetails);
        btnPendingFinish = findViewById(R.id.btnPendingFinish);

        transportOrderService = new TransportOrderService(this);
        userService = new UserService(this);

        fetchAndSendFcmToken();

        hideCards();
        fetchData();

        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(this::showDropdownMenu);

        Button btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(v -> {
            hideCards();
            fetchData();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION)
                ;
            }
        }
    }

    private void fetchAndSendFcmToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String newFcmToken = task.getResult();
                    Log.d(TAG, "New FCM token: " + newFcmToken);

                    userService.sendFcmTokenToBackend(newFcmToken,
                            response -> Log.d(TAG, "Token saved on backend: " + response.toString()),
                            error -> Log.e(TAG, "Failed to save token: " + error.getMessage()));
                });
    }

    private void hideCards() {
        cardTransportTodo.setVisibility(View.GONE);
        cardTransportPending.setVisibility(View.GONE);
    }

    private void fetchData() {
        fetchTodoOrders();
        fetchPendingOrders();
    }

    @SuppressLint("SetTextI18n")
    private void fetchTodoOrders() {
        Log.d(TAG, "Fetching todo transport order data...");
        transportOrderService.fetchTransportOrdersToDo(
                response -> {
                    Log.d(TAG, "Todo Response: " + response.toString());
                    parseAndDisplayTodo(response);
                },
                error -> Log.e(TAG, "Error fetching todo orders: " + error.getMessage())
        );
    }

    @SuppressLint("SetTextI18n")
    private void fetchPendingOrders() {
        Log.d(TAG, "Fetching pending transport order data...");
        transportOrderService.fetchTransportOrdersPending(
                response -> {
                    Log.d(TAG, "Pending Response: " + response.toString());
                    parseAndDisplayPending(response);
                },
                error -> Log.e(TAG, "Error fetching pending orders: " + error.getMessage())
        );
    }

    private void parseAndDisplayTodo(JSONObject response) {
        try {
            int transportOrderId = response.optInt("transportOrderId", -1);
            String driverName = response.optString("driverName", "No driver");
            String createdAt = response.optString("transportCreatedAt", "N/A");

            String shortText = "Transport Order ID: " + transportOrderId + "\n" +
                    "Driver: " + driverName + "\n" +
                    "Created at: " + createdAt;
            tvTodoInfo.setText(shortText);
            cardTransportTodo.setVisibility(View.VISIBLE);

            btnTodoDetails.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, TransportDetailsActivity.class);
                intent.putExtra("transportData", response.toString());
                startActivity(intent);
            });

            btnTodoStart.setOnClickListener(v -> {
                if (transportOrderId == -1) {
                    Toast.makeText(MainActivity.this,
                            "No transport order available",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",
                        Locale.getDefault()).format(new Date());
                transportOrderService.startTransport((long) transportOrderId, date,
                        resp -> {
                            Toast.makeText(MainActivity.this,
                                    "Transport started successfully",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Transport started: " + resp.toString());
                            hideCards();
                            fetchData();
                        },
                        err -> {
                            Toast.makeText(MainActivity.this,
                                    "Failed to start transport",
                                    Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Error starting transport: " + err.getMessage());
                        });
            });

        } catch (Exception e) {
            Log.e(TAG, "Error parsing todo data: " + e.getMessage());
            Toast.makeText(MainActivity.this,
                    "Error parsing todo data",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void parseAndDisplayPending(JSONObject response) {
        try {
            int transportOrderId = response.optInt("transportOrderId", -1);
            String driverName = response.optString("driverName", "No driver");
            String createdAt = response.optString("transportCreatedAt", "N/A");

            String shortText = "Transport ID: " + transportOrderId + "\n" +
                    "Driver: " + driverName + "\n" +
                    "Created at: " + createdAt;
            tvPendingInfo.setText(shortText);
            cardTransportPending.setVisibility(View.VISIBLE);

            btnPendingDetails.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, TransportDetailsActivity.class);
                intent.putExtra("transportData", response.toString());
                startActivity(intent);
            });

            btnPendingFinish.setOnClickListener(v -> {
                if (transportOrderId == -1) {
                    Toast.makeText(MainActivity.this,
                            "No transport order available",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",
                        Locale.getDefault()).format(new Date());
                transportOrderService.endTransport((long) transportOrderId, date,
                        resp -> {
                            Toast.makeText(MainActivity.this,
                                    "Transport ended successfully",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Transport ended: " + resp.toString());
                            hideCards();
                            fetchData();
                        },
                        err -> {
                            Toast.makeText(MainActivity.this,
                                    "Failed to end transport",
                                    Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Error ending transport: " + err.getMessage());
                        });
            });

        } catch (Exception e) {
            Log.e(TAG, "Error parsing pending data: " + e.getMessage());
            Toast.makeText(MainActivity.this, "Error parsing pending data", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDropdownMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_reports) {
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intent);
                return true;
            } else if (id == R.id.menu_incidents) {
                Intent intent = new Intent(MainActivity.this, IncidentActivity.class);
                startActivity(intent);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Notifications", "Notification permission access");
            } else {
                Log.d("Notifications", "Notification permission not access");
            }
        }
    }
}

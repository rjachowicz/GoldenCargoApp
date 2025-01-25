package com.goldencargo.app;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.goldencargo.app.service.AuthService;

public class LoginActivity extends AppCompatActivity {

    private EditText userNameEditText, passwordEditText;
    private Button loginButton;
    private TextView errorMessageTextView;
    private AuthService authService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameEditText = findViewById(R.id.editTextUserName);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        errorMessageTextView = findViewById(R.id.textViewErrorMessage);

        authService = new AuthService(this, "http://10.0.2.2:8080/api");

        loginButton.setOnClickListener(v -> loginUser());
    }

    @SuppressLint("SetTextI18n")
    private void loginUser() {
        String userName = userNameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (userName.isEmpty() || password.isEmpty()) {
            errorMessageTextView.setText("Please fill in all fields");
            errorMessageTextView.setVisibility(TextView.VISIBLE);
            return;
        }

        errorMessageTextView.setVisibility(TextView.GONE);

        authService.loginUser(userName, password, response -> {
            try {
                boolean success = response.getBoolean("success");
                if (success) {
                    String token = response.getString("token");
                    Log.i(TAG, "Login successful. Token: " + token);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("auth_token", token);
                    startActivity(intent);
                    finish();
                } else {
                    String message = response.optString("message", "Invalid credentials");
                    Log.w(TAG, "Login failed: " + message);
                    errorMessageTextView.setText(message);
                    errorMessageTextView.setVisibility(TextView.VISIBLE);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error parsing response", e);
                errorMessageTextView.setText("Error parsing response");
                errorMessageTextView.setVisibility(TextView.VISIBLE);
            }
        }, error -> {
            Log.e(TAG, "Login request failed", error);
            errorMessageTextView.setText("Failed to connect to the server. Please try again later.");
            errorMessageTextView.setVisibility(TextView.VISIBLE);
        });
    }
}

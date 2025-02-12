package com.goldencargo.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.goldencargo.app.service.ReportService;

public class CreateReportActivity extends AppCompatActivity {

    private static final String TAG = "CreateReportActivity";
    private ReportService reportService;
    private EditText etReportType, etReportContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        etReportType = findViewById(R.id.etReportType);
        etReportContent = findViewById(R.id.etReportContent);
        Button btnCreate = findViewById(R.id.btnCreate);

        reportService = new ReportService(this);

        btnCreate.setOnClickListener(this::createReport);
    }

    private void createReport(View view) {
        String reportType = etReportType.getText().toString().trim();
        String content = etReportContent.getText().toString().trim();

        if (reportType.isEmpty()) {
            etReportType.setError("Please enter report type");
            return;
        }
        if (content.isEmpty()) {
            etReportContent.setError("Please enter content");
            return;
        }

        reportService.createReport(reportType, content,
                response -> {
                    Log.d(TAG, "Report created: " + response.toString());
                    finish();
                },
                error -> {
                    Log.e(TAG, "Error creating report: " + error.getMessage());
                }
        );
    }
}

package com.goldencargo.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.goldencargo.app.data.ReportItem;
import com.goldencargo.app.service.ReportService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private static final String TAG = "ReportActivity";
    private ReportService reportService;
    private com.goldencargo.app.ReportAdapter reportAdapter;
    private final List<ReportItem> reportList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportService = new ReportService(this);

        RecyclerView recyclerReports = findViewById(R.id.recyclerReports);
        recyclerReports.setLayoutManager(new LinearLayoutManager(this));

        reportAdapter = new com.goldencargo.app.ReportAdapter(reportList);
        recyclerReports.setAdapter(reportAdapter);

        findViewById(R.id.btnCreateReport).setOnClickListener(view -> {
            Intent intent = new Intent(ReportActivity.this, CreateReportActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchReports();
    }

    private void fetchReports() {
        reportService.fetchReports(response -> {
            Log.d(TAG, "Reports response: " + response.toString());
            parseReportData(response);

        }, error -> {
            Log.e(TAG, "Error fetching reports: " + error.getMessage());
        });
    }

    private void parseReportData(JSONArray jsonArray) {
        try {
            reportList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                long reportId = obj.optLong("reportId", -1);
                String reportType = obj.optString("reportType", "N/A");
                String content = obj.optString("content", "");

                reportList.add(new ReportItem(reportId, reportType, content));
            }
            reportAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, "Error parsing report data: " + e.getMessage());
        }
    }
}

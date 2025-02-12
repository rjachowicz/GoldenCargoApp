package com.goldencargo.app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldencargo.app.data.ReportItem;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private final List<ReportItem> reportList;

    public ReportAdapter(List<ReportItem> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ViewHolder holder, int position) {
        ReportItem report = reportList.get(position);
        Log.d("ReportAdapter", "onBindViewHolder: " + report.getReportType() + ", " + report.getContent());
        holder.tvReportType.setText(report.getReportType());
        holder.tvReportContent.setText(report.getContent());
    }


    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReportType, tvReportContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReportType = itemView.findViewById(R.id.tvReportType);
            tvReportContent = itemView.findViewById(R.id.tvReportContent);
        }
    }
}

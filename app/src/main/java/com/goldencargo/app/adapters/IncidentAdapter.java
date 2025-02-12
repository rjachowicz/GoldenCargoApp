package com.goldencargo.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldencargo.app.R;
import com.goldencargo.app.data.IncidentItem;

import java.util.List;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.ViewHolder> {

    private final List<IncidentItem> incidentList;

    public IncidentAdapter(List<IncidentItem> incidentList) {
        this.incidentList = incidentList;
    }

    @NonNull
    @Override
    public IncidentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_incident, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentAdapter.ViewHolder holder, int position) {
        IncidentItem incident = incidentList.get(position);
        holder.tvIncidentType.setText(incident.getIncidentType());
        holder.tvIncidentDate.setText(incident.getDate());
        holder.tvIncidentDescription.setText(incident.getDescription());
    }

    @Override
    public int getItemCount() {
        return incidentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIncidentType, tvIncidentDate, tvIncidentDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIncidentType = itemView.findViewById(R.id.tvIncidentType);
            tvIncidentDate = itemView.findViewById(R.id.tvIncidentDate);
            tvIncidentDescription = itemView.findViewById(R.id.tvIncidentDescription);
        }
    }
}

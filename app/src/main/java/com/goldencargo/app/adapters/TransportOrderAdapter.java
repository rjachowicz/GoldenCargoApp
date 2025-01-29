package com.goldencargo.app.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goldencargo.app.R;

import org.json.JSONObject;

import java.util.List;

public class TransportOrderAdapter extends RecyclerView.Adapter<TransportOrderAdapter.ViewHolder> {

    private List<JSONObject> transportOrders;

    public TransportOrderAdapter(List<JSONObject> transportOrders) {
        this.transportOrders = transportOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transport_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject order = transportOrders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return transportOrders.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<JSONObject> newData) {
        this.transportOrders = newData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, driver, vehicle, route, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            driver = itemView.findViewById(R.id.item_driver);
            vehicle = itemView.findViewById(R.id.item_vehicle);
            route = itemView.findViewById(R.id.item_route);
            status = itemView.findViewById(R.id.item_status);
        }

        @SuppressLint("SetTextI18n")
        public void bind(JSONObject order) {
            name.setText(order.optString("name", "Unknown"));
            driver.setText("Driver: " + order.optString("driverName", "Unknown"));
            vehicle.setText("Vehicle: " + order.optString("vehicleDetails", "Unknown"));
            route.setText("Route: " + order.optString("startLocation", "Unknown") + " -> " + order.optString("endLocation", "Unknown"));
            status.setText("Status: " + order.optString("status", "Unknown"));
        }
    }
}

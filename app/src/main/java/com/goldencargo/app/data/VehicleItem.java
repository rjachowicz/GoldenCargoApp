package com.goldencargo.app.data;

import androidx.annotation.NonNull;

public class VehicleItem {
    private final long vehicleId;
    private final String registrationNumber;

    public VehicleItem(long vehicleId, String registrationNumber) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return registrationNumber;
    }
}

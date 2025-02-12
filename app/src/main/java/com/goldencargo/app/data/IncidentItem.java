package com.goldencargo.app.data;

public class IncidentItem {
    private final long incidentId;
    private final String incidentType;
    private final String date;
    private final String description;

    public IncidentItem(long incidentId, String incidentType, String date, String description) {
        this.incidentId = incidentId;
        this.incidentType = incidentType;
        this.date = date;
        this.description = description;
    }

    public long getIncidentId() {
        return incidentId;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}

package com.goldencargo.app.data;

public class ReportItem {
    private final long reportId;
    private final String reportType;
    private final String content;

    public ReportItem(long reportId, String reportType, String content) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.content = content;
    }

    public long getReportId() {
        return reportId;
    }

    public String getReportType() {
        return reportType;
    }

    public String getContent() {
        return content;
    }
}

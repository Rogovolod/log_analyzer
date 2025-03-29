package ru.farpost.loganalyzer.model;

public class LogEntry {

    private final String timestamp;
    private final int statusCode;
    private final double responseTime;

    public LogEntry(String timestamp, int statusCode, double responseTime) {
        this.timestamp = timestamp;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public double getResponseTime() {
        return responseTime;
    }

}

package ru.farpost.loganalyzer.model;

public class Interval {

    private String start;
    private String end;
    private double availability;

    public Interval(String start, String end, double availability) {
        this.start = start;
        this.end = end;
        this.availability = availability;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public double getAvailability() {
        return availability;
    }

}

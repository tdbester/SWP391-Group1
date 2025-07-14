package org.example.talentcenter.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Slot {
    private int id;
    private LocalTime startTime;
    private LocalTime endTime;

    // Constructors (giữ nguyên)
    public Slot() {}

    public Slot(int id, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters (giữ nguyên)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    // THÊM CÁC PHƯƠNG THỨC HELPER MỚI
    public String getFormattedStartTime() {
        return startTime != null ? startTime.format(DateTimeFormatter.ofPattern("HH:mm")) : "";
    }

    public String getFormattedEndTime() {
        return endTime != null ? endTime.format(DateTimeFormatter.ofPattern("HH:mm")) : "";
    }

    public String getTimeRange() {
        return getFormattedStartTime() + " - " + getFormattedEndTime();
    }

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

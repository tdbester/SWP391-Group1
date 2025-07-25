package org.example.talentcenter.model;

import java.time.LocalDate;

public class ClassSchedulePattern {
    private int id;
    private int classRoomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int slotId;
    private int dayOfWeek;

    public ClassSchedulePattern() {
    }

    public ClassSchedulePattern(int id, int dayOfWeek, int slotId, LocalDate endDate, LocalDate startDate, int classRoomId) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.slotId = slotId;
        this.endDate = endDate;
        this.startDate = startDate;
        this.classRoomId = classRoomId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(int classRoomId) {
        this.classRoomId = classRoomId;
    }
}

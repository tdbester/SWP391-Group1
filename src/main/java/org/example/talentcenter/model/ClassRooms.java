package org.example.talentcenter.model;

import java.time.LocalTime;

public class ClassRooms {
    private int id;
    private String name;
    private int courseId;
    private int teacherId;
    private int slotId;

    // Thông tin bổ sung
    private String courseTitle;
    private LocalTime startTime;
    private LocalTime endTime;
    private int studentCount;

    public ClassRooms() {
    }

    public ClassRooms(int id, int teacherId, int courseId, String name, int slotId) {
        this.id = id;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.name = name;
        this.slotId = slotId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

}
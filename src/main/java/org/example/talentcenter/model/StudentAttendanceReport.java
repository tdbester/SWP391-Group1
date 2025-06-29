package org.example.talentcenter.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class StudentAttendanceReport {
    private int id;
    private LocalDate date;
    private int slotId;
    private LocalTime slotStartTime;
    private LocalTime slotEndTime;
    private int roomId;
    private int classRoomId;
    private String className;
    private String courseTitle;
    private String roomCode;
    private int teacherId;
    private String teacherName;
    private int scheduleId;
    private String status;
    private String note;

    public StudentAttendanceReport() {
    }

    public StudentAttendanceReport(int id, int slotId, LocalTime slotStartTime, LocalTime slotEndTime, String className, String courseTitle, String teacherName, String status, String note, String roomCode) {
        this.id = id;
        this.slotId = slotId;
        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
        this.className = className;
        this.courseTitle = courseTitle;
        this.teacherName = teacherName;
        this.status = status;
        this.note = note;
        this.roomCode = roomCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public LocalTime getSlotStartTime() {
        return slotStartTime;
    }

    public void setSlotStartTime(LocalTime slotStartTime) {
        this.slotStartTime = slotStartTime;
    }

    public LocalTime getSlotEndTime() {
        return slotEndTime;
    }

    public void setSlotEndTime(LocalTime slotEndTime) {
        this.slotEndTime = slotEndTime;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(int classRoomId) {
        this.classRoomId = classRoomId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

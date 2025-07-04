package org.example.talentcenter.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class StudentSchedule {
    private int id;
    private LocalDate date;
    private int roomId;
    private int classRoomId;
    private String className;
    private String courseTitle;
    private String roomCode;
    private int teacherId;
    private String teacherName;
    private int slotId;
    // Derived from Slot table
    private LocalTime slotStartTime;
    private LocalTime slotEndTime;
    private String teacherEmail;
    private String teacherPhoneNumber;
    private String attendaceStatus;

    public StudentSchedule() {
    }

    public StudentSchedule(int id, LocalDate date, int roomId, int classRoomId, int slotId, LocalTime slotStartTime, LocalTime slotEndTime, String TeacherEmail, String TeacherPhoneNumber, String attendaceStatus) {
        this.id = id;
        this.date = date;
        this.roomId = roomId;
        this.classRoomId = classRoomId;
        this.slotId = slotId;
        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
        this.teacherEmail = TeacherEmail;
        this.teacherPhoneNumber = TeacherPhoneNumber;
        this.attendaceStatus = attendaceStatus;
    }

    // Getters and Setters
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

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getAttendaceStatus() {
        return attendaceStatus;
    }

    public void setAttendaceStatus(String attendaceStatus) {
        this.attendaceStatus = attendaceStatus;
    }

    public String getTeacherPhoneNumber() {
        return teacherPhoneNumber;
    }

    public void setTeacherPhoneNumber(String teacherPhoneNumber) {
        this.teacherPhoneNumber = teacherPhoneNumber;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", date=" + date +
                ", roomId=" + roomId +
                ", classRoomId=" + classRoomId +
                ", className='" + className + '\'' +
                ", courseTitle='" + courseTitle + '\'' +
                ", roomCode='" + roomCode + '\'' +
                ", teacherId=" + teacherId +
                ", teacherName='" + teacherName + '\'' +
                '}';
    }
}
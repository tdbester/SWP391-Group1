package org.example.talentcenter.model;

import java.util.Date;

public class Attendance {
    private int id;
    private int scheduleId;
    private int studentId;
    private String status; // "Present", "Absent", "Late"
    private String note;
    private String studentName;
    private String className;
    private String courseTitle;
    private Date date;

    public Attendance() {}

    public Attendance(int scheduleId, int studentId, String status, String note) {
        this.scheduleId = scheduleId;
        this.studentId = studentId;
        this.status = status;
        this.note = note;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    // Constants cho status
    public static final String PRESENT = "Present";
    public static final String ABSENT = "Absent";
    public static final String LATE = "Late";

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", scheduleId=" + scheduleId +
                ", studentId=" + studentId +
                ", status='" + status + '\'' +
                ", note='" + note + '\'' +
                ", studentName='" + studentName + '\'' +
                '}';
    }
}
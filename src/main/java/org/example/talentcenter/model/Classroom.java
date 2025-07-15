package org.example.talentcenter.model;

import java.util.ArrayList;

public class Classroom {
    private int classroomID;
    private String classroomName;
    private int courseId;
    private int teacherId;
    private int maxCapacity;
    private String teacherName;
    private int availableSeats;
    private ArrayList<StudentSchedule> schedules;

    public Classroom(int classroomID, String classroomName, int courseId, int teacherId) {
        this.classroomID = classroomID;
        this.classroomName = classroomName;
        this.courseId = courseId;
        this.teacherId = teacherId;
    }

    public Classroom(int classroomID, String classroomName) {
        this.classroomID = classroomID;
        this.classroomName = classroomName;
    }

    public Classroom() {
    }

    public int getClassroomID() {
        return classroomID;
    }

    public void setClassroomID(int classroomID) {
        this.classroomID = classroomID;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public ArrayList<StudentSchedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList<StudentSchedule> schedules) {
        this.schedules = schedules;
    }
}

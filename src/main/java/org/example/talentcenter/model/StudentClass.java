package org.example.talentcenter.model;

import java.util.Date;

public class StudentClass {
    private int id;
    private int classRoomId;
    private int studentId;
    private Date joinDate;

    public StudentClass() {}

    public StudentClass(int id, int classRoomId, int studentId, Date joinDate) {
        this.id = id;
        this.classRoomId = classRoomId;
        this.studentId = studentId;
        this.joinDate = joinDate;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getClassRoomId() {
        return classRoomId;
    }
    public void setClassRoomId(int classRoomId) {
        this.classRoomId = classRoomId;
    }
    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public Date getJoinDate() {
        return joinDate;
    }
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}

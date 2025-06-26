package org.example.talentcenter.model;

public class StudentClass {
    private int classroomID;
    private String classroomName;
    private int courseId;
    private int teacherId;

    public StudentClass(int classroomID, String classroomName, int courseId, int teacherId) {
        this.classroomID = classroomID;
        this.classroomName = classroomName;
        this.courseId = courseId;
        this.teacherId = teacherId;
    }

    public StudentClass(int classroomID, String classroomName) {
        this.classroomID = classroomID;
        this.classroomName = classroomName;
    }

    public StudentClass() {
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
}

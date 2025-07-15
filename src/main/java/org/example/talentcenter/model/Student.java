package org.example.talentcenter.model;

import java.time.LocalDate;

public class Student {
    private int id;
    private String name;
    private String parentPhone;
    private String motherPhone;
    private int accountId;
    private int classRoomId;
    private String className;
    private String phoneNumber;
    private int presentCount;
    private int absentCount;
    private LocalDate enrollmentDate;

    // Thuộc tính để xử lý điểm danh
    private Integer attendanceId;
    private String attendanceStatus;
    private String attendanceNote;
    private boolean hasAttendance;
    public Student(int id, String name, String parentPhone, String motherPhone, int accountId, LocalDate enrollmentDate, int classRoomId, String classRoomName, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.parentPhone = parentPhone;
        this.motherPhone = motherPhone;
        this.accountId = accountId;
        this.enrollmentDate = enrollmentDate;
        this.classRoomId = classRoomId;
        this.className = classRoomName;
        this.phoneNumber = phoneNumber;
    }

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getMotherPhone() {
        return motherPhone;
    }

    public void setMotherPhone(String motherPhone) {
        this.motherPhone = motherPhone;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(int presentCount) {
        this.presentCount = presentCount;
    }

    public int getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(int absentCount) {
        this.absentCount = absentCount;
    }

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getAttendanceNote() {
        return attendanceNote;
    }

    public void setAttendanceNote(String attendanceNote) {
        this.attendanceNote = attendanceNote;
    }

    public boolean isHasAttendance() {
        return hasAttendance;
    }

    public void setHasAttendance(boolean hasAttendance) {
        this.hasAttendance = hasAttendance;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}

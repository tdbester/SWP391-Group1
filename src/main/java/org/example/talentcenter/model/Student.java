package org.example.talentcenter.model;

import java.time.LocalDate;

public class Student {
    private int id;
    private String fullName;
    private String parentPhone;
    private String motherPhone;
    private int accountId;
    private LocalDate enrollmentDate;

    // Thống kê điểm danh
    private int presentCount;
    private int absentCount;

    // Thuộc tính để xử lý điểm danh
    private Integer attendanceId;
    private String attendanceStatus;
    private String attendanceNote;
    private boolean hasAttendance;

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public boolean isHasAttendance() {
        return hasAttendance;
    }

    public void setHasAttendance(boolean hasAttendance) {
        this.hasAttendance = hasAttendance;
    }

    public String getAttendanceNote() {
        return attendanceNote;
    }

    public void setAttendanceNote(String attendanceNote) {
        this.attendanceNote = attendanceNote;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public Student() {}

    public Student(int id,String fullName, LocalDate enrollmentDate, int accountId, String motherPhone, String parentPhone) {
        this.id = id;
        this.fullName = fullName;
        this.enrollmentDate = enrollmentDate;
        this.accountId = accountId;
        this.motherPhone = motherPhone;
        this.parentPhone = parentPhone;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getMotherPhone() {
        return motherPhone;
    }

    public void setMotherPhone(String motherPhone) {
        this.motherPhone = motherPhone;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", parentPhone='" + parentPhone + '\'' +
                ", motherPhone='" + motherPhone + '\'' +
                ", accountId=" + accountId +
                ", enrollmentDate=" + enrollmentDate +
                ", presentCount=" + presentCount +
                ", absentCount=" + absentCount +
                '}';
    }
}
package org.example.talentcenter.model;

import java.util.Date;

public class Student {
    private int id;
    private String name;
    private String parentPhone;
    private String motherPhone;
    private String accountId;
    private Date enrollmentDate;
    private int classRoomId;
    private String className;
    private String phoneNumber;

    public Student(int id, String name, String parentPhone, String motherPhone, String accountId, Date enrollmentDate, int classRoomId, String classRoomName, String phoneNumber) {
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
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

}

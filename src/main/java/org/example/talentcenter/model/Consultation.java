package org.example.talentcenter.model;

import java.sql.Timestamp;

public class Consultation {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String courseInterest;
    private boolean contacted;
    private Timestamp createdAt;

    public Consultation() {}

    public Consultation(int id, String fullName, String email, String phone, String courseInterest, boolean contacted) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.courseInterest = courseInterest;
        this.contacted = contacted;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCourseInterest() {
        return courseInterest;
    }

    public void setCourseInterest(String courseInterest) {
        this.courseInterest = courseInterest;
    }

    public boolean isContacted() {
        return contacted;
    }

    public void setContacted(boolean contacted) {
        this.contacted = contacted;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
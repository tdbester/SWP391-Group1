package org.example.talentcenter.model;

public class Consultation {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String status;
    private int courseId;
    private int ProcessedBy;
    private String title;

    public Consultation() {
    }

    public Consultation(int id, int processedBy, int courseId, String status, String phone, String email, String fullName) {
        this.id = id;
        ProcessedBy = processedBy;
        this.courseId = courseId;
        this.status = status;
        this.phone = phone;
        this.email = email;
        this.fullName = fullName;
    }

    public Consultation(int id, String fullName, String email, String phone, String status, String title) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getProcessedBy() {
        return ProcessedBy;
    }

    public void setProcessedBy(int processedBy) {
        ProcessedBy = processedBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
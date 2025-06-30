package org.example.talentcenter.model;

import java.util.Date;

public class Request {
    private int id;
    private String typeName;
    private int senderID;
    private String reason;
    private String status;
    private String response;
    private Date createdAt;
    private Date responseAt;
    private int processedBy;
    private String courseName;
    private String parentPhone;
    private String phoneNumber;

    public Request() {
    }

    public Request(int id, int senderID, String reason, String status, String response, Date createdAt, Date responseAt, int processedBy) {
        this.id = id;
        this.senderID = senderID;
        this.reason = reason;
        this.status = status;
        this.response = response;
        this.createdAt = createdAt;
        this.responseAt = responseAt;
        this.processedBy = processedBy;
    }

    public Request(int senderID, String reason, String courseName, String parentPhone, Date createdAt) {
        this.senderID = senderID;
        this.courseName = courseName;
        this.reason = reason;
        this.parentPhone = parentPhone;
        this.createdAt = createdAt;
    }

    public Request(int id, String reason, int senderID) {
        this.id = id;
        this.reason = reason;
        this.senderID = senderID;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getResponseAt() {
        return responseAt;
    }

    public void setResponseAt(Date responseAt) {
        this.responseAt = responseAt;
    }

    public int getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(int processedBy) {
        this.processedBy = processedBy;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}

package org.example.talentcenter.model;

import java.util.Date;

public class Request {
    private int id;
    private String type;
    private int senderID;
    private String reason;
    private String status;
    private String response;
    private Date createdAt;
    private Date responseAt;
    private int processedBy;

    public Request(int id, String type, int senderID, String reason, String status, String response, Date createdAt, Date responseAt, int processedBy) {
        this.id = id;
        this.type = type;
        this.senderID = senderID;
        this.reason = reason;
        this.status = status;
        this.response = response;
        this.createdAt = createdAt;
        this.responseAt = responseAt;
        this.processedBy = processedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}

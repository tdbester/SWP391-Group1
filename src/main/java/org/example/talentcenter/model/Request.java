package org.example.talentcenter.model;

import java.time.LocalDate;
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
    private String courseName;
    private String parentPhone;
    private String phoneNumber;
    private String typeName;
    private String description;
    private int typeId;
    private String senderName;
    private String senderRole;
    private String senderEmail;
    private LocalDate offDate;     // cho đơn xin nghỉ phép
    private LocalDate fromDate;    // cho đơn đổi lịch
    private LocalDate toDate;
    private int slot;
    private int scheduleId;
    private String targetClassName;
    private Integer targetClassId;
    public Request() {
    }

    public Request(int id, String type, int senderID, String reason, String status, String response, Date createdAt, Date responseAt, int processedBy, String courseName, String parentPhone, String phoneNumber, String typeName, String description, int typeId) {
        this.id = id;
        this.type = type;
        this.senderID = senderID;
        this.reason = reason;
        this.status = status;
        this.response = response;
        this.createdAt = createdAt;
        this.responseAt = responseAt;
        this.processedBy = processedBy;
        this.courseName = courseName;
        this.parentPhone = parentPhone;
        this.phoneNumber = phoneNumber;
        this.typeName = typeName;
        this.description = description;
        this.typeId = typeId;

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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(String senderRole) {
        this.senderRole = senderRole;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public LocalDate getOffDate() {
        return offDate;
    }

    public void setOffDate(LocalDate offDate) {
        this.offDate = offDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }
    public String getTargetClassName() {
        return targetClassName;
    }
    public void setTargetClassName(String targetClassName) {
        this.targetClassName = targetClassName;
    }
    public Integer getTargetClassId() {
        return targetClassId;
    }
    public void setTargetClassId(Integer targetClassId) {
        this.targetClassId = targetClassId;
    }
}

package org.example.talentcenter.model;

import java.sql.Timestamp;

public class TeacherSalary {
    private int salaryId;
    private int teacherId;
    private String teacherName;
    private double salaryPerSession;
    private int month;
    private int year;
    private int totalSessions;
    private double baseSalary;
    private double totalSalary;
    private double adjustment;
    private double finalSalary;
    private Timestamp paymentDate;
    private String note;
    private String processedByName;

    // Constructors
    public TeacherSalary() {}

    // Getters and Setters
    public int getSalaryId() { return salaryId; }
    public void setSalaryId(int salaryId) { this.salaryId = salaryId; }

    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public double getSalaryPerSession() { return salaryPerSession; }
    public void setSalaryPerSession(double salaryPerSession) { this.salaryPerSession = salaryPerSession; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getTotalSessions() { return totalSessions; }
    public void setTotalSessions(int totalSessions) { this.totalSessions = totalSessions; }

    public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }

    public double getTotalSalary() { return totalSalary; }
    public void setTotalSalary(double totalSalary) { this.totalSalary = totalSalary; }

    public double getAdjustment() { return adjustment; }
    public void setAdjustment(double adjustment) { this.adjustment = adjustment; }

    public double getFinalSalary() { return finalSalary; }
    public void setFinalSalary(double finalSalary) { this.finalSalary = finalSalary; }

    public Timestamp getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Timestamp paymentDate) { this.paymentDate = paymentDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getProcessedByName() { return processedByName; }
    public void setProcessedByName(String processedByName) { this.processedByName = processedByName; }

    // Helper methods
    public boolean getPaid() {
        return paymentDate != null;
    }

    public boolean hasAdjustment() {
        return adjustment != 0;
    }
}

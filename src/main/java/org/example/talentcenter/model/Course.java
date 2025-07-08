package org.example.talentcenter.model;

public class Course {
    private int id;
    private String title;
    private double price;
    private String information;
    private int createdBy;
    private int classCount;

    public Course() {
    }

    public Course(int id, String title, double price, String information, int createdBy) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.information = information;
        this.createdBy = createdBy;
    }

    public Course(int id, String title, double price, String information, int createdBy, int classCount) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.information = information;
        this.createdBy = createdBy;
        this.classCount = classCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }
}

package org.example.talentcenter.model;

import org.example.talentcenter.utilities.Level;
import org.example.talentcenter.utilities.Type;

public class Course {
    private int id;
    private String title;
    private double price;
    private String information;
    private int createdBy;
    private String image;
    private Category category;
    private Level level;
    private Type type;
    private int classCount;


    public Course() {
    }

    public Course(int id, String title, double price, String information, int createdBy, String image, Category category, Level level, Type type) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.information = information;
        this.createdBy = createdBy;
        this.image = image;
        this.category = category;
        this.level = level;
        this.type = type;
    }

    public Course(int id, String title, double price, String information, int createdBy, int classCount){
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
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public Level getLevel() {
        return level;
    }
    public void setLevel(Level level) {
        this.level = level;
    }
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public int getClassCount() {
        return classCount;
    }
    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }
}
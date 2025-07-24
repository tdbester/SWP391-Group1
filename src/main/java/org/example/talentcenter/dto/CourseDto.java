package org.example.talentcenter.dto;

import org.example.talentcenter.model.Category;
import org.example.talentcenter.utilities.Level;
import org.example.talentcenter.utilities.Type;

public class CourseDto {
    private int id;
    private String title;
    private double price;
    private String information;
    private int createdBy;
    private String fullname;
    private String image;
    private Category category;
    private Level level;
    private Type type;
    private int status; // 0 = not public (hidden), 1 = public (visible)

    public CourseDto() {
    }

    public CourseDto(int id, String title, double price, String information,
                     int createdBy,String fullname, String image, Category category, Level level, Type type) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.information = information;
        this.createdBy = createdBy;
        this.image = image;
        this.fullname = fullname;
        this.category = category;
        this.level = level;
        this.type = type;
        this.status = 1; // Default to public
    }

    public CourseDto(int id, String title, double price, String information,
                     int createdBy,String fullname, String image, Category category, Level level, Type type, int status) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.information = information;
        this.createdBy = createdBy;
        this.image = image;
        this.fullname = fullname;
        this.category = category;
        this.level = level;
        this.type = type;
        this.status = status;
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
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
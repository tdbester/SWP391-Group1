package org.example.talentcenter.model;

public class Course {
    private int id;
    private String title;
    private double price;
    private String information;
    private int createdBy;
    private String image;
    private Category category;

    public Course() {
    }

    public Course(int id, String title, double price, String information, int createdBy, String image, Category category) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.information = information;
        this.createdBy = createdBy;
        this.image = image;
        this.category = category;
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

}

package org.example.talentcenter.dto;

import java.util.Date;

public class BlogDto {
    private int id;
    private String title;
    private String description;
    private String content;
    private String image;
    private Date createdAt;
    private String fullname;

    public BlogDto() {
    }

    public BlogDto(int id, String title, String description, String content, String image, Date createdAt, String fullname) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.image = image;
        this.createdAt = createdAt;
        this.fullname = fullname;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getContent() {
        return content;
    }
    public String getImage() {
        return image;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public String getFullname() {
        return fullname;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


}
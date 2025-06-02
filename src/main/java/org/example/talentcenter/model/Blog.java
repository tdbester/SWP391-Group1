package org.example.talentcenter.model;

import java.util.Date;

public class Blog {
    private Integer id;
    private String title;
    private String description;
    private String content;
    private String image;
    private Integer authorId;
    private Date createdAt;


    public Blog(Integer id, String title, String content, String image, Integer authorId, Date createdAt, String description) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.description = description;
    }

    public Blog() {
    }

    public Blog(int id, String title, String description, String content, String image, int authorId, Date createdAt) {
    }


    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }


    public String getImage() {
        return image;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

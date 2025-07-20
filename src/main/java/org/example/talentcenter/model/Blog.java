package org.example.talentcenter.model;

import java.util.Date;

public class Blog {
    private Integer id;
    private String title;
    private String description;
    private String content;
    private String image;
    private Integer authorId;
    private Integer category;
    private Date createdAt;
    private int status; // 0 = not public (hidden), 1 = public (visible)



    public Blog(Integer id, String title, String content, String image, Integer authorId, Date createdAt, String description, Integer category) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.description = description;
        this.category = category;
        this.status = 1; // Default to public
    }

    public Blog(Integer id, String title, String content, String image, Integer authorId, Date createdAt, String description, Integer category, int status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.description = description;
        this.category = category;
        this.status = status;
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

    public Integer getCategory() {
        return category;
    }
    public void setCategory(Integer category) {
        this.category = category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", authorId=" + authorId +
                ", createdAt=" + createdAt +
                ", status=" + status +
                '}';
    }
}

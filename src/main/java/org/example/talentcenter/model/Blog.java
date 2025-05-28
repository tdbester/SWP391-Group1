package org.example.talentcenter.model;

import java.sql.Date;

public class Blog {
    private Integer id;
    private String title;
    private String content;
    private Boolean status;
    private Date createdBy;
    private Date createdAt;

    public Blog(Integer id, String title, String content, Boolean status, Date createdBy, Date createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Blog() {
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

    public Boolean getStatus() {
        return status;
    }

    public Date getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setCreatedBy(Date createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

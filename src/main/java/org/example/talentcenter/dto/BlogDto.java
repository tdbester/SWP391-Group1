package org.example.talentcenter.dto;

public class BlogDto {
    private int id;
    private String title;
    private String image;
    private boolean status;
    private String username;

    public BlogDto(int id, String title, String image, boolean status, String username) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.status = status;
        this.username = username;
    }

    // getters và setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}

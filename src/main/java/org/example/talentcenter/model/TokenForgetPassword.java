package org.example.talentcenter.model;

import java.time.LocalDateTime;

public class TokenForgetPassword {
    private int id, userId;
    private boolean isUsed;
    private String token;
    private LocalDateTime expiryTime;

    public TokenForgetPassword(int id, int userId, boolean isUsed, String token, LocalDateTime expiryTime) {
        this.id = id;
        this.userId = userId;
        this.isUsed = isUsed;
        this.token = token;
        this.expiryTime = expiryTime;
    }

    public TokenForgetPassword(int userId, boolean isUsed, String token, LocalDateTime expiryTime) {
        this.userId = userId;
        this.isUsed = isUsed;
        this.token = token;
        this.expiryTime = expiryTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    @Override
    public String toString() {
        return "TokenForgetPassword{" +
                "id=" + id +
                ", userId=" + userId +
                ", isUsed=" + isUsed +
                ", token='" + token + '\'' +
                ", expiryTime=" + expiryTime +
                '}';
    }
}

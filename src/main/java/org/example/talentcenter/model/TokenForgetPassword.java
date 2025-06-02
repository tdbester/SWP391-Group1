package org.example.talentcenter.model;

import java.time.LocalDateTime;

public class TokenForgetPassword {
    private int id;
    private String token;
    private LocalDateTime expiryTime;
    private boolean isUsed;
    private int accountId; // Changed from userId to accountId

    public TokenForgetPassword(int id, boolean b, String token, LocalDateTime localDateTime) {
    }

    public TokenForgetPassword(int id, String token, LocalDateTime expiryTime, boolean isUsed, int accountId) {
        this.id = id;
        this.token = token;
        this.expiryTime = expiryTime;
        this.isUsed = isUsed;
        this.accountId = accountId;
    }

    public TokenForgetPassword(String token, LocalDateTime expiryTime, boolean isUsed, int accountId) {
        this.token = token;
        this.expiryTime = expiryTime;
        this.isUsed = isUsed;
        this.accountId = accountId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getExpiryTime() { return expiryTime; }
    public void setExpiryTime(LocalDateTime expiryTime) { this.expiryTime = expiryTime; }

    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { isUsed = used; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    @Override
    public String toString() {
        return "TokenForgetPassword{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", expiryTime=" + expiryTime +
                ", isUsed=" + isUsed +
                ", accountId=" + accountId +
                '}';
    }
}
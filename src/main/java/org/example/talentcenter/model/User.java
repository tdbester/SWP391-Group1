package org.example.talentcenter.model;

import java.time.LocalDateTime;
import java.util.Date;

public class User {
    private int id;
    private String full_name;
    private String email;
    private String password;
    private String role;
    private String phone;
    private String status;
    private LocalDateTime created_at;

    public User(){
    }

    public User(int id, String full_name, String email, String password, String role, String phone, String status, LocalDateTime created_at) {
        this.id = id;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.status = status;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFull_name() {
        return full_name;
    }
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}

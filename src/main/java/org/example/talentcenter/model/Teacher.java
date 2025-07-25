package org.example.talentcenter.model;

public class Teacher {

    private Integer id;
    private Integer accountId;
    private String department;
    private double salary;
    private String fullName;

    private Account account;
    // Constructors
    public Teacher() {}

    public Teacher(Integer accountId, String department, double salary) {
        this.accountId = accountId;
        this.department = department;
        this.salary = salary;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                '}';
    }
}
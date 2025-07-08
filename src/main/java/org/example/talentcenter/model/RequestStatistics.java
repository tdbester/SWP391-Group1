package org.example.talentcenter.model;

public class RequestStatistics {
    private int total;
    private int pending;
    private int approved;
    private int rejected;

    public RequestStatistics() {}

    public RequestStatistics(int total, int pending, int approved, int rejected) {
        this.total = total;
        this.pending = pending;
        this.approved = approved;
        this.rejected = rejected;
    }

    // Getters and Setters
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getRejected() {
        return rejected;
    }

    public void setRejected(int rejected) {
        this.rejected = rejected;
    }

    @Override
    public String toString() {
        return "RequestStatistics{" +
                "total=" + total +
                ", pending=" + pending +
                ", approved=" + approved +
                ", rejected=" + rejected +
                '}';
    }
}

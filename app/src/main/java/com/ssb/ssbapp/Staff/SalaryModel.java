package com.ssb.ssbapp.Staff;

public class SalaryModel {
    String sid;
    String staffID;
    double amount;
    String date;
    String status;

    public SalaryModel() {
    }

    public SalaryModel(String sid, double amount, String date, String status,String staffID) {
        this.sid = sid;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.staffID=staffID;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

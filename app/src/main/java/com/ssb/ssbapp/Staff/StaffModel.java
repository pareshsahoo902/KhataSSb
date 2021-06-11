package com.ssb.ssbapp.Staff;

import java.io.Serializable;

public class StaffModel implements Serializable {


    String name , aadhar ,contact,branch , profile_image, date_of_joining,last_salary;
    boolean admin;
    int type , salary;

    public StaffModel() {
    }

    public StaffModel(String name, String aadhar, String contact, String branch, String profile_image, String date_of_joining, boolean admin, int type, int salary, String last_salary) {
        this.name = name;
        this.aadhar = aadhar;
        this.contact = contact;
        this.branch = branch;
        this.profile_image = profile_image;
        this.date_of_joining = date_of_joining;
        this.admin = admin;
        this.type = type;
        this.salary = salary;
        this.last_salary= last_salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getDate_of_joining() {
        return date_of_joining;
    }

    public void setDate_of_joining(String date_of_joining) {
        this.date_of_joining = date_of_joining;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getLast_salary() {
        return last_salary;
    }

    public void setLast_salary(String last_salary) {
        this.last_salary = last_salary;
    }
}

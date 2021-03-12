package com.ssb.ssbapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class EmployeeModel implements Serializable {

    private String name ,aadhar , contact , loginID , password , branchAssinged;
    private long type;

    public EmployeeModel(String name, String aadhar, String contact, String loginID, String password, String branchAssinged, long type) {
        this.name = name;
        this.aadhar = aadhar;
        this.contact = contact;
        this.loginID = loginID;
        this.password = password;
        this.branchAssinged = branchAssinged;
        this.type = type;
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

    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBranchAssinged() {
        return branchAssinged;
    }

    public void setBranchAssinged(String branchAssinged) {
        this.branchAssinged = branchAssinged;
    }

    public long getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

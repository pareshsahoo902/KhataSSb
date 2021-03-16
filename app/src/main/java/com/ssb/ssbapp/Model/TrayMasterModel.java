package com.ssb.ssbapp.Model;

import java.io.Serializable;

public class TrayMasterModel implements Serializable {

    String name , company , tid , created_at;
    boolean isActive;

    public TrayMasterModel() {
    }

    public TrayMasterModel(String name, String company, String tid, String created_at, boolean isActive) {
        this.name = name;
        this.company = company;
        this.tid = tid;
        this.created_at = created_at;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

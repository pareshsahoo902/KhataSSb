package com.ssb.ssbapp.Model;

import java.io.Serializable;

public class KhataModel implements Serializable {

    String name , company, kid , created_at;
    boolean active;

    public KhataModel() {
    }

    public KhataModel(String name, String company, String kid, String created_at, boolean isActive) {
        this.name = name;
        this.company = company;
        this.kid = kid;
        this.created_at = created_at;
        this.active = isActive;
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

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

package com.ssb.ssbapp.Customer;

import java.io.Serializable;

public class CustomerModel implements Serializable {

    String name, contact, uid, updated;

    public CustomerModel() {
    }

    public CustomerModel(String name, String contact, String uid, String updated) {
        this.name = name;
        this.contact = contact;
        this.uid = uid;
        this.updated = updated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}

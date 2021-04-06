package com.ssb.ssbapp.CashDetails;

import java.io.Serializable;

public class CashModel implements Serializable {

    public String id;
    public String ceid;
    public String cid;
    public String kid;
    public String customerName;

    public String date;
    public String edit_date;

    public String status;

    public double total;

    public CashModel() {
    }

    public CashModel(String id, String ceid, String cid, String kid, String customerName, String date, String edit_date, String status, double total) {
        this.id = id;
        this.ceid = ceid;
        this.cid = cid;
        this.kid = kid;
        this.customerName = customerName;
        this.date = date;
        this.edit_date = edit_date;
        this.status = status;
        this.total = total;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCeid() {
        return ceid;
    }

    public void setCeid(String ceid) {
        this.ceid = ceid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEdit_date() {
        return edit_date;
    }

    public void setEdit_date(String edit_date) {
        this.edit_date = edit_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

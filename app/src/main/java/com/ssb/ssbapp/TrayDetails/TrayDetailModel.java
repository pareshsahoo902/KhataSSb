package com.ssb.ssbapp.TrayDetails;

import com.ssb.ssbapp.TrayModels.TrayModelItem;

import java.io.Serializable;
import java.util.ArrayList;

public class TrayDetailModel implements Serializable {

    private ArrayList<TrayModelItem> modelItemArrayList;
    private String id;
    private String teid;

    public String cid;
    public String kid;
    public String customerName;

    public String date;

    public String status;

    public int total;


    public TrayDetailModel() {
    }

    public TrayDetailModel(ArrayList<TrayModelItem> modelItemArrayList, String id, String teid, String cid, String kid, String customerName, String date, String status, int total) {
        this.modelItemArrayList = modelItemArrayList;
        this.id = id;
        this.teid = teid;
        this.cid = cid;
        this.kid = kid;
        this.customerName = customerName;
        this.date = date;
        this.status = status;
        this.total = total;
    }

    public ArrayList<TrayModelItem> getModelItemArrayList() {
        return modelItemArrayList;
    }

    public void setModelItemArrayList(ArrayList<TrayModelItem> modelItemArrayList) {
        this.modelItemArrayList = modelItemArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeid() {
        return teid;
    }

    public void setTeid(String teid) {
        this.teid = teid;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

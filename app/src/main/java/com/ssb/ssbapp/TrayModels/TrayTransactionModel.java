package com.ssb.ssbapp.TrayModels;

import java.io.Serializable;
import java.util.ArrayList;

public class TrayTransactionModel implements Serializable {

    private String teid;
    private String cid;
    private String kid;

    private String date;
    private String status;
    private long total;

    private String imageUrl;
    private String description;
    private long balance;
    private ArrayList<TrayModelItem> modelItemArrayList;

    public TrayTransactionModel() {

    }

    public TrayTransactionModel(String teid, String cid, String kid, String date, String status, long total, String imageUrl, String description, long balance, ArrayList<TrayModelItem> modelItemArrayList) {
        this.teid = teid;
        this.cid = cid;
        this.kid = kid;
        this.date = date;
        this.status = status;
        this.total = total;
        this.imageUrl = imageUrl;
        this.description = description;
        this.balance = balance;
        this.modelItemArrayList = modelItemArrayList;
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public ArrayList<TrayModelItem> getModelItemArrayList() {
        return modelItemArrayList;
    }

    public void setModelItemArrayList(ArrayList<TrayModelItem> modelItemArrayList) {
        this.modelItemArrayList = modelItemArrayList;
    }
}

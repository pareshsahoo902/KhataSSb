package com.ssb.ssbapp.TransactionModel;

import java.io.Serializable;

public class MoneyTransactionModel implements Serializable {

    public String ceid;
    public String cid;
    public String kid;

    public String date;
    public String edit_date;

    public String imageurl;
    public String description;
    public String entriesText;
    public String status;
    public boolean isCleared;
    public boolean isParty;
    public String total;
    public String balance;

    public MoneyTransactionModel() {
    }

    public MoneyTransactionModel(String ceid, String cid, String kid, String date, String edit_date, String imageurl, String description, String entriesText, String status, String total, String balance ,boolean isCleared, boolean isParty) {


        this.ceid = ceid;
        this.cid = cid;
        this.kid = kid;
        this.date = date;
        this.edit_date = edit_date;
        this.imageurl = imageurl;
        this.description = description;
        this.entriesText = entriesText;
        this.status = status;
        this.total = total;
        this.balance = balance;
        this.isCleared = isCleared;
        this.isParty = isParty;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntriesText() {
        return entriesText;
    }

    public void setEntriesText(String entriesText) {
        this.entriesText = entriesText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }


    public boolean isCleared() {
        return isCleared;
    }

    public void setCleared(boolean cleared) {
        isCleared = cleared;
    }

    public boolean isParty() {
        return isParty;
    }

    public void setParty(boolean party) {
        isParty = party;
    }
}

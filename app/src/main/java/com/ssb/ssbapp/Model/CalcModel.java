package com.ssb.ssbapp.Model;

public class CalcModel {
    String cid;
    Double amount;
    int amountTray;

    public CalcModel(String cid, Double amount) {
        this.cid = cid;
        this.amount = amount;
    }

    public CalcModel(String cid, int amountTray) {
        this.cid = cid;
        this.amountTray = amountTray;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Double getAmount() {
        return amount;
    }

    public int getAmountTray() {
        return amountTray;
    }

    public void setAmountTray(int amountTray) {
        this.amountTray = amountTray;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

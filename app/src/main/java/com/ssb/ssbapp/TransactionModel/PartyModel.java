package com.ssb.ssbapp.TransactionModel;

import java.io.Serializable;

public class PartyModel implements Serializable {

    private String total;
    private double commision , fair, labour , extra;
    private int commPercent;

    public PartyModel(String total, double commision, double fair, double labour, double extra, int commPercent) {
        this.total = total;
        this.commision = commision;
        this.fair = fair;
        this.labour = labour;
        this.extra = extra;
        this.commPercent = commPercent;
    }

    public PartyModel() {
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public double getCommision() {
        return commision;
    }

    public void setCommision(double commision) {
        this.commision = commision;
    }

    public double getFair() {
        return fair;
    }

    public void setFair(double fair) {
        this.fair = fair;
    }

    public double getLabour() {
        return labour;
    }

    public void setLabour(double labour) {
        this.labour = labour;
    }

    public double getExtra() {
        return extra;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }

    public int getCommPercent() {
        return commPercent;
    }

    public void setCommPercent(int commPercent) {
        this.commPercent = commPercent;
    }
}

package com.ssb.ssbapp.TrayModels;

import java.io.Serializable;

public class TrayModelItem implements Serializable {

    private String id;
    private String name;
    private int totalCount;

    public TrayModelItem(String id, String name, int totalCount) {
        this.id = id;
        this.name = name;
        this.totalCount = totalCount;
    }

    public TrayModelItem() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}

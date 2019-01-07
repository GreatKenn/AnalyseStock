package com.mp2.analysestock.db.model;

public class D2IndexData extends D2BaseData {

    private double change_val;
    private double pct_change_val;

    public double getChange_val() {
        return change_val;
    }

    public void setChange_val(double change_val) {
        this.change_val = change_val;
    }

    public double getPct_change_val() {
        return pct_change_val;
    }

    public void setPct_change_val(double pct_change_val) {
        this.pct_change_val = pct_change_val;
    }
}

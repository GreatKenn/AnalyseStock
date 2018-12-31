package com.mp2.analysestock.db.model;

public class DBaseData {
    private String yyyymmdd;
    private double close_val;
    private int volume_val;
    private double change_val;

    public String getYyyymmdd() {
        return yyyymmdd;
    }

    public void setYyyymmdd(String yyyymmdd) {
        this.yyyymmdd = yyyymmdd;
    }

    public double getClose_val() {
        return close_val;
    }

    public void setClose_val(double close_val) {
        this.close_val = close_val;
    }

    public int getVolume_val() {
        return volume_val;
    }

    public void setVolume_val(int volume_val) {
        this.volume_val = volume_val;
    }

    public double getChange_val() {
        return change_val;
    }

    public void setChange_val(double change_val) {
        this.change_val = change_val;
    }
}

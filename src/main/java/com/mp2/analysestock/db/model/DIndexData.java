package com.mp2.analysestock.db.model;

public class DIndexData {
    private String yyyymmdd;
    private double sh_close;
    private int sh_volume;
    private double sh_change;
    private double sz_close;
    private int sz_volume;
    private double sz_change;

    public String getYyyymmdd() {
        return yyyymmdd;
    }

    public void setYyyymmdd(String yyyymmdd) {
        this.yyyymmdd = yyyymmdd;
    }

    public double getSh_close() {
        return sh_close;
    }

    public void setSh_close(double sh_close) {
        this.sh_close = sh_close;
    }

    public int getSh_volume() {
        return sh_volume;
    }

    public void setSh_volume(int sh_volume) {
        this.sh_volume = sh_volume;
    }

    public double getSh_change() {
        return sh_change;
    }

    public void setSh_change(double sh_change) {
        this.sh_change = sh_change;
    }

    public double getSz_close() {
        return sz_close;
    }

    public void setSz_close(double sz_close) {
        this.sz_close = sz_close;
    }

    public int getSz_volume() {
        return sz_volume;
    }

    public void setSz_volume(int sz_volume) {
        this.sz_volume = sz_volume;
    }

    public double getSz_change() {
        return sz_change;
    }

    public void setSz_change(double sz_change) {
        this.sz_change = sz_change;
    }
}

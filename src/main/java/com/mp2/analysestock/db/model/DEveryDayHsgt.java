package com.mp2.analysestock.db.model;

public class DEveryDayHsgt {
    private String trade_date;
    private double ggt_ss;
    private double ggt_sz;
    private double hgt;
    private double sgt;
    private double north_money;
    private double south_money;

    public String getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(String trade_date) {
        this.trade_date = trade_date;
    }

    public double getGgt_ss() {
        return ggt_ss;
    }

    public void setGgt_ss(double ggt_ss) {
        this.ggt_ss = ggt_ss;
    }

    public double getGgt_sz() {
        return ggt_sz;
    }

    public void setGgt_sz(double ggt_sz) {
        this.ggt_sz = ggt_sz;
    }

    public double getHgt() {
        return hgt;
    }

    public void setHgt(double hgt) {
        this.hgt = hgt;
    }

    public double getSgt() {
        return sgt;
    }

    public void setSgt(double sgt) {
        this.sgt = sgt;
    }

    public double getNorth_money() {
        return north_money;
    }

    public void setNorth_money(double north_money) {
        this.north_money = north_money;
    }

    public double getSouth_money() {
        return south_money;
    }

    public void setSouth_money(double south_money) {
        this.south_money = south_money;
    }
}

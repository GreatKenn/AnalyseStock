package com.mp2.analysestock.db.model;

public class D2TotalIndexBetweenDate {
    private int day_count;
    private double sum_vol;
    private double chg_rate;
    private double val_50;  // 50% 位置值

    public int getDay_count() {
        return day_count;
    }

    public void setDay_count(int day_count) {
        this.day_count = day_count;
    }

    public double getSum_vol() {
        return sum_vol;
    }

    public void setSum_vol(double sum_vol) {
        this.sum_vol = sum_vol;
    }

    public double getChg_rate() {
        return chg_rate;
    }

    public void setChg_rate(double chg_rate) {
        this.chg_rate = chg_rate;
    }

    public double getVal_50() {
        return val_50;
    }

    public void setVal_50(double val_50) {
        this.val_50 = val_50;
    }

    public String toString() {
        return "day_count:" + day_count + " sum_vol:" + sum_vol + " chg_rate:" + chg_rate + " val_50:" + val_50;
    }
}

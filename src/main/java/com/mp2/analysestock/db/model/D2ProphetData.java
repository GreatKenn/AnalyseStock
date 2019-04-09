package com.mp2.analysestock.db.model;

public class D2ProphetData {
    private String trade_date;
    private double yhat;
    private double yhat_lower;
    private double yhat_upper;

    public String toString() {
        return trade_date + "|" + yhat + "|" + yhat_lower + "|" + yhat_upper;
    }

    public String getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(String trade_date) {
        this.trade_date = trade_date;
    }

    public double getYhat() {
        return yhat;
    }

    public void setYhat(double yhat) {
        this.yhat = yhat;
    }

    public double getYhat_lower() {
        return yhat_lower;
    }

    public void setYhat_lower(double yhat_lower) {
        this.yhat_lower = yhat_lower;
    }

    public double getYhat_upper() {
        return yhat_upper;
    }

    public void setYhat_upper(double yhat_upper) {
        this.yhat_upper = yhat_upper;
    }
}

package com.mp2.analysestock.db.model;

public class DEveryDayTotal {
    private String trade_date;
    private double sh_close;
    private double sz_close;
    private double sh_turnover_rate;
    private double sz_turnover_rate;
    private double sh_total_mv;
    private double sz_total_mv;

    public String getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(String trade_date) {
        this.trade_date = trade_date;
    }

    public double getSh_close() {
        return sh_close;
    }

    public void setSh_close(double sh_close) {
        this.sh_close = sh_close;
    }

    public double getSz_close() {
        return sz_close;
    }

    public void setSz_close(double sz_close) {
        this.sz_close = sz_close;
    }

    public double getSh_turnover_rate() {
        return sh_turnover_rate;
    }

    public void setSh_turnover_rate(double sh_turnover_rate) {
        this.sh_turnover_rate = sh_turnover_rate;
    }

    public double getSz_turnover_rate() {
        return sz_turnover_rate;
    }

    public void setSz_turnover_rate(double sz_turnover_rate) {
        this.sz_turnover_rate = sz_turnover_rate;
    }

    public double getSh_total_mv() {
        return sh_total_mv;
    }

    public void setSh_total_mv(double sh_total_mv) {
        this.sh_total_mv = sh_total_mv;
    }

    public double getSz_total_mv() {
        return sz_total_mv;
    }

    public void setSz_total_mv(double sz_total_mv) {
        this.sz_total_mv = sz_total_mv;
    }
}

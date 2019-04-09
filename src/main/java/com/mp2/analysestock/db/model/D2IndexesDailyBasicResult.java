package com.mp2.analysestock.db.model;

public class D2IndexesDailyBasicResult {
    private String trade_date;
    private double total_mv;
    private double float_mv;
    private double total_share;
    private double float_share;
    private double free_share;
    private double turnover_rate;
    private double turnover_rate_f;
    private double pe;
    private double pe_ttm;
    private double pb;

    public String getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(String trade_date) {
        this.trade_date = trade_date;
    }

    public double getTotal_mv() {
        return total_mv;
    }

    public void setTotal_mv(double total_mv) {
        this.total_mv = total_mv;
    }

    public double getFloat_mv() {
        return float_mv;
    }

    public void setFloat_mv(double float_mv) {
        this.float_mv = float_mv;
    }

    public double getTotal_share() {
        return total_share;
    }

    public void setTotal_share(double total_share) {
        this.total_share = total_share;
    }

    public double getFloat_share() {
        return float_share;
    }

    public void setFloat_share(double float_share) {
        this.float_share = float_share;
    }

    public double getFree_share() {
        return free_share;
    }

    public void setFree_share(double free_share) {
        this.free_share = free_share;
    }

    public double getTurnover_rate() {
        return turnover_rate;
    }

    public void setTurnover_rate(double turnover_rate) {
        this.turnover_rate = turnover_rate;
    }

    public double getTurnover_rate_f() {
        return turnover_rate_f;
    }

    public void setTurnover_rate_f(double turnover_rate_f) {
        this.turnover_rate_f = turnover_rate_f;
    }

    public double getPe() {
        return pe;
    }

    public void setPe(double pe) {
        this.pe = pe;
    }

    public double getPe_ttm() {
        return pe_ttm;
    }

    public void setPe_ttm(double pe_ttm) {
        this.pe_ttm = pe_ttm;
    }

    public double getPb() {
        return pb;
    }

    public void setPb(double pb) {
        this.pb = pb;
    }
}

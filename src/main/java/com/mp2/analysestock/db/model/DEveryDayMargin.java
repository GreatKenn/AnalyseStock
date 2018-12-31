package com.mp2.analysestock.db.model;

public class DEveryDayMargin {
    private String trade_date;
    private double sh_rzye;
    private double sz_rzye;
    private double sh_rqye;
    private double sz_rqye;
    private double all_rzye;

    public String getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(String trade_date) {
        this.trade_date = trade_date;
    }

    public double getSh_rzye() {
        return sh_rzye;
    }

    public void setSh_rzye(double sh_rzye) {
        this.sh_rzye = sh_rzye;
        this.all_rzye = sh_rzye + this.sz_rzye;
    }

    public double getSz_rzye() {
        return sz_rzye;
    }

    public void setSz_rzye(double sz_rzye) {
        this.sz_rzye = sz_rzye;
        this.all_rzye = this.sh_rzye + sz_rzye;
    }

    public double getSh_rqye() {
        return sh_rqye;
    }

    public void setSh_rqye(double sh_rqye) {
        this.sh_rqye = sh_rqye;
    }

    public double getSz_rqye() {
        return sz_rqye;
    }

    public void setSz_rqye(double sz_rqye) {
        this.sz_rqye = sz_rqye;
    }

    public double getAll_rzye() {
        return all_rzye;
    }

    public void setAll_rzye(double all_rzye) {
        this.all_rzye = all_rzye;
    }
}

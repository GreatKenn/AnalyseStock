package com.mp2.analysestock.db.model;

public class D2AnalyseKResult {
    private String ts_code;
    private String trade_date;
    private double open_val;
    private double high_val;
    private double low_val;
    private double close_val;
    private double vol_val;
    private double ema_5_val;
    private double ema_10_val;
    private double ema_20_val;
    private double ema_30_val;
    private double ema_250_val;
    private double bias_5_val;
    private double bias_20_val;
    private double macd_val;
    private double psy_val;

    public String toString() {
        return "ts_code:" + ts_code + " trade_date:" + trade_date + " open_val:" + open_val + " high_val:" + high_val
                + " low_val:" + low_val + " close_val:" + close_val + " vol_val:" + vol_val + " ema_5_val:" + ema_5_val
                + " ema_10_val:" + ema_10_val + " ema_20_val:" + ema_20_val + " ema_30_val:" + ema_30_val
                + " ema_250_val:" + ema_250_val + " bias_5_val:" + bias_5_val + " bias_20_val:" + bias_20_val
                + " macd_val:" + macd_val + " psy_val:" + psy_val;
    }

    public String getTs_code() {
        return ts_code;
    }

    public void setTs_code(String ts_code) {
        this.ts_code = ts_code;
    }

    public String getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(String trade_date) {
        this.trade_date = trade_date;
    }

    public double getOpen_val() {
        return open_val;
    }

    public void setOpen_val(double open_val) {
        this.open_val = open_val;
    }

    public double getHigh_val() {
        return high_val;
    }

    public void setHigh_val(double high_val) {
        this.high_val = high_val;
    }

    public double getLow_val() {
        return low_val;
    }

    public void setLow_val(double low_val) {
        this.low_val = low_val;
    }

    public double getClose_val() {
        return close_val;
    }

    public void setClose_val(double close_val) {
        this.close_val = close_val;
    }

    public double getVol_val() {
        return vol_val;
    }

    public void setVol_val(double vol_val) {
        this.vol_val = vol_val;
    }

    public double getEma_5_val() {
        return ema_5_val;
    }

    public void setEma_5_val(double ema_5_val) {
        this.ema_5_val = ema_5_val;
    }

    public double getEma_10_val() {
        return ema_10_val;
    }

    public void setEma_10_val(double ema_10_val) {
        this.ema_10_val = ema_10_val;
    }

    public double getEma_20_val() {
        return ema_20_val;
    }

    public void setEma_20_val(double ema_20_val) {
        this.ema_20_val = ema_20_val;
    }

    public double getEma_30_val() {
        return ema_30_val;
    }

    public void setEma_30_val(double ema_30_val) {
        this.ema_30_val = ema_30_val;
    }

    public double getEma_250_val() {
        return ema_250_val;
    }

    public void setEma_250_val(double ema_250_val) {
        this.ema_250_val = ema_250_val;
    }

    public double getBias_5_val() {
        return bias_5_val;
    }

    public void setBias_5_val(double bias_5_val) {
        this.bias_5_val = bias_5_val;
    }

    public double getBias_20_val() {
        return bias_20_val;
    }

    public void setBias_20_val(double bias_20_val) {
        this.bias_20_val = bias_20_val;
    }

    public double getMacd_val() {
        return macd_val;
    }

    public void setMacd_val(double macd_val) {
        this.macd_val = macd_val;
    }

    public double getPsy_val() {
        return psy_val;
    }

    public void setPsy_val(double psy_val) {
        this.psy_val = psy_val;
    }
}

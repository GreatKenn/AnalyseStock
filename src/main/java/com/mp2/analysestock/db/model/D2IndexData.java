package com.mp2.analysestock.db.model;

public class D2IndexData {
    private String trade_date;
    private double open_val;
    private double close_val;
    private double high_val;
    private double low_val;
    private double change_val;
    private double pct_change_val;
    private int volume_val;
    private int amount_val;

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

    public double getClose_val() {
        return close_val;
    }

    public void setClose_val(double close_val) {
        this.close_val = close_val;
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

    public int getVolume_val() {
        return volume_val;
    }

    public void setVolume_val(int volume_val) {
        this.volume_val = volume_val;
    }

    public int getAmount_val() {
        return amount_val;
    }

    public void setAmount_val(int amount_val) {
        this.amount_val = amount_val;
    }

    public String toString() {
        String re = "Date:" + trade_date + " H:" + high_val + " L:" + low_val;

        return re;
    }

    /**
     * 获取此K线的趋势 -1向下 0十字 1向上
     * @return
     */
    public int getDirection() {
        int re = 0;

        if(open_val < close_val)
            re = 1;
        else if(open_val > close_val)
            re = 1;

        return re;
    }
}

package com.mp2.analysestock.action.structure;

public class ChanPoint {
    public short tp; // 0:顶分型 1:底分型
    public int arr_pos;
    public String trade_date;
    public double close_val;

    public String toString() {
        String re = "tp:" + tp + " trade_date:" + trade_date + " close_val:" + close_val;
        return re;
    }
}

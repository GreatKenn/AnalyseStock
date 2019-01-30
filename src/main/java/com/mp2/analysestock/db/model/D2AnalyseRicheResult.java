package com.mp2.analysestock.db.model;

public class D2AnalyseRicheResult {
    private String ts_code;
    private String class_str;
    private int order_val;
    private String data_json;

    public String getTs_code() {
        return ts_code;
    }

    public void setTs_code(String ts_code) {
        this.ts_code = ts_code;
    }

    public String getClass_str() {
        return class_str;
    }

    public void setClass_str(String class_str) {
        this.class_str = class_str;
    }

    public int getOrder_val() {
        return order_val;
    }

    public void setOrder_val(int order_val) {
        this.order_val = order_val;
    }

    public String getData_json() {
        return data_json;
    }

    public void setData_json(String data_json) {
        this.data_json = data_json;
    }
}

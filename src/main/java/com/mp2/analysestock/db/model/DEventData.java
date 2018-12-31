package com.mp2.analysestock.db.model;

public class DEventData {
    private String yyyymmdd;
    private String icon_name;
    private String event_name;
    private String event_memo;

    public String getYyyymmdd() {
        return yyyymmdd;
    }

    public void setYyyymmdd(String yyyymmdd) {
        this.yyyymmdd = yyyymmdd;
    }

    public String getIcon_name() {
        return icon_name;
    }

    public void setIcon_name(String icon_name) {
        this.icon_name = icon_name;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_memo() {
        return event_memo;
    }

    public void setEvent_memo(String event_memo) {
        this.event_memo = event_memo;
    }
}

package com.mp2.analysestock.db.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kenn on 16/5/9.
 */
public class SelectResult extends BaseStruct {

    private final static Logger logger = LoggerFactory.getLogger(SelectResult.class);

    public int total_count;
    public SelectItem[] items;

    public int getTotal_count() {
        return total_count;
    }
    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }
    public SelectItem[] getItems() {
        return items;
    }
    public void setItems(SelectItem[] items) {
        this.items = items;
    }

}

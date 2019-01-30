package com.mp2.analysestock.action.structure;

public class ChanLine {
    public int arr_pos;
    public ChanPoint highPoint;
    public ChanPoint lowPoint;

    public String toString() {
        return "Low(" + lowPoint.toString() + ") High(" + highPoint.toString() + ")";
    }
}

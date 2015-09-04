package com.palliser.neogenesis.bi;

/**
 * Created by NeoGenesis on 03/09/2015.
 */
public class Turnover {

    private int id;
    private String date;
    private double value;

    public Turnover() {
    }

    public Turnover(int id, String date, double value) {
        this.id = id;
        this.date = date;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

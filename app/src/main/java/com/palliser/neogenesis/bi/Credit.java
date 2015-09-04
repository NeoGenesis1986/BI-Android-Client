package com.palliser.neogenesis.bi;

/**
 * Created by NeoGenesis on 04/09/2015.
 */
public class Credit {

    private int id;
    private String date;
    private double suivi00;
    private double suivi30;
    private double suivi60;
    private double suivi90;

    public Credit() {
    }

    public Credit(int id, String date, double suivi00, double suivi30, double suivi60, double suivi90) {
        this.id = id;
        this.date = date;
        this.suivi00 = suivi00;
        this.suivi30 = suivi30;
        this.suivi60 = suivi60;
        this.suivi90 = suivi90;
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

    public double getSuivi00() {
        return suivi00;
    }

    public void setSuivi00(double suivi00) {
        this.suivi00 = suivi00;
    }

    public double getSuivi30() {
        return suivi30;
    }

    public void setSuivi30(double suivi30) {
        this.suivi30 = suivi30;
    }

    public double getSuivi60() {
        return suivi60;
    }

    public void setSuivi60(double suivi60) {
        this.suivi60 = suivi60;
    }

    public double getSuivi90() {
        return suivi90;
    }

    public void setSuivi90(double suivi90) {
        this.suivi90 = suivi90;
    }
}

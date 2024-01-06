package com.example.medialert.Model;

import java.io.Serializable;

public class Data implements Serializable {
    String name,time,qty,medicineName;

    public Data(String name, String time, String qty, String medicineName) {
        this.name = name;
        this.time = time;
        this.qty = qty;
        this.medicineName = medicineName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
}

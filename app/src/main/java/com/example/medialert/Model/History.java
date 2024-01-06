package com.example.medialert.Model;

public class History {
    String name,time,des;

    public History(String name, String time, String des) {
        this.name = name;
        this.time = time;
        this.des = des;
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}

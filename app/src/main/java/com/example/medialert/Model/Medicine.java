package com.example.medialert.Model;

import java.io.Serializable;
import java.util.Date;

public class Medicine implements Serializable {
    private String id,userId,name,days,description,userName;
    private int hour,min;
    private boolean isRepeated;
    private boolean isEnabled;
    private boolean isTaken;

    public Medicine(String id, String userId, String name, String days, String description, String userName, int hour, int min, boolean isRepeated) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.days = days;
        this.description = description;
        this.userName = userName;
        this.hour = hour;
        this.min = min;
        this.isRepeated = isRepeated;
    }

    public Medicine() {
    }

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public boolean isRepeated() {
        return isRepeated;
    }

    public void setRepeated(boolean repeated) {
        isRepeated = repeated;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }
}

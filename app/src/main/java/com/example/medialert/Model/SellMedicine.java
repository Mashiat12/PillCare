package com.example.medialert.Model;

import java.io.Serializable;
import java.util.Date;

public class SellMedicine implements Serializable {
    private String id,name,price,description,contactNumber,userId;
    private Date createdDate;

    public SellMedicine(String id, String name, String price, String description, String contactNumber, String userId, Date createdDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.contactNumber = contactNumber;
        this.userId = userId;
        this.createdDate = createdDate;
    }

    public SellMedicine() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}

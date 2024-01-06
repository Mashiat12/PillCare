package com.example.medialert.Model;

import java.io.Serializable;
import java.util.Date;

public class Prescription implements Serializable {
    private String id,prescriptionUrlLink,uploaderUserId;
    private Date uploadDate;

    public Prescription() {
    }

    public Prescription(String id, String uploaderUserId, Date uploadDate) {
        this.id = id;
        this.uploaderUserId = uploaderUserId;
        this.uploadDate = uploadDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrescriptionUrlLink() {
        return prescriptionUrlLink;
    }

    public void setPrescriptionUrlLink(String prescriptionUrlLink) {
        this.prescriptionUrlLink = prescriptionUrlLink;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUploaderUserId() {
        return uploaderUserId;
    }

    public void setUploaderUserId(String uploaderUserId) {
        this.uploaderUserId = uploaderUserId;
    }
}

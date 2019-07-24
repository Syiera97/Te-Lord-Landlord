package com.example.te_lord_landlord.Models;

import com.google.firebase.database.ServerValue;

public class Complaint {

    private  String ComplaintKey;
    private String Title;
    private String Desc;
    private String picture;
    private String userId;
    private  String UserPhoto;
    private Object TimeStamp;

    public Complaint(String title, String desc, String picture, String userId, String userPhoto) {
        this.Title = title;
        this.Desc = desc;
        this.picture = picture;
        this.userId = userId;
        this.UserPhoto = userPhoto;
        this.TimeStamp = ServerValue.TIMESTAMP;
    }

    public Complaint() {
    }

    public String getComplaintKey() {
        return ComplaintKey;
    }

    public void setComplaintKey(String complaintKey) {
        ComplaintKey = complaintKey;
    }

    public String getTitle() {
        return Title;
    }

    public String getDesc() {
        return Desc;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public Object getTimeStamp() {
        return TimeStamp;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPhoto(String userPhoto) {
        UserPhoto = userPhoto;
    }

    public void setTimeStamp(Object timeStamp) {
        TimeStamp = timeStamp;
    }
}

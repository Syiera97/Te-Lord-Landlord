package com.example.te_lord_landlord.Models;

import android.widget.EditText;

import javax.annotation.meta.Exclusive;

public class Landlord {
    private String lname,luserPhoto, lemail,  lphone, laddress, groupID, groupPassword;

    @Exclusive private String id;
    public Landlord(){

    }

    public Landlord(EditText username, EditText userphoto, EditText userEmail, EditText userPhoneNumber, EditText userAddress, EditText groupID, EditText groupPassword){

    }

    public Landlord(String lname, String luserPhoto, String lemail,  String lphone, String laddress, String groupID, String groupPassword) {
        this.lname = lname;
        this.luserPhoto = luserPhoto;
        this.lemail = lemail;
        this.lphone = lphone;
        this.laddress = laddress;
        this.groupID = groupID;
        this.groupPassword = groupPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLuserPhoto() {
        return luserPhoto;
    }

    public void setLuserPhoto(String luserPhoto) {
        this.luserPhoto = luserPhoto;
    }

    public String getLemail() {
        return lemail;
    }

    public void setLemail(String lemail) {
        this.lemail = lemail;
    }

    public String getLphone() {
        return lphone;
    }

    public void setLphone(String lphone) {
        this.lphone = lphone;
    }

    public String getLaddress() {
        return laddress;
    }

    public void setLaddress(String laddress) {
        this.laddress = laddress;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    public void setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
    }
}

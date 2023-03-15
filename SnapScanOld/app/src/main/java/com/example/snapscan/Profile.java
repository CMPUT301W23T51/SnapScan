package com.example.snapscan;

import java.util.Date;

public abstract class Profile {
    private String username, password;
    private String name, description;
    private int qrcodes, points;
    private Date dob;
    private String gender;

    public String getName() {
        return name;
    }

    public abstract Boolean isOwner();

    public Profile(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.description = "";
        this.qrcodes = 0;
        this.points = 0;
        this.gender = "";
    }
    
    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQrcodes() {
        return qrcodes;
    }

    public void setQrcodes(int qrcodes) {
        this.qrcodes = qrcodes;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

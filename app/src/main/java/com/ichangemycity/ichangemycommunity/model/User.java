package com.ichangemycity.ichangemycommunity.model;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private boolean isVerified;
    private String name;
    private String email;
    private String phone;
    private Integer image;
    private double rating;
    private String liveLocation;
    private String timeStamp;

    public User(String id, boolean isVerified, String name, String email, String phone, Integer image, double rating, String liveLocation, String timeStamp) {
        this.id = id;
        this.isVerified = isVerified;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.rating = rating;
        this.liveLocation = liveLocation;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLiveLocation() {
        return liveLocation;
    }

    public void setLiveLocation(String liveLocation) {
        this.liveLocation = liveLocation;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", isVerified=" + isVerified +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", image=" + image +
                ", rating=" + rating +
                ", liveLocation='" + liveLocation + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

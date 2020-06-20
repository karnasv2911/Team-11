package com.kickstart.woc.wocdriverapp.model;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.libraries.places.api.model.Place;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private boolean isVerified;
    private boolean isAvailable;
    private String name;
    private String email;
    private String phone;
    private Integer image;
    private double rating;
    private Address sourceAddress;
    private Address destinationAddress;
    private Place sourcePlace;
    private Place destinationPlace;
    private String timeStamp;

    public User(String id, boolean isVerified, boolean isAvailable, String name, String email, String phone, Integer image, double rating, Address sourceAddress, Address destinationAddress, Place sourcePlace, Place destinationPlace, String timeStamp) {
        this.id = id;
        this.isVerified = isVerified;
        this.isAvailable = isAvailable;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.rating = rating;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.sourcePlace = sourcePlace;
        this.destinationPlace = destinationPlace;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
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

    public Address getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(Address sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(Address destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public Place getSourcePlace() {
        return sourcePlace;
    }

    public void setSourcePlace(Place sourcePlace) {
        this.sourcePlace = sourcePlace;
    }

    public Place getDestinationPlace() {
        return destinationPlace;
    }

    public void setDestinationPlace(Place destinationPlace) {
        this.destinationPlace = destinationPlace;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", isVerified=" + isVerified +
                ", isAvailable=" + isAvailable +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", image=" + image +
                ", rating=" + rating +
                ", sourceAddress=" + sourceAddress +
                ", destinationAddress=" + destinationAddress +
                ", sourcePlace=" + sourcePlace +
                ", destinationPlace=" + destinationPlace +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

package com.janaagraha.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	public String id;
    private boolean isVerified;
    private String name;
    private String email;
    private String phone;
    private Integer image;
    private double rating;
    private String liveLocation;
    private String timeStamp;

    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isVerified() {
		return isVerified;
	}
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
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
	public String getLiveLocation() {
		return liveLocation;
	}
	public void setLiveLocation(String liveLocation) {
		this.liveLocation = liveLocation;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}

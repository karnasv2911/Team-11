package com.ichangemycity.ichangemycommunity.model;

import com.google.android.gms.maps.model.LatLng;
import com.ichangemycity.ichangemycommunity.utils.map.SurveyDropDownEnum;

import java.io.Serializable;

public class Survey implements Serializable {

    private String id;
    private Integer image;
    private String city;
    private String location;
    private LatLng position;
    private SurveyDropDownEnum category;
    private String subCategory;
    private String qualityScore;
    private String distance;

    public Survey() {
    }

    public Survey(String id, Integer image, String city, String location, LatLng position, SurveyDropDownEnum category, String subCategory, String qualityScore, String distance) {
        this.id = id;
        this.image = image;
        this.city = city;
        this.location = location;
        this.position = position;
        this.category = category;
        this.subCategory = subCategory;
        this.qualityScore = qualityScore;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public SurveyDropDownEnum getCategory() {
        return category;
    }

    public void setCategory(SurveyDropDownEnum category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(String qualityScore) {
        this.qualityScore = qualityScore;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}

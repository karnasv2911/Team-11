package com.ichangemycity.ichangemycommunity.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Survey implements Serializable {

    private String id;
    private Integer image;
    private String city;
    private String location;
    private LatLng position;
    private String category;
    private String subCategory;
    private String qualityScore;
    private String startSurveyPoint;
    private String endSurveyPoint;

    public Survey(String id, Integer image, String city, String location, LatLng position, String category, String subCategory, String qualityScore, String startSurveyPoint, String endSurveyPoint) {
        this.id = id;
        this.image = image;
        this.city = city;
        this.location = location;
        this.position = position;
        this.category = category;
        this.subCategory = subCategory;
        this.qualityScore = qualityScore;
        this.startSurveyPoint = startSurveyPoint;
        this.endSurveyPoint = endSurveyPoint;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public String getStartSurveyPoint() {
        return startSurveyPoint;
    }

    public void setStartSurveyPoint(String startSurveyPoint) {
        this.startSurveyPoint = startSurveyPoint;
    }

    public String getEndSurveyPoint() {
        return endSurveyPoint;
    }

    public void setEndSurveyPoint(String endSurveyPoint) {
        this.endSurveyPoint = endSurveyPoint;
    }
}

package com.janaagraha.entity;

import javax.persistence.Entity;


@Entity
public class Survey {
	@javax.persistence.Id
	public long Id;
		private String city;
	    private String location;
	    private Long position;
	    private String category;
	    private String subCategory;
	    private String qualityScore;
	    private String startSurveyPoint;
	    private String endSurveyPoint;
	    private Integer image;
	public void setCity(String city) {
		this.city = city;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Long getPosition() {
		return position;
	}
	public void setPosition(Long position) {
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
	public Integer getImage() {
		return image;
	}
	 public String getCity() {
			return city;
		}
		
		public void setImage(Integer image) {
			this.image = image;
		}

}

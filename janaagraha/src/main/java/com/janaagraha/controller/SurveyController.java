package com.janaagraha.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.janaagraha.entity.Survey;
import com.janaagraha.repository.SurveyRepository;



@RestController
@RequestMapping("v1/survey")
public class SurveyController {
	@Autowired
	SurveyRepository surveyRepo;
	
	 @GetMapping
	    public List<Survey> findAll() {
	        return surveyRepo.findAll();
	    }

 @GetMapping("/{id}")
 public Survey findOne(@PathVariable Long id) {
     return surveyRepo.findByID(id);
 }
 
 @PostMapping("v1/survey")
 public Survey insertSurvey(@RequestBody Survey survey) {
	 return surveyRepo.insert(survey);
 }
 
 @PutMapping("/v1/survey")
 public Survey updateSurvey(@RequestBody Survey survey) {
	 return surveyRepo.save(survey);
 }
 
	
	

}

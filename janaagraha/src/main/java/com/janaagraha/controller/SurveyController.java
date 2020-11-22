package com.janaagraha.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.janaagraha.entity.Survey;
import com.janaagraha.exception.SurveyNotFoundException;
import com.janaagraha.repository.SurveyRepository;



@RestController
@RequestMapping("/v1")
public class SurveyController {
	@Autowired
	SurveyRepository surveyRepo;
	
	 @GetMapping("/survey")
	    public Iterable<Survey> findAll() {
	        return surveyRepo.findAll();
	    }

 @GetMapping("/survey/{id}")
 public Survey findOne(@PathVariable Long id) throws SurveyNotFoundException {
	 Optional<Survey> survey = surveyRepo.findById(id);
	 if(survey.isPresent()) {
		 return survey.get();
	 }
	 else {
		throw new SurveyNotFoundException("Survey with ID -"+id+" is not found");
	 }
 }
 
 @PostMapping("/survey")
 public Survey insertSurvey(@RequestBody Survey survey) {
	 System.out.println("Inserting survey");
	 return surveyRepo.save(survey);
 }

}

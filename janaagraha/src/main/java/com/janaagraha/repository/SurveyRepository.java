package com.janaagraha.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.janaagraha.entity.Survey;
public interface SurveyRepository extends MongoRepository<Survey, Long> {
	public Survey findByID(Long id);

}

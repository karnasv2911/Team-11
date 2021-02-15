package com.janaagraha.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.janaagraha.entity.Survey;
@Repository
public interface SurveyRepository extends CrudRepository<Survey, Long> {

}

package com.janaagraha.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.janaagraha.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User,String> {

}

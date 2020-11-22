package com.janaagraha.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.janaagraha.entity.User;
import com.janaagraha.exception.UserNotFoundException;
import com.janaagraha.repository.UserRepository;

@RestController
@RequestMapping("/v1")
public class UserController {
	
@Autowired
UserRepository userRepo;
	
@GetMapping("/user/{id}")
public User getUser(@PathVariable String id) throws UserNotFoundException {
	Optional<User> user = userRepo.findById(id);
	if(user.isPresent()) {
		
		return user.get();
	}
	else {
		throw new UserNotFoundException("User Not Found with ID "+id+" Not found");
	}
	
}
@PostMapping("/user")
public User insertUser(@RequestBody User user) {
	return userRepo.save(user);	
}




}

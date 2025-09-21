package com.example.journalApp.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.User;
import com.example.journalApp.service.JournalEntryService;
import com.example.journalApp.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public List<User> getAllUser(){
		return userService.getAll();
	}
	
	@PostMapping
	public void createUser(@RequestBody User user){
		userService.saveEntry(user);
	}
	
	@PutMapping("/{userName}")
	public ResponseEntity<?> updateUser(@RequestBody User user,@PathVariable String userName) {
		User userinDB = userService.findByUserName(userName);
		if(userinDB != null) {
			userinDB.setUserName(user.getUserName());
			userinDB.setPassword(user.getPassword());
			userService.saveEntry(userinDB);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}


	

	
}

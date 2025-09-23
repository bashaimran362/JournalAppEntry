package com.example.journalApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {
 User findByUserName(String username);

void deleteByUserName(String username);

}

package com.example.journalApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.User;
import com.example.journalApp.repository.JournalEntryRepository;
import com.example.journalApp.repository.UserRepository;

@Component
public class JournalEntryService {

	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	@Autowired
	private UserService userService;
	
	@Transactional
	public void saveEntry(JournalEntry journalEntry, String userName) {
		try {
		User  user = userService.findByUserName(userName);
		journalEntry.setDate(LocalDateTime.now());
		
		JournalEntry saved= journalEntryRepository.save(journalEntry);
		user.getJournalEntries().add(saved);
		//user.setUserName(null);  //reference if in case exception arrive then transaction will rollback the success crud operation before it
		userService.saveEntry(user);
		}
		catch(Exception e) {
			System.out.println(e);
			throw new RuntimeException("An error occurred while saving the entry",e);
		}
	
	}
		
	public void saveEntry(JournalEntry journalEntry) {
		journalEntryRepository.save(journalEntry);
	}
	
	public List<JournalEntry> getAll(){
		return journalEntryRepository.findAll();
	}
	public Optional<JournalEntry> findById(ObjectId myID) {
		return journalEntryRepository.findById(myID);
	}
	public void deleteById(ObjectId myId, String userName) {
		 User user = userService.findByUserName(userName);
		 user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
		 userService.saveEntry(user);
		 journalEntryRepository.deleteById(myId);
	}
	
}

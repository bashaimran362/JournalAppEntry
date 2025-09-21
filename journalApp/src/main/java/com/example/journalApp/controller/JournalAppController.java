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
@RequestMapping("/journal")
public class JournalAppController {

	@Autowired
	private JournalEntryService journalEntryService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/{userName}")
	public ResponseEntity<?> getAllJournalEntries(@PathVariable String userName){
		User user = userService.findByUserName(userName);
		List<JournalEntry> all = user.getJournalEntries();
		if(all != null && !all.isEmpty()) {
			return  new ResponseEntity<>(all, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/{userName}")
	public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName) {
		try {
			journalEntryService.saveEntry(myEntry, userName);
			return new ResponseEntity<>(myEntry, HttpStatus.CREATED);		}
		catch(Exception e) {
			return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("id/{myId}")
	public ResponseEntity<JournalEntry> getJournalEntryByid(@PathVariable ObjectId myId ) {
		Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
		//if(journalEntry.isPresent()) {
			//return new ResponseEntity<>(journalEntry.get(),HttpStatus.OK ); 
		//}
		//return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
		
		return  journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@DeleteMapping("id/{userName}/{myId}")
	public ResponseEntity<?> deleteJournalEntryByid(@PathVariable ObjectId myId, @PathVariable String userName ) {
		 journalEntryService.deleteById(myId, userName);
		 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/id/{userName}/{myId}")
	public ResponseEntity<?> updateJournalEntryByid(@PathVariable ObjectId myId, @RequestBody JournalEntry newEntry, @PathVariable String userName ) {
		JournalEntry old = journalEntryService.findById(myId).orElse(null);
		if(old!=null) {
			old.setTitle(newEntry.getTitle()!= null && ! newEntry.equals("")? newEntry.getTitle():old.getTitle());
			old.setContent(newEntry.getContent()!=null && ! newEntry.equals("")? newEntry.getContent():old.getContent());
			journalEntryService.saveEntry(old);
			return new ResponseEntity<>(old, HttpStatus.OK);
		}
	//	journalEntryService.saveEntry(old);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}

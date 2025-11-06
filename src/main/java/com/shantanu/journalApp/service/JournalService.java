package com.shantanu.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import com.shantanu.journalApp.dto.JournalEntryDTO;
import com.shantanu.journalApp.entity.JournalEntry;
import com.shantanu.journalApp.entity.User;
import com.shantanu.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class JournalService {

    @Autowired // Dependency Injection
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional // to make it commit or rollback(if any problem occcurs)
    public JournalEntry saveEntry(JournalEntryDTO newEntryDto, String userName) {
        try {
            User user = userService.findByUserName(userName);
            if(user == null) throw new RuntimeException("User not found");

            JournalEntry newEntry = JournalEntry.builder()
                            .title(newEntryDto.getTitle())
                            .content(newEntryDto.getContent())
                            .date(LocalDateTime.now())
                            .sentiment(newEntryDto.getSentiment())
                            .build();
            JournalEntry savedEntry = journalEntryRepository.save(newEntry);

            // here we take reference of journalentry savedEntry variable and insert in user's List to sync.
            user.getJournalEntryList().add(savedEntry);
            userService.saveUser(user);
            return savedEntry;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the entry : "+e);
        }
    }

    @Transactional
    // method overloaded for update API
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public ArrayList<JournalEntry> getAllEntries() {
        return new ArrayList<>(journalEntryRepository.findAll());
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntryList().removeIf(x -> x.getId().equals(id));
            if(removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            log.error("error : ",e);
            throw new RuntimeException("Exception occured while deleting the entry" + e);
        }
        return removed;
    }
}

// controller --> service --> repository --> Entity(POJO)
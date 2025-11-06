package net.engineeringdigest.journalApp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.engineeringdigest.journalApp.dto.JournalEntryDTO;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@Tag(name = "Journal APIs", description = "Create, Read, Update, Delete, Journal APIs")
public class JournalEntryController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> allEntries = user.getJournalEntryList();
        if (allEntries != null && !allEntries.isEmpty()) {
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> findJournalByID(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntryList()
                .stream()
                .filter(x -> x.getId().equals(myId))
                .collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalService.findById(myId);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntryDTO newEntryDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            JournalEntry saved = journalService.saveEntry(newEntryDto, userName);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId myId) { // get username from auth
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean removed = journalService.deleteById(myId, userName);
        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<JournalEntry> updateJournalByID(@PathVariable ObjectId myId,
                                                          @RequestBody JournalEntryDTO updatedEntryDto) {
        // Get authenticated username
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUserName(userName);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Find journal entry in user's list
        Optional<JournalEntry> optionalEntry = user.getJournalEntryList()
                .stream()
                .filter(j -> j.getId().equals(myId))
                .findFirst();

        if (optionalEntry.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        JournalEntry oldEntry = optionalEntry.get();

        // Update only if values provided
        if (updatedEntryDto.getTitle() != null && !updatedEntryDto.getTitle().isEmpty()) {
            oldEntry.setTitle(updatedEntryDto.getTitle());
        }
        if (updatedEntryDto.getContent() != null && !updatedEntryDto.getContent().isEmpty()) {
            oldEntry.setContent(updatedEntryDto.getContent());
        }

        if (updatedEntryDto.getSentiment() != null) {
            oldEntry.setSentiment(updatedEntryDto.getSentiment());
        }

        // Save updated journal entry
        journalService.saveEntry(oldEntry);
        return new ResponseEntity<>(oldEntry, HttpStatus.OK);
    }



//    @GetMapping("/all")
//    public ResponseEntity<?> getAllJournalEntries() {
//        ArrayList<JournalEntry> allEntries = journalService.getAllEntries();
//        if (allEntries != null && !allEntries.isEmpty()) {
//            return new ResponseEntity<>(allEntries, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(allEntries, HttpStatus.NO_CONTENT);
//    }


}

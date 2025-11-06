package com.shantanu.journalApp.serviceTests;

import com.shantanu.journalApp.dto.JournalEntryDTO;
import com.shantanu.journalApp.entity.JournalEntry;
import com.shantanu.journalApp.entity.User;
import com.shantanu.journalApp.repository.JournalEntryRepository;
import com.shantanu.journalApp.service.JournalService;
import com.shantanu.journalApp.service.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
class JournalServiceTest {

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private JournalService journalService;

    @Test
    void testSaveEntry_AddsToUserJournalList() {
        User user = new User();
        user.setUserName("Ram");
        user.setJournalEntryList(new ArrayList<>());

        JournalEntryDTO dto = new JournalEntryDTO();
        dto.setTitle("Title");
        dto.setContent("Content");

        when(userService.findByUserName("Ram")).thenReturn(user);

        JournalEntry savedEntry = new JournalEntry();
        savedEntry.setTitle(dto.getTitle());
        savedEntry.setContent(dto.getContent());

        when(journalEntryRepository.save(any(JournalEntry.class))).thenReturn(savedEntry);
        doNothing().when(userService).saveUser(user);

        journalService.saveEntry(dto, "Ram");

        assertTrue(user.getJournalEntryList().contains(savedEntry));
        verify(journalEntryRepository, times(1)).save(any(JournalEntry.class));
        verify(userService, times(1)).saveUser(user);
    }
}

package com.shantanu.journalApp.serviceTests;

import com.shantanu.journalApp.entity.User;
import com.shantanu.journalApp.repository.UserRepository;
import com.shantanu.journalApp.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@Disabled
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository; // Mock(fake) repository (no DB, no Spring context)

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService; // Injects mock into service manually

    @BeforeEach
    void setUp() {
        // Spring Context ke absence me objects create nahi hote → service/repo null
        // Fix: BeforeEach me init kiya
        MockitoAnnotations.initMocks(this); // Ye line @Mock aur @InjectMocks objects ko initialize karti hai
    }

    @Test
    void loadUserByUsernameTest() {
        // Mocking repository behavior: whenever findByUserName() is called with any String,
        // return a dummy User entity built using Lombok's @Builder
        when(userRepository.findByUserName(ArgumentMatchers.anyString()))
                .thenReturn(User.builder()
                        .userName("ram")
                        .password("sdawkkjas")
                        .roles(new ArrayList<>())
                        .build());

        // Calling the actual service method to test its logic
        UserDetails user = userDetailsService.loadUserByUsername("Ram");

        // Assertion to verify that the service successfully returned a UserDetails object
        Assertions.assertNotNull(user);
        Assertions.assertEquals("ram", user.getUsername());
    }

}

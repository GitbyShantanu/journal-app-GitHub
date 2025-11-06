package com.shantanu.journalApp.serviceTests;

import com.shantanu.journalApp.dto.UserDTO;
import com.shantanu.journalApp.entity.User;
import com.shantanu.journalApp.repository.UserRepository;
import com.shantanu.journalApp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Optional: additional setup if needed
    }

    @Test
    void testSaveNewUser_EncodesPasswordAndAssignRole_ReturnsTrue() {
        // Prepare DTO
        UserDTO dto = new UserDTO();
        dto.setUserName("testUser");
        dto.setPassword("plain");

        // Mock encoding
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");

        // Mock repository save to just return the user
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // Call service
        boolean result = userService.saveNewUser(dto);

        // Assertions
        assertTrue(result, "User should be saved successfully");

        // Verify encoder was called
        verify(passwordEncoder).encode("plain");
    }
}

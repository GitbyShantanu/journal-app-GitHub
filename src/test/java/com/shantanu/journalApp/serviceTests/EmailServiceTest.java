package com.shantanu.journalApp.serviceTests;

import com.shantanu.journalApp.service.EmailService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendEmail() {
        emailService.sendEmail(
                "cpshantanu5@gmail.com",
                "Testing Java mail sender",
                "Hello, Kasa ahes tu ? Happy Diwali....! "
        );
    }
}

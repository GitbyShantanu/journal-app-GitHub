package com.shantanu.journalApp;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class MyFirstTest {
    @Test
    void myFirstTestMethod() {
        // Arrange
        int a = 5, b = 3;
        // Act
        int sum = a + b;
        // Assert
        assertEquals(8, sum);
    }

    @BeforeAll
    static void setup() {
        System.out.println("JUnit Testing started....");
    }

    @BeforeEach
    void setup2() {
        System.out.println("Setting up before each test...");
    }

    @Test
    void arithmetic() {
        assertEquals(10, 5+5);
    }

    @Test
    @CsvSource("shan")
    void stringNotNullTest() {
        String str = "shan";
        assertNotNull(str);
    }

    @AfterEach
    void teardown() {
        System.out.println("Cleaning up after each test");
    }

}

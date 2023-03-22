package com.example.SnapScan;

import static org.junit.Assert.assertTrue;

import com.github.javafaker.Faker;

import org.junit.Test;


public class QRTesting {
    // Test to check if the QR code name is generated correctly
    @Test
    public void fakerNameTest() {
        Faker faker = new Faker();
        String name = faker.ancient().god();
        assertTrue(name.length() > 0);
    }
}

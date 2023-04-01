package com.example.SnapScan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.SnapScan.model.QRcode;
import com.github.javafaker.Faker;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class QRcodeTest {

    private static QRcode qrcode;

    @BeforeAll
    static void setUp() {
        qrcode = new QRcode("test");
    }

    // Test to check if the QR code name is generated correctly
    @Test
    public void fakerNameTest() {
        Faker faker = new Faker();
        String name = faker.ancient().god();
        assertTrue(name.length() > 0);
    }
    // Testing all setters and getters for Firebase
    @Test
    public void testSetters() {
        QRcode qrcodeSetter = new QRcode();
        qrcodeSetter.setHash("test");
        qrcodeSetter.setName("test");
        qrcodeSetter.setPoints(10);
        qrcodeSetter.setResult("test");
        qrcodeSetter.setImageURL("test");
        qrcodeSetter.setGeoPointWithLatLong(10.0, 10.0);
        assertEquals("test", qrcodeSetter.getHash());
        assertEquals("test", qrcodeSetter.getName());
        assertEquals(10, qrcodeSetter.getPoints());
        assertEquals("test", qrcodeSetter.getResult());
        assertEquals("test", qrcodeSetter.getImageURL());
        assertEquals(10.0, qrcodeSetter.getGeoPoint().getLatitude());
        assertEquals(10.0, qrcodeSetter.getGeoPoint().getLongitude());
        qrcodeSetter = null;
    }

    // Test to check if the QR code hash is generated correctly
    @Test
    // for original value site used : https://xorbin.com/tools/sha256-hash-calculator
    public void testHash() {
        String hash = qrcode.getHash();
        assertTrue(hash.length() > 0);
        assertEquals(hash, "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08");

    }

    // Test to check if the QR code name is generated correctly
    @Test
    public void testGetName() {
        String name = qrcode.getName();
        Assertions.assertNotNull(name);
        assertTrue(name.length() > 0);
    }

    @Test
    // if the QR code result contains group member name
    // then the points should be more than other QR codes
    public void testGetPoints() {
        Assertions.assertTrue(qrcode.getPoints() >= 0);
        QRcode groupMemberTest = new QRcode("Suvan");
        Assertions.assertTrue(groupMemberTest.getPoints() >= 0);
        int inflatedScore = qrcode.getPoints() * 10;
        assertTrue(groupMemberTest.getPoints() > inflatedScore);
        System.out.println("Group Member: " + groupMemberTest.getPoints());
        System.out.println("Score: " + inflatedScore);
        System.out.println("Original: " + qrcode.getPoints());

    }
    // Test to check if the QR code image URL is generated correctly
    @Test
    public void testImageURL() {
        // Regex to check if the URL is valid
        String regex = "^(https?|ftp)://[a-z\\d-]+(\\.[a-z\\d-]+)+([/?].*)?$";
        String url = qrcode.getImageURL();
        assertTrue(url.matches(regex));
    }

    @Test
    public void testGetResult() {
        Assertions.assertEquals("test", qrcode.getResult());
    }

    @AfterAll
    static void tearDown() {
        qrcode = null;
    }
}


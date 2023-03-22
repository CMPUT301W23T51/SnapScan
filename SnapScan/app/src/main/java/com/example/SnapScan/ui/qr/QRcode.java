package com.example.SnapScan.ui.qr;

import androidx.annotation.NonNull;

import com.github.javafaker.Faker;
import com.google.firebase.firestore.GeoPoint;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class to represent a QR codes
 */
public class QRcode {

    // Static variable to create a unique image QR codes created
    // https://picsum.photos is used to generate random images which requires the image seed
    private static int imageSeed = 0;
    private final String result;
    private String hash;
    private String name;
    private int points;
    private GeoPoint geoPoint;

    public QRcode(String result) {
        this.result = result;
        setHash(result);
        setName();
        setPoints();
        imageSeed++;
    }

    /**
     * Get the string Value of image seed to be used in the QR code image
     * returning as string because the image seed is used in the URL
     *
     * @return String value of Image Seed
     */
    public static String getImageSeed() {
        return String.valueOf(imageSeed);
    }

    public String getHash() {
        return hash;
    }

    // Getter and Setter for hash
    private void setHash(String result) {
        getHash256(result);
    }


    // Getter and Setter for name

    /**
     * Set the name of the QR code using a random name generator from the Faker library
     *
     * @see <a href="faker.readthedocs.io">Faker</a>
     */
    private void setName() {
        Faker faker = new Faker();
        this.name = faker.ancient().god();
    }

    public String getName() {
        return name;
    }

    // Getter and Setter for points
    public int getPoints() {
        return this.points;
    }

    private void setPoints() {
        // Get and set points
        this.points = calculateScore();
    }

    // Getter and Setter for GeoPoint
    // Setters for GeoPoint is public as we will set it after asking for permission
    public void setgeoPoint(Double latitude, Double longitude) {
        this.geoPoint = new GeoPoint(latitude, longitude);
    }

    public GeoPoint getgeoPoint() {
        return this.geoPoint;
    }


    /**
     * Calculate the Hash 256 value of the QR code and set the hash value
     *
     * @param result the result(data) of the QR code
     * @see <a href="https://www.baeldung.com/sha-256-hashing-java">Baeldung</a>
     */
    private void getHash256(@NonNull String result) {
        String hash256;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(result.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashHex = new StringBuilder(2 * hashBytes.length);
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hashHex.append('0');
                }
                hashHex.append(hex);
            }
            hash256 = hashHex.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Use SHA-256 message digest algorithm");
            hash256 = Integer.toHexString(result.hashCode());
        }
        this.hash = hash256;
    }

    /**
     * Calculate the score of the QR code
     * if the name of the QR code is a member of the group, the score is multiplied by 1000
     * else the score is multiplied by 10
     *
     * @return the score of the QR code
     */
    private int calculateScore() {
        String hex = this.hash;
        String[] members = {"Suvan", "Varun", "Anant", "Prabhjot", "Rechal", "Ruilin"};
        int score = 0;
        for (int i = 0; i < hex.length(); i++) {
            score += Character.getNumericValue(hex.charAt(i));
        }
        if (ArrayUtils.contains(members, this.result)) {
            score *= 1000;
        } else {
            score *= 10;
        }
        return Math.round(score);
    }


    public String getResult() {
        return this.result;
    }
}

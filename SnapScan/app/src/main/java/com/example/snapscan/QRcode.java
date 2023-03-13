package com.example.snapscan;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

//unique name
//visual representation
//photo
//description

// References: SHA-256 hashing from GeeksforGeeks - https://www.geeksforgeeks.org/sha-256-hash-in-java/

public class QRcode {
    String name;
    String unique_name;
    String geolocation;
    String description;
    Integer points;
    String hashno;

    String latitude;
    String longitude;
    String city_name;
    String country_name;

    // constructor
    public QRcode(String name, String location){
        this.name = name;
        this.geolocation = location;
        this.points = this.name.length();
    }

    public String getName() {
        return this.name;
    }

    public String getGeolocation() {
        //this.geolocation = this.latitude.toString() + this.longitude.toString() + this.city_name.toString() + this.country_name.toString();
        //return this.country_name;
        return this.geolocation;
    }

    // user will input their description in the dialouge box
    public void setDescription(String description) {
        this.description = description;
    }

    // returns points associated with a specific qr code
    public Integer getPoints() {
        return this.points;
    }

    public void setGeolocation(String lat, String lon, String city, String country) {
        this.latitude = String.valueOf(lat);
        this.longitude = String.valueOf(lon);
        this.city_name = city;
        this.country_name = country;

        this.geolocation = this.latitude.toString() + this.longitude.toString() + this.city_name.toString() + this.country_name.toString();
    }

    public static byte[] HashCode(String code) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        return messageDigest.digest(code.getBytes(StandardCharsets.UTF_8));
    }

    public static String HexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger num = new BigInteger(1, hash);
        // Convert message digest into hex value
        StringBuilder hexstr = new StringBuilder(num.toString(16));
        // Pad with leading zeros
        while (hexstr.length() < 32) {
            hexstr.insert(0, '0');
        }
        return hexstr.toString();
    }


    public String getHashno() throws NoSuchAlgorithmException {
        this.hashno = HexString(HashCode(this.name));
        return this.hashno;
    }

    public String getUnique_name() {
        String ustr = "";
        for (char c : this.name.toCharArray()){
            Integer asc = (int) c;
            if ((asc > 96 && asc < 123) || (asc > 64 && asc < 91)){
                ustr = ustr + c;
            }
        }
        if (ustr == ""){
            Random r = new Random();
            char a = (char)(r.nextInt(26) + 'a');
            char b = (char)(r.nextInt(26) + 'a');
            char c = (char)(r.nextInt(26) + 'a');
            ustr = String.valueOf(a + b + c);
        }
        this.unique_name = ustr;
        return this.unique_name;
    }
}

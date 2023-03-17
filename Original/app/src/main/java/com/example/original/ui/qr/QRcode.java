package com.example.original.ui.qr;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class QRcode {

    private Context context;

    String name;
    String unique_name;
    String geolocation;
    String description;
    Integer points;
    String hashno;

    String latitude, longitude;
    String city_name, country_name;

    public QRcode(Context context) {
        this.context = context;
    }

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

    public void setGeolocation(Double lat, Double lon, String city, String country) {
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

    public Location currentLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
}

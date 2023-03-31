package com.example.SnapScan.model;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.github.javafaker.Faker;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Class to represent a QR codes
 */
public class QRcode {

    private String result;
    private String hash;
    private String name;
    private int points;
    private GeoPoint geoPoint;
    private String imageURL;


    // Constructor for QR code which sets the hash, name, points, image seed and image URL
    public QRcode(String result) {
        this.result = result;
        setHash(result);
        setName();
        setPoints();
        setImageURL();
    }

    //Empty constructor for Firebase
    public QRcode() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void loadImage(ImageView imageView) {
        // Using Picasso to load the image from the URL
        try {
            Picasso.get()
                    .load(getImageURL())
                    .into((ImageView) imageView);
        } catch (Exception e) {
            // Should display app Icon if the QR code does not have an image
            Log.d(TAG, "Unable to Fetch Visual Representation : " + e.getMessage());
        }
    }

    /**
     * Set the image URL of the QR code
     * The image URL is generated using the image seed
     *
     * @see <a href="https://picsum.photos">Picsum</a>
     * @see <a href="https://stackoverflow.com/questions/20389890/generating-a-random-number-between-1-and-10-java">...</a>
     */
    private void setImageURL() {
        // Generate a random number between 1 and 100 for image seed
        int imageSeed = new Random().nextInt(100);
        this.imageURL = "https://picsum.photos/seed/" + imageSeed + "/400/275";
        //Width/Height
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
     * see <a href="https://commons.apache.org/proper/commons-lang/javadocs/api-3.4/org/apache/commons/lang3/ArrayUtils.html">ArrayUtils</a>
     * see <a href="https://stackoverflow.com/questions/1128723/how-do-i-determine-whether-an-array-contains-a-particular-value-in-java">StackOverflow</a>
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

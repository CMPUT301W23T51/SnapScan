package com.example.snapscan;

public class UserDetails {
    public String name;
    public static String dob;
    public static String gender;
    public static String phone;

    public UserDetails(String textName, String textDob, String textGender, String textPhone){
        this.name = textName;
        this.dob = textDob;
        this.gender = textGender;
        this.phone = textPhone;

    }

}

package com.example.snapscan;

public class Owner extends Profile{

    @Override
    public Boolean isOwner(){
        return true;
    }

    public Owner(String username, String password, String name) {
        super(username, password, name);
    }

    // additional functions for owner
    // ...
}

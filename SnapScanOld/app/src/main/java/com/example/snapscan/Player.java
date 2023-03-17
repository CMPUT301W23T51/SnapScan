package com.example.snapscan;

public class Player extends Profile{

    @Override
    public Boolean isOwner(){
        return false;
    }

    public Player(String username, String password, String name) {
        super(username, password, name);
    }

}

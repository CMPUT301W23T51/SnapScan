package com.example.snapscan;

public class User {

    String name;
    long score;

    public User() {}

    public User(String name, long score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public long getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(long score) {
        this.score = score;
    }
}

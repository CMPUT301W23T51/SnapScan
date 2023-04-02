package com.example.SnapScan.ui.profile;
/**

        A class representing a score object containing a player's name, points, and rank.
        */
public class Score {
    private String mName;
    private Long mPoints;
    private int rank;
    /**

     Constructs a new Score object with the specified name and points.
     @param name the player's name.
     @param points the player's points as a String.
     */
    public Score(String name, String points) {}
/*
    public Score(String name, String points) {
        mName = name;
        mPoints = Long.valueOf(points);
    }
*/

    public Score(String name, Long points) {
        mName = name;
        mPoints = points;
    }

    public String getName() {
        return mName;
    }

    public Long getPoints() {
        return mPoints;
    }
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}

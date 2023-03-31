package com.example.SnapScan.ui.profile;

public class Score {
    private String mName;
    private Long mPoints;
    private int rank;
    public Score() {}

    public Score(String name, String points) {
        mName = name;
        mPoints = Long.valueOf(points);
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

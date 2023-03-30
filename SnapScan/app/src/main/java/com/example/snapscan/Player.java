package com.example.snapscan;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * This is a class that keeps track of a player
 */

public class Player implements Parcelable {

    private String username;
    private String profileCode;
    private ArrayList<String> gameCodeList;
    private int totalPoints;
    private int totalGameCode;
    private int maxGameCodePoints;
    private int displayTotal;

    /**
     * empty constructor for firestore operations
     */
    public Player() {
        this.gameCodeList = new ArrayList<>();
    }

    /**
     * constructs a player with a given username
     * @param username
     *      the username of the player that is being constructed
     */
    public Player(String username) {
        this.username = username;
        this.gameCodeList = new ArrayList<>();
    }

    /**
     * contructs a player from parcelable
     * @param in data from parcel
     */
    protected Player(Parcel in) {
        username = in.readString();
        profileCode = in.readString();
        gameCodeList = in.createStringArrayList();
        totalPoints = in.readInt();
        totalGameCode = in.readInt();
        maxGameCodePoints = in.readInt();
        displayTotal = in.readInt();
    }


    /**
     * creates a player from parcelable
     */
    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    /**
     * Gets the players username
     * @return
     *      Returns the string representing the players username
     */
    public String getUsername(){
        return this.username;
    }


    /**
     * Gets the players profile code
     * @return
     *      Returns the string representing the players profile code
     */
    public String getProfileCode() {
        return this.profileCode;
    }

    /**
     * gets the players game code list
     * @return
     *      Returns the list of GameCodes
     */
    public ArrayList<String> getGameCodeList() {
        return this.gameCodeList;
    }

    /**
     * gets the players total points
     * @return
     *      Returns the int representing the players total points
     */
    public int getTotalPoints() {
        return this.totalPoints;
    }

    /**
     * gets the players total game code
     * @return
     *      Returns the int representing the players total game code
     */
    public int getTotalGameCode() {
        return this.totalGameCode;
    }

    /**
     * gets the players max game code points
     * @return
     *      Returns the int representing the players max game code points
     */
    public int getMaxGameCodePoints() {
        return this.maxGameCodePoints;
    }

    /**
     * adds a gameCode to the players gameCodeList
     * @param gameCode
     *      this is a game code to add to gameCodeList
     */
    public void addGameCode(String gameCode) {
        this.gameCodeList.add(gameCode);
    }

    /**
     * removes a gameCode from the players gameCodeList
     * @param gameCode
     *      this is a game code to remove from gameCodeList
     */
    public void removeGameCode(String gameCode) {
        this.gameCodeList.remove(gameCode);
    }

    /**
     * sets the players total points to a provided value
     * @param points
     *      this is the int value that total points will be set to
     */
    public void setTotalPoints(int points) {
        this.totalPoints = points;
    }

    /**
     * sets the total game code
     * @param total
     *      this is the int value that total game code will be set to
     */
    public void setTotalGameCode(int total) {
        this.totalGameCode = total;
    }

    /**
     * sets the max game code points
     * @param points
     *      this is the int value that max game code points will be set to
     */
    public void setMaxGameCodePoints(int points) {
        this.maxGameCodePoints = points;
    }

    /**
     * gets the points that are to be displayed
     * @return
     *      returns the int points
     */
    public int getDisplayTotal() {
        return this.displayTotal;
    }

    /**
     * sets the points that are to be displayed
     * @param points
     *      the points that are set
     */
    public void setDisplayTotal(int points) {
        this.displayTotal = points;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * to make player parcelable
     * @param parcel the parcel that is to be written to
     * @param i an integer, not used
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(profileCode);
        parcel.writeStringList(gameCodeList);
        parcel.writeInt(totalPoints);
        parcel.writeInt(totalGameCode);
        parcel.writeInt(maxGameCodePoints);
        parcel.writeInt(displayTotal);
    }
}
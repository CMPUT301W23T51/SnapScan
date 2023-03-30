package com.example.snapscan;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;


/**
 * this is a controller class for a list of players
 */
public class PlayerList {

    private ArrayList<Player> playerList;


    public PlayerList() {
        this.playerList = new ArrayList<>();
    }

    /**
     * sorts the list of players in descending order based on the players highest QR score
     *
     */
    public void sortByQRScore() {
        Collections.sort(playerList, (a, b) -> {
            if (a.getMaxGameCodePoints() > b.getMaxGameCodePoints()) {
                return -1;
            }
            else if (a.getMaxGameCodePoints() < b.getMaxGameCodePoints()) {
                return 1;
            }
            else {
                return 0;
            }
        });
    }

    /**
     * sorts the list of players in descending order based on which player has scanned the most QR codes
     */
    public void sortByMostQR() {
        Collections.sort(playerList, (a, b) -> {
            if (a.getTotalGameCode() > b.getTotalGameCode()) {
                return -1;
            }
            else if (a.getTotalGameCode() < b.getTotalGameCode()) {
                return 1;
            }
            else {
                return 0;
            }
        });

    }

    /**
     * sorts the list of players in descending order based on the total points of the players
     */
    public void sortByTotalPoints() {
        Collections.sort(playerList, (a, b) -> {
            if (a.getTotalPoints() > b.getTotalPoints()) {
                return -1;
            } else if (a.getTotalPoints() < b.getTotalPoints()) {
                return 1;
            } else {
                return 0;
            }
        });
    }


    /**
     * adds a player to the player list
     * @param player
     *      the player to be added
     */
    public void addPlayerList(Player player) {
        this.playerList.add(player);

    }

    /**
     * gets the size of player list
     * @return
     *      returns the size in the form of an int
     */
    public int getSize() {
        return this.playerList.size();
    }


    /**
     * gets a player at an index i
     * @param i
     *      the index that the player is located at
     * @return
     *      returns the player at index i
     */
    public Player getPlayer(int i) {
        return this.playerList.get(i);
    }


    /**
     * gets the list of players
     * @return
     *      returns an arrayList of that contains all the players
     */
    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }

    /**
     * gets the position of a player in the player list based on username
     * @param username
     *      this is the username of the player that we are looking for
     * @return
     *      returns the index that the player is located at, if not found return -1
     */
    public int findPlayerPos(String username) {

        for (int i =0; i < playerList.size(); i++) {

            if (playerList.get(i).getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * gets the position of a player in the player list
     * @param player
     *      the player to be located
     * @return
     *      the int index of the player
     */
    public int indexOfPlayer(Player player) {
        return this.playerList.indexOf(player);
    }






}
package com.example.SnapScan.ui.profile;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Player {

    String player;
    Integer points;
    FirebaseFirestore db;

    String qrs = null;
    String tpoints = null;

    public Player(String email, Integer points){
        this.player = email;
        this.points = points;
    }

    public void update(){

        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(this.player);
        Map<String , Object> user_newdata = new HashMap<>();


        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        user_newdata.put("qr's scanned", String.valueOf( Integer.valueOf(document.getString("qr's scanned")) + 1 ));
                        user_newdata.put("total points", String.valueOf( Integer.valueOf(document.getString("total points")) + points ));

                        //qrs = document.getString("qr's scanned");
                        //tpoints = document.getString("total points");
                        Log.d(TAG, "The user's name is " + qrs);
                    } else {
                        Log.d(TAG, "No such document!");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //user_newdata.put("qr's scanned", String.valueOf(scanned_qrs[0] + 1));
        //user_newdata.put("total points", String.valueOf(tpoints[0] + this.points));

        documentReference.update(user_newdata)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Document updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Failed to update document: " + e.getMessage());
                    }
                });

    }


}

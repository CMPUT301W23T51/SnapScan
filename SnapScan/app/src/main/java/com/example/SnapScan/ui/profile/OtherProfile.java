package com.example.SnapScan.ui.profile;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.SnapScan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
/**

 OtherProfile class displays the profile of a user selected from the leaderboard.

 The class retrieves user data from Firestore and displays it in the TextViews.
 */

public class OtherProfile extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String username;
    private TextView mNameTextView;
    private TextView mPointsTextView;
    /**

     onCreate method is called when the activity is created.

     It retrieves the username passed from the leaderboard activity and displays the user data in the TextViews.

     @param savedInstanceState Bundle object containing the activity's previously saved state.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        // Get the username passed from the leaderboard activity
        username = getIntent().getStringExtra("username");

        // Find the TextViews in the layout file
        mNameTextView = findViewById(R.id.user_name);
        mPointsTextView = findViewById(R.id.user_points);

        // Retrieve user data from Firestore and display it in the TextViews
        db.collection("QR")
                .whereEqualTo("Name", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Set the name and points TextViews to the values retrieved from Firestore
                                mNameTextView.setText(document.getString("Name"));
                                mPointsTextView.setText(document.getString("Points"));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
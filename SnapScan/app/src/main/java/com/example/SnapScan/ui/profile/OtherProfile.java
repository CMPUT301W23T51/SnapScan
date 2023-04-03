package com.example.SnapScan.ui.profile;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class OtherProfile extends Activity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String username;
    private TextView mNameTextView;
    private TextView mPointsTextView;
    private TextView mNumberofQRTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        // Get the username passed from the leaderboard activity
        username = getIntent().getStringExtra("username");
        // Find the TextViews in the layout file
        mNameTextView = findViewById(R.id.player_name);
        mPointsTextView = findViewById(R.id.total_points_int);
        mNumberofQRTextView = findViewById(R.id.qr_count_int);

        // Retrieve user data from Firestore and display it in the TextViews
        db.collection("users")
                .whereEqualTo("name", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Set the name and points TextViews to the values retrieved from Firestore
                                mNameTextView.setText(document.getString("name"));
                                mPointsTextView.setText( String.valueOf(document.getLong("TotalPoints")) );
                                mNumberofQRTextView.setText(String.valueOf(document.getLong("QrScanned")));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
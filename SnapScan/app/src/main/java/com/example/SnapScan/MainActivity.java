package com.example.SnapScan;

import static android.content.ContentValues.TAG;
import static com.example.SnapScan.ui.profile.QRListFragment.dataLoaded;
import static com.example.SnapScan.ui.profile.QRListFragment.userQrList;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.SnapScan.databinding.ActivityMainBinding;
import com.example.SnapScan.model.QRcode;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        populateUserQRList();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_profile, R.id.navigation_qr, R.id.navigation_map)
                .build();

        // Alternatively, you can use the Navigation to get the NavController
    // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    /**
     * This method will populate the qrList with the qr codes that the user has scanned
     */
    public void populateUserQRList() {
        // Get a Fire store instance
        db = FirebaseFirestore.getInstance();
        ArrayList<String> qrHashList = new ArrayList<>();
        db.collectionGroup("Scanned QRs").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Log the Success
                        Log.d("QRListFragment", "Successfully retrieved the list of qr codes that the user has scanned");
                        // Get the list of qr codes that the user has scanned
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            qrHashList.add(document.getId());
                        }
                        _addToQRList(qrHashList, db);
                    }
                });
    }

    private void _addToQRList(ArrayList<String> qrHashList, FirebaseFirestore db) {
        for (String qrHash : qrHashList) {
            DocumentReference docRef = db.collection("QR").document(qrHash);
            docRef.get().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Failed to get QR Code as it does not exist in the database");
                }
            }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    QRcode qrCode = documentSnapshot.toObject(QRcode.class);
                    userQrList.add(qrCode);
                    Log.d(TAG, "Successfully added QR code to the list");
                    dataLoaded = true;
                }
            });
        }
    }
}
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Good Site for Splash Screen:I used this site to create the splash screen.
        //https://proandroiddev.com/splash-screen-in-android-3bd9552b92a5#6e92
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // We are populating the qrList in the MainActivity as it reduces the loading time
        // of the QRListFragment
        // https://stackoverflow.com/questions/68023340/custom-object-retrieved-from-firebase-always-has-null-attributes

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
     * * @helperMethod _addToQRList
     */
    public void populateUserQRList() {
        //TODO: Change this to the user
        String user = "suvan5@gmail.com";

        // Get a Fire store instance
        db = FirebaseFirestore.getInstance();
        ArrayList<String> qrHashList = new ArrayList<>();
        CollectionReference collectionReference = db.collection("users").document(user).collection("Scanned QRs");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Log the Success
                        Log.d(TAG, "Successfully retrieved the list of QR codes that the user has scanned");
                        // Get the list of qr codes that the user has scanned
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            qrHashList.add(document.getId());
                        }
                        _addToQRList(qrHashList, db);
                    }
                });
    }

    /**
     * This method will add the qr codes to the qrList
     *
     * @param qrHashList the list of qr hashes that the user has scanned
     * @param db         the FireStore instance
     */
    private void _addToQRList(ArrayList<String> qrHashList, FirebaseFirestore db) {
        // For each qr hash in the list, get the qr code from the database
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
                    // Convert the hash to a QR code object
                    QRcode qrCode = documentSnapshot.toObject(QRcode.class);
                    userQrList.add(qrCode);
                    Log.d(TAG, "Successfully added QR code to the list");
                    dataLoaded = true;
                }
            });
        }
    }
}
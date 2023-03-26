package com.example.SnapScan.ui.qr;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;
import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.SnapScan.R;
import com.example.SnapScan.model.QRcode;
import com.example.SnapScan.ui.profile.ProfileFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * This fragment is used to display the data from the QR code
 * The user can also take a picture of the QR code
 * and saved the location of the QR code
 */

public class PostScanFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private final int CAMERA_REQUEST_CODE = 103;
    FusedLocationProviderClient fusedLocationProviderClient;
    QRcode scannedQrCode;
    String QRcodeName;
    FirebaseFirestore db;

    // need these variables afterwards
    EditText comment;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post_scan, container, false);


        // Check if location permission is already granted
        if (ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Location permission is not granted yet, request it
            ActivityCompat.requestPermissions(requireActivity(), new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        // Receive the result from ScanQRFragment
        getParentFragmentManager().setFragmentResultListener("dataFromQR", this, (requestKey, result) -> {
            String data = result.getString("Scanned Data");
            scannedQrCode = new QRcode(data);


            // Using Picasso to load the image from the URL
            try {
                Picasso.get()
                        .load(scannedQrCode.getImageURL())
                        .into((ImageView) root.findViewById(R.id.imageViewQrCode));
            } catch (Exception e) {
                // Should display app Icon if the QR code does not have an image
                Log.d(TAG, "Unable to Fetch Visual Representation : " + e.getMessage());
            }


            //Setting up the  views to display the data
            comment = root.findViewById(R.id.editTextTextPersonName); // comment stores the edittext entered by the user

            TextView QR_score = root.findViewById(R.id.qr_score_text);
            TextView QR_name = root.findViewById(R.id.qr_name_text);
            TextView QR_result = root.findViewById(R.id.qr_result_text);
            QR_score.setText(String.valueOf(scannedQrCode.getPoints()));
            QR_name.setText(scannedQrCode.getName());
            QRcodeName = scannedQrCode.getName();
            QR_result.setText(scannedQrCode.getResult());

        });


        // Photo Button to add a picture to the QR code
        Button addPhotoButton = root.findViewById(R.id.photo_button);
        addPhotoButton.setOnClickListener(view -> {
            Intent cameraIntent = new Intent(ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        });

        // Geolocation Button to add the location of the QR code
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        Button addGeoButton = root.findViewById(R.id.geolocation_button);
        addGeoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                    CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
                    fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
                            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    scannedQrCode.setgeoPoint(location.getLatitude(), location.getLongitude());
                                    Toast.makeText(getContext(), "Location Saved Successfully", Toast.LENGTH_SHORT).show();
                                    System.out.println(scannedQrCode.getgeoPoint());
                                }
                            })
                            .addOnFailureListener(getActivity(), new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Location not saved", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "You need to enable Location permission from Settings", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Save Button to save the QR code to the database
        Button saveButton = root.findViewById(R.id.save_qr_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseFirestore.getInstance();
                // Check if the QR code already exists in the database
                db.collection("QR").document(QRcodeName).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        // if Document exists change name of the QR code to avoid overwriting
                                        Log.d(TAG, "Document with same Name exists in Firebase");
                                        String DocumentName = scannedQrCode.nameExistsInFirebase();
                                        addQRToFirebase(DocumentName);
                                    } else {
                                        // if Document does not exist add the QR code to the database normally
                                        Log.d(TAG, "Document with same Name does not exist in Firebase");
                                        addQRToFirebase(QRcodeName);
                                    }
                                } else {
                                    Log.d(TAG, "Error getting document: ", task.getException());
                                }
                            }
                        });

                // Go back to Profile fragment to signify completion of QR scan
                Fragment fragment = new ProfileFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
                fragmentTransaction.commit();
            }
        });

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // Location permission denied
                // Handle the denial or request again
                Toast.makeText(getActivity(), "Location permission is required for saving QR code location", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * This method is adds a QR code to the Firebase database
     * @param documentName the name of the document to be added to the database
     */
    private void addQRToFirebase(String documentName) {
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("QR");
        // Add the data to the database
        HashMap<String, String> data = new HashMap<>();
        try {
            data.put("Location", scannedQrCode.getgeoPoint().toString());
        } catch (Exception e) {
            data.put("Location", "No Location");
        }
        data.put("Points", String.valueOf(scannedQrCode.getPoints()));
        data.put("Hash", scannedQrCode.getHash());
        data.put("ImageURL", scannedQrCode.getImageURL());
        collectionReference.document(documentName).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be added!" + e);
                    }
                });

        // Add comment to database specific to a qr code

        // Right now I am using player "akwrgbpiyBPHzTUlgY4dNHFP3NN2" once we get the user ID set document to user ID

        CollectionReference cf = db.collection("users");
        HashMap<String, String> d = new HashMap<>();
        d.put("comment", comment.getText().toString());
        cf.document("akwrgbpiyBPHzTUlgY4dNHFP3NN2").collection("scanned_qr").document(documentName).set(d)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be added!" + e);
                    }
                });

    }

}


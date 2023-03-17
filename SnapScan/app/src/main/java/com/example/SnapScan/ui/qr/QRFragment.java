package com.example.SnapScan.ui.qr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.SnapScan.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class QRFragment extends Fragment {

    private com.example.SnapScan.databinding.FragmentQrBinding binding;
    private static final String TAG = "QRscanFragment";
    FirebaseFirestore db;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // setContentView(R.layout.activity_qrscan);
//        scancode();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr, container, false);
        scancode();
        return view;
    }
    private void scancode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(QRFragment.class);
        barLaucher.launch(options);
    }

//    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(
//            new ScanContract(),
//            new ActivityResultCallback<ScanResult>() {
//                @Override
//                public void onActivityResult(ScanResult result) {
//                    if (result.getContents() != null) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//
//                        QRcode current_qr = new QRcode(result.getContents(), "Edmonton");
//                        HashMap<String, String> data = new HashMap<>();
//                        data.put("Location", "Edmonton");
//                        data.put("points", String.valueOf(current_qr.getPoints()));
//                        data.put("content", current_qr.getName());
//                        data.put("unique_name", current_qr.getUnique_name());
//                        try {
//                            data.put("unique id", current_qr.getHashno());
//                        } catch (NoSuchAlgorithmException e) {
//                            e.printStackTrace();
//                        }
//
//                        db = FirebaseFirestore.getInstance();
//                        final CollectionReference collectionReference = db.collection("QRs");
//
//                        collectionReference
//                                .document(result.getContents())
//                                .set(data)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        // These are a method which gets executed when the task is succeeded
//                                        Log.d(TAG, "Data has been added successfully!");
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        // These are a method which gets executed if there’s any problem
//                                        Log.d(TAG, "Data could not be added!" + e.toString());
//                                    }
//                                });
//
//                        builder.setTitle("Result");
//                        builder.setMessage(result.getContents());
//                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent = new Intent(getActivity(), MainActivity.class);
//                                startActivity(intent);
//                            }
//                        }).show();
//                    }
//                }
//            });
ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
    if (result.getContents() != null) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        QRcode current_qr = new QRcode(result.getContents(), "Edmonton");
        HashMap<String, String> data = new HashMap<>();
        data.put("Location", "Edmonton");
        data.put("points", String.valueOf(current_qr.getPoints()));
        data.put("content", current_qr.getName());
        data.put("unique_name", current_qr.getUnique_name());
        try {
            data.put("unique id", current_qr.getHashno());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("QRs");

        collectionReference
                .document(result.getContents())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }
                });

        builder.setTitle("Result");
        builder.setMessage(result.getContents());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
});




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
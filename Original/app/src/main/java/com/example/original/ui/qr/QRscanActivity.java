package com.example.original.ui.qr;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.example.original.MainActivity;
import com.example.original.ui.qr.CaptureAct;
import com.example.original.ui.qr.QRcode;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class QRscanActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseApp.initializeApp(this);

        // setContentView(R.layout.activity_qrscan);
        scancode();
    }

    private void scancode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    //dialogInterface.dismiss();
                    Intent intent = new Intent(QRscanActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }).show();
        }
    });
}
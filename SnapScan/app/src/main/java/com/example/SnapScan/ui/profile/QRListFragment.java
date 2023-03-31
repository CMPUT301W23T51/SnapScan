package com.example.SnapScan.ui.profile;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SnapScan.R;
import com.example.SnapScan.model.QRcode;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class QRListFragment extends Fragment {
    protected RecyclerView recyclerView;
    protected ArrayList<QRcode> qrList = new ArrayList<>();
    FirebaseFirestore db;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_qr_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_qr_list);
        populateQRList();
        // TODO: Code works only with this line added figure out why
        qrList.add(new QRcode("trial1"));
        QRListAdapter qrListAdapter = new QRListAdapter(qrList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(qrListAdapter);
        return view;
    }

    /**
     * This method will populate the qrList with the qr codes that the user has scanned
     */
    public void populateQRList() {
        // Get a Fire store instance
        db = FirebaseFirestore.getInstance();
        ArrayList<String> qrHashList = new ArrayList<>();
        db.collectionGroup("scanned").get()
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
                    QRcode qr = documentSnapshot.toObject(QRcode.class);
                    // IMPORTANT: Firebase is changing the hash value of the qr code somehow which
                    // is causing the qr code to not be found in the database
                    qrList.add(qr);
                    Log.d(TAG, "Successfully got QR code from database");
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}

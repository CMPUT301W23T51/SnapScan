package com.example.SnapScan.ui.profile;

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
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class QRListFragment extends Fragment {
    protected RecyclerView recyclerView;
    protected ArrayList<QRcode> qrList = new ArrayList<>();
    FirebaseFirestore db;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_qr_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_qr_list);
        populateQRList();
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
        // Get a Firestore instance
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
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}

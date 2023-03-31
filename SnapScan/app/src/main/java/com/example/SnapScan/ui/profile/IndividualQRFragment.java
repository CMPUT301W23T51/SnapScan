package com.example.SnapScan.ui.profile;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.SnapScan.R;
import com.example.SnapScan.model.QRcode;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class IndividualQRFragment extends Fragment {
    FirebaseFirestore db;
    private String qrHash;
    private TextView qrNameView;
    private TextView qrScoreView;
    private TextView qrLatView;
    private TextView qrLongView;
    private TextView qrCommentView;
    private ImageView qrImage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual_qr, container, false);

        // Receive the QR hash from the QRListFragment
        getParentFragmentManager().setFragmentResultListener("QR Hash", this, (requestKey, result) -> {
            qrHash = result.getString("QR Hash");
        });
        displayQR(qrHash);
        Button back_button = view.findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    /**
     * Get the QR code object from the database
     * and set the views to the QR code's attributes
     *
     * @param qrHash the hash of the QR code
     */
    public void displayQR(String qrHash) {
        db = FirebaseFirestore.getInstance();
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
                Log.d(TAG, "Successfully got QR code from database");
                qrNameView = getView().findViewById(R.id.qr_name_placeholder);
                qrNameView.setText(qr.getName());
                qrScoreView = getView().findViewById(R.id.qr_score_placeholder);
                qrScoreView.setText(String.valueOf(qr.getPoints()));
                qrLatView = getView().findViewById(R.id.qr_latitude_placeholder);
                qrLatView.setText(String.valueOf(qr.getgeoPoint().getLatitude()));
                qrLongView = getView().findViewById(R.id.qr_longitude_placeholder);
                qrLongView.setText(String.valueOf(qr.getgeoPoint().getLongitude()));
                qrCommentView = getView().findViewById(R.id.qr_comment);
                qrCommentView.setText(qr.getImageURL());
                // TODO: get proper comments
                qrImage = getView().findViewById(R.id.imageView_qr);
                qr.loadImage(qrImage);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

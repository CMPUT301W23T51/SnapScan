package com.example.SnapScan.ui.profile;

import static android.content.ContentValues.TAG;

import static com.example.SnapScan.MainActivity.USER_ID;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.SnapScan.R;
import com.example.SnapScan.model.QRcode;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class IndividualQRFragment extends Fragment {
    FirebaseFirestore db;
    private String qrHash;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_individual_qr, container, false);
        ProgressBar progressBar = view.findViewById(R.id.progressBar_qr_individual);

        // Receive the QR hash from the QRListFragment
        getParentFragmentManager().setFragmentResultListener("Hash", this, (requestKey, result) -> {
            qrHash = result.getString("QR Hash");
            // Important to use DisplayQR line here, otherwise the QR code will not be displayed
            // as the hash is not received in time if it is called outside of this method
            displayQR(qrHash);
            progressBar.setVisibility(View.INVISIBLE);

        });
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
                Log.d("IndividualQRFragment", "Successfully Made QR Code");
                ImageView qrImage = getView().findViewById(R.id.imageView_qr);
                qr.loadImage(qrImage);
                TextView qrNameView = getView().findViewById(R.id.qr_name_placeholder);
                qrNameView.setText(qr.getName());
                TextView qrResultView = getView().findViewById(R.id.qr_result_placeholder);
                qrResultView.setText(qr.getResult());
                TextView qrScoreView = getView().findViewById(R.id.qr_score_placeholder);
                qrScoreView.setText(String.valueOf(qr.getPoints()));
                displayComment(qrHash);
            }
        });
    }
    /**
     * This method is responsible for displaying the Comment User has made on the QR
     * if the user has not made one it will display "No Comment"
     * @param qrHash the hash of the QR code
     */
    public void displayComment(String qrHash) {
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users")
                .document(USER_ID)
                .collection("Scanned QRs")
                .document(qrHash);
        docRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to get QR Code " + e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String comment = documentSnapshot.getString("Comment");
                Log.d("IndividualQRFragment", "Successfully Found Comment");
                if (comment == null || comment.isEmpty()) {
                    comment = "No Comment";
                }
                TextView qrCommentView = getView().findViewById(R.id.qr_comment);
                qrCommentView.setText(comment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

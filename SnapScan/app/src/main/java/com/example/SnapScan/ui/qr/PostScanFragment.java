package com.example.SnapScan.ui.qr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;
import androidx.fragment.app.FragmentResultListener;

import com.example.SnapScan.R;

public class PostScanFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    QRcode scannedQrCode;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_post_scan, container, false);


        // Check if location permission is already granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Location permission is already granted
            // Do something with the location
        } else {
            // Location permission is not granted yet, request it
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        // Receive the result from ScanQRFragment
        getParentFragmentManager().setFragmentResultListener("dataFromQR", this, (requestKey, result) -> {
            String data = result.getString("Scanned Data");
            scannedQrCode = new QRcode(data);

            //Setting up the  views to display the data

            // Using Picasso to load the image from the URL
            ImageView imageView = root.findViewById(R.id.imageViewQrCode);
            String URL = "https://picsum.photos/seed/" + QRcode.getImageSeed() + "/270";
            Picasso.get()
                    .load(URL)
                    .into(imageView);

            TextView QR_score = root.findViewById(R.id.qr_score_text);
            TextView QR_name = root.findViewById(R.id.qr_name_text);
            TextView QR_result = root.findViewById(R.id.qr_result_text);
            QR_score.setText(String.valueOf(scannedQrCode.getPoints()));
            QR_name.setText(scannedQrCode.getName());
            QR_result.setText(scannedQrCode.getResult());
        });


        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted
                // Do something with the location
            } else {
                // Location permission denied
                // Handle the denial or request again
                Toast.makeText(getActivity(), "Location permission is required for saving QR code location", Toast.LENGTH_LONG).show();

            }
        }
    }
}


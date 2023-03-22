package com.example.SnapScan.ui.qr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.SnapScan.R;

public class PostScanFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 101;

    private QRViewModel postScanViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        postScanViewModel =
                new ViewModelProvider(this).get(QRViewModel.class);
        View root = inflater.inflate(R.layout.fragment_post_scan, container, false);

        final TextView textView = root.findViewById(R.id.postScanText);

        // Check if location permission is already granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Location permission is already granted
            // Do something with the location
        } else {
            // Location permission is not granted yet, request it
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        postScanViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
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


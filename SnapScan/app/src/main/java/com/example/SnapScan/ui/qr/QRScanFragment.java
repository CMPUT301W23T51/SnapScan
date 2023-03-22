package com.example.SnapScan.ui.qr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.SnapScan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * This fragment is used to scan the QR code
 * upon successful scan, the data is displayed in the PostScanFragment
 */
public class QRScanFragment extends Fragment {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private BottomNavigationView bottomNav;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr, container, false);

        barcodeScannerView = view.findViewById(R.id.barcode_scanner);

        // Hide the bottom navigation bar to show the full screen
        bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.GONE);

        capture = new CaptureManager(getActivity(), barcodeScannerView);
        startScanner();

        return view;
    }

    private void startScanner() {
        BarcodeCallback callback = result -> {
            // Open the PostScanFragment after scanning the QR code to display the data
            if (result.getText() != null) {
                // display the bottom navigation bar after scanning the QR code
                bottomNav.setVisibility(View.VISIBLE);
                barcodeScannerView.setStatusText(result.getText());
                barcodeScannerView.pause();

                // Replace the current fragment with the PostScanFragment
                Bundle data = new Bundle();
                data.putString("Scanned Data", result.getText());
                getParentFragmentManager().setFragmentResult("dataFromQR", data);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PostScanFragment newFragment = new PostScanFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, newFragment);
                // if you want to allow user to go back to scanning on back press then add this line
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };
        barcodeScannerView.decodeContinuous(callback);
        barcodeScannerView.initializeFromIntent(new IntentIntegrator(getActivity()).createScanIntent());
    }

    @Override
    public void onResume() {
        super.onResume();

        // if camera permission is not granted, request permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            capture.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capture.onResume();
            } else {
                Toast.makeText(getActivity(), "Camera permission is required for scanning QR codes", Toast.LENGTH_LONG).show();
            }
        }
    }


}
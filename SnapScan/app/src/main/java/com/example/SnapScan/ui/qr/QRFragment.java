package com.example.SnapScan.ui.qr;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.SnapScan.R;
import com.google.zxing.Result;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QRFragment extends Fragment implements DecoratedBarcodeView.TorchListener {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 101;

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private boolean isTorchOn = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr, container, false);

        barcodeScannerView = view.findViewById(R.id.barcode_scanner);
        barcodeScannerView.setTorchListener(this);

        capture = new CaptureManager(getActivity(), barcodeScannerView);
        barcodeScannerView.setTorchListener(new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                toggleTorch();
            }

            @Override
            public void onTorchOff() {
                toggleTorch();
            }
        });
        capture.initializeFromIntent(getActivity().getIntent(), savedInstanceState);
        capture.decode();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            capture.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeScannerView.setTorchListener(null);
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
        } else if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            // Handle storage permission request
        }
    }

    @Override
    public void onTorchOn() {
        isTorchOn = true;
    }

    @Override
    public void onTorchOff() {
        isTorchOn = false;
    }

    private void toggleTorch() {
        if (isTorchOn) {
            barcodeScannerView.setTorchOff();
            onTorchOff();
        } else {
            barcodeScannerView.setTorchOn();
            onTorchOn();
        }
    }

    public void onBarcodeScanned(Result result) {
        String scannedResult = result.getText();
//        Toast.makeText(getActivity(), "Scanned: " + scannedResult, Toast.LENGTH_LONG).show();
        // show the result in a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Scan Result");
        builder.setMessage(scannedResult);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Replace the current fragment with the Decoded Result fragment
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                MyNewFragment newFragment = new MyNewFragment();
//                fragmentTransaction.replace(R.id.fragment_container, newFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();

            }
        });
        builder.show();
    }

}

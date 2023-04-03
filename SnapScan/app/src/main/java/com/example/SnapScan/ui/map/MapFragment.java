package com.example.SnapScan.ui.map;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.SnapScan.R;
import com.example.SnapScan.model.QRcode;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Marker currentLocationMarker;
    private QRcode qrcode;

    private FirebaseFirestore db;
    private CollectionReference qrCollection;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
//        qrcode = new QRcode(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        db = FirebaseFirestore.getInstance();
        qrCollection = db.collection("QR");
//        collectionReference.document(documentName).set(qrcode);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        if (currentLocationMarker == null) {
                            currentLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                        } else {
                            currentLocationMarker.setPosition(latLng);
                        }
                    }
                }

                Location currentLocation = null;
                if (currentLocation != null) {
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    if (currentLocationMarker == null) {
                        currentLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                    } else {
                        currentLocationMarker.setPosition(latLng);
                    }
                }
            }
        };

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        return view;
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(5000)
                .setFastestInterval(2000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback,
                null /* Looper */);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Update user's current location on the map
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates();
        } else {
            Toast.makeText(getContext(), "Location permission not granted", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            // Query Firebase and add markers here

            qrCollection.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        GeoPoint geoPoint = document.getGeoPoint("geoPoint");
                        if (geoPoint != null){
                            double latitude = geoPoint.getLatitude();
                            double longitude = geoPoint.getLongitude();
                            LatLng latLng = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        }

                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            });
        }

    }
}



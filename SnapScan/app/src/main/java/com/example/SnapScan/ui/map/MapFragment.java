package com.example.SnapScan.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.SnapScan.R;
import com.example.SnapScan.model.QRcode;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
/**

 A MapFragment that displays a map with markers for QR codes with location attributes

 within 2km of the user's current location.

 This class implements the OnMapReadyCallback and LocationListener interfaces to handle

 map and location related events.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private List<QRcode> qrCodes = new ArrayList<>();
    /**

     Called when the Fragment is being created.
     @param savedInstanceState A Bundle containing data that was saved when the Fragment was destroyed
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize location manager
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
    }
    /**

     Called when the Fragment's UI is being created.

     @param inflater The LayoutInflater object that can be used to inflate any views in the Fragment

     @param container The parent ViewGroup that the Fragment's UI should be attached to

     @param savedInstanceState A Bundle containing data that was saved when the Fragment was destroyed

     @return The View for the Fragment's UI, or null
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return rootView;
    }
    /**

     Called when the map is ready to be used.

     @param googleMap The GoogleMap object that displays the map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Check for location permissions and request if necessary
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        mMap.setMyLocationEnabled(true);
// Set up location listener to update user's current location on the map
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
// Retrieve QR codes with location attributes from Firestore
        FirebaseFirestore.getInstance().collection("QR")
                .whereEqualTo("geoPoint",true)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            QRcode qrCode = document.toObject(QRcode.class);
                            qrCodes.add(qrCode);
                            // Convert latitude and longitude values from N/S and W/E format to positive/negative format
                            double latitude = qrCode.getGeoPoint().getLatitude();
                            double longitude = qrCode.getGeoPoint().getLongitude();
                            if (qrCode.getGeoPoint() != null) {
                                String latStr = String.valueOf(qrCode.getGeoPoint().getLatitude());
                                String lonStr = String.valueOf(qrCode.getGeoPoint().getLongitude());
                                if (latStr.endsWith("S")) {
                                    latitude *= -1;
                                }
                                if (lonStr.endsWith("W")) {
                                    longitude *= -1;
                                }
                                if (latStr.endsWith("N")) {
                                    latitude *= 1;
                                }
                                if (lonStr.endsWith("E")) {
                                    longitude *= 1;
                                }
                            }
                            // Add a marker for each QR code with location attribute within 2km of the user's current location
                            if (qrCode.getGeoPoint() != null && distance(latitude, longitude, locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(), locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude()) <= 5) {
                                LatLng latLng = new LatLng(latitude, longitude);
                                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(qrCode.getName()));
                                marker.setTag(document.getId());
                            }
                        }
                    } else {
                        Log.d("MapFragment", "Error getting documents: ", task.getException());
                    }
                });



    }
    /**

     Updates the user's current location on the map and animates the camera to zoom in on the location.
     @param location The current location of the user.
     */

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Update user's current location on the map
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }
    /**

     Handles the result of a permission request for location access. If permission is granted, enables the
     display of the user's location on the map and starts requesting updates to the location from the
     LocationManager. If permission is not granted, prompts the user to grant permission.
     @param requestCode The code that identifies the permission request.
     @param permissions The permissions that were requested.
     @param grantResults The results of the permission request.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Check if location permission was granted and update map if necessary
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            if ((ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }
    /**

     Calculates the distance between two sets of latitude and longitude coordinates using the Haversine formula.
     @param lat1 The latitude of the first coordinate.
     @param lon1 The longitude of the first coordinate.
     @param lat2 The latitude of the second coordinate.
     @param lon2 The longitude of the second coordinate.
     @return The distance between the two coordinates, in kilometers.
     */

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        // Calculate distance between two sets of latitude and longitude coordinates using the Haversine formula
        double R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d;
    }
}


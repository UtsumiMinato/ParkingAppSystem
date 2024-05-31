package com.example.parkingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;

import java.io.IOException;
import java.util.List;

import android.widget.Toast;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private SearchView searchView;
    private FusedLocationProviderClient mLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.idSearchView);
        mLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getDeviceLocation();
        } else {
            // Set the map coordinates to Kyoto Japan.
            LatLng kyoto = new LatLng(35.00116, 135.7681);
            // Set the map type to Hybrid.
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // Add a marker on the map coordinates.
            googleMap.addMarker(new MarkerOptions()
                    .position(kyoto)
                    .title("Kyoto"));
            // Move the camera to the map coordinates and zoom in closer.
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(kyoto));
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            // Display traffic.
            googleMap.setTrafficEnabled(true);

            //設定預設北方朝上
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(kyoto)
                    .zoom(15)
                    .bearing(0)  // 北方
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Log.d("Location", "Current location: Latitude " + latitude + ", Longitude " + longitude);
                        LatLng currentLocation = new LatLng(latitude, longitude);
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(currentLocation)
                                .zoom(15)
                                .bearing(0)  //北方
                                .build();
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                    } else {
                        Log.d("Location", "No location retrieved");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("Location", "Error trying to get last GPS location", e);
                });
    }

    private void searchLocation(String location) {
        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.clear();
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(12)
                        .bearing(0)  //北方
                        .build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            } else {
                Log.d("Location", "Location not found: " + location);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.example.parkingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.clustering.ClusterManager;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private SearchView searchView;
    private ClusterManager<MapClusterItem> mClusterManager;
    private FusedLocationProviderClient mLocationClient;
    private List<LatLng> predefinedLocations;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private TextView parkingLotName, parkingLotAddress, parkingLotAvailable, parkingLotPrice, parkingLotTotal, parkingLotManagerName, parkingLotManagerPhone;
    private Button navigateButton;
    private LatLng currentDestination;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        predefinedLocations = new ArrayList<>();
        addPredefinedLocations();
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    private void addPredefinedLocations() {
        predefinedLocations.add(new LatLng(23.69240510302933, 120.54115858531878));
        predefinedLocations.add(new LatLng(23.69589083905699, 120.52695418898324));
        predefinedLocations.add(new LatLng(23.697305531011292, 120.52502299857244));
        predefinedLocations.add(new LatLng(23.697619904919158, 120.52751208843522));
        predefinedLocations.add(new LatLng(23.701588810386625, 120.53150321528412));
        predefinedLocations.add(new LatLng(23.699034578122333, 120.54163123632712));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navigateButton = view.findViewById(R.id.btnNavigate);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMapsForNavigation(currentDestination);
            }
        });

        searchView = view.findViewById(R.id.idSearchView);
        mLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        parkingLotName = view.findViewById(R.id.parking_lot_name);
        parkingLotAddress = view.findViewById(R.id.parking_lot_address);
        parkingLotAvailable = view.findViewById(R.id.parking_lot_available);
        parkingLotPrice = view.findViewById(R.id.parking_lot_price);
        parkingLotTotal = view.findViewById(R.id.parking_lot_total);
        parkingLotManagerName = view.findViewById(R.id.parking_lot_manager_name);
        parkingLotManagerPhone = view.findViewById(R.id.parking_lot_manager_phone);

        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    parkingLotTotal.setVisibility(View.VISIBLE);
                    parkingLotManagerName.setVisibility(View.VISIBLE);
                    parkingLotManagerPhone.setVisibility(View.VISIBLE);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    parkingLotTotal.setVisibility(View.GONE);
                    parkingLotManagerName.setVisibility(View.GONE);
                    parkingLotManagerPhone.setVisibility(View.GONE);
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    parkingLotTotal.setVisibility(View.GONE);
                    parkingLotManagerName.setVisibility(View.GONE);
                    parkingLotManagerPhone.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // 本來想加動畫效果
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                hideKeyboard();
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
        setUpClusterer();
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        checkLocationPermission();

        mMap.setOnMarkerClickListener(marker -> {
            // 檢查是否是自訂標記
            if (marker.getTag() instanceof MapClusterItem) {
                showParkingLotDetails((MapClusterItem) marker.getTag());
            }
            return true;
        });
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 預設位置台北
            LatLng taipei = new LatLng(25.0330, 121.5654);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(taipei)
                    .zoom(15)
                    .bearing(0) // 北方朝上
                    .build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.addMarker(new MarkerOptions().position(taipei).title("Default Location: Taipei"));

            return;
        }

        mMap.setMyLocationEnabled(true);
        getDeviceLocation();
    }

    private void setUpClusterer() {
        mClusterManager = new ClusterManager<>(getContext(), mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(item -> {
            showParkingLotDetails(item);
            return true;
        });
        addItems();
    }

    private void addItems() {
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.69240510302933, 120.54115858531878), "Group 5 Parking Lot", "描述", "No. 353, Zhuangjing Rd, Douliu City, Yunlin County, 640", "30/1hr",10, 50, "John Doe", "05-5351576,0968739743"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.69589083905699, 120.52695418898324), "Parking Lot_2", "描述", "No. 600, Section 3, Daxue Rd, Douliu City, Yunlin County, 640", "20/1hr",5,30, "Jane Smith", "05-5350202,0968794740"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.697305531011292, 120.52502299857244), "Parking Lot_3", "描述", "No. 19, Section 2, Yunlin Rd, Douliu City, Yunlin County, 640", "10/30min",0,20, "Jane Smith", "05-5350202,0968794740"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.697619904919158, 120.52751208843522), "Parking Lot_4", "描述", "No. 297, Section 2, Yunlin Rd, Douliu City, Yunlin County, 640", "20/1hr",20,100, "Mike Brown", "05-5352814, 0968848974"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.701588810386625, 120.53150321528412), "Central Parking", "描述", "No. 60, Baozhang Rd, Douliu City, Yunlin County, 64058", "30/1hr",15,150, "John Doe", "05-5351576,0968739743"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.699034578122333, 120.54163123632712), "Parking Lot_6", "描述", "No. 240, Zhennan Rd, Douliu City, Yunlin County, 640", "30/1hr",8,40, "Mike Brown", "05-5352814"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.68821243409715, 120.51725212857887), "Parking Lot_7", "Description", "Some address, Douliu City, Yunlin County", "15/30min",5,25, "Person A", "05-5300000"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.701416406041318, 120.51399056253165), "Parking Lot_8", "Description", "Another address, Douliu City, Yunlin County", "25/1hr",10,50, "Person B", "05-5300001"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.709432451668924, 120.51210228745167), "Parking Lot_9", "Description", "Additional address, Douliu City, Yunlin County", "20/1hr",15,60, "Person C", "05-5300002"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.707467780160165, 120.55184189481669), "Parking Lot_10", "Description", "More address, Douliu City, Yunlin County", "30/1hr",20,80, "Person D", "05-5300003"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.715479970501605, 120.54847620233127), "Parking Lot_11", "Description", "Different address, Douliu City, Yunlin County", "10/30min",0,30, "Person E", "05-5300004"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.71501271457807, 120.53877950845772), "Parking Lot_12", "Description", "Distinct address, Douliu City, Yunlin County", "35/1hr",25,100, "Person F", "05-5300005"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.704661266738068, 120.5502080309321), "Parking Lot_13", "Description", "Specific address, Douliu City, Yunlin County", "40/1hr",30,120, "Person G", "05-5300006"));

    }

    private void searchLocation(String query) {
        if ("douliu".equalsIgnoreCase(query)) {
            mClusterManager.clearItems(); // 清除所有 cluster items
            addItems(); // 重新加速 all items
            mClusterManager.cluster();

            // 計算所有預定義位置的邊界以聚焦地圖
            if (!predefinedLocations.isEmpty()) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (LatLng location : predefinedLocations) {
                    builder.include(location);
                }
                LatLngBounds bounds = builder.build();
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(bounds.getCenter())
                        .zoom(mMap.getCameraPosition().zoom)
                        .bearing(0) // 北方朝上
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        } else {
            Geocoder geocoder = new Geocoder(getContext());
            try {
                List<Address> addresses = geocoder.getFromLocationName(query, 1);
                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.clear(); // 清除之前的標記
                    mMap.addMarker(new MarkerOptions().position(latLng).title(query));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)
                            .zoom(15)
                            .bearing(0) // 北方朝上
                            .build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                } else {
                    Toast.makeText(getContext(), "Location not found: " + query, Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(currentLocation)
                                .zoom(15)
                                .bearing(0) // 北方朝上
                                .build();
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                    } else {
                        Toast.makeText(getContext(), "Current location not found", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("Location", "Error trying to get last GPS location");
                    Toast.makeText(getContext(), "Error trying to get current location", Toast.LENGTH_LONG).show();
                });
    }

    private void showParkingLotDetails(Marker marker) {
        parkingLotName.setText(marker.getTitle());
        // 在這裡設置其他詳細資訊
        parkingLotAddress.setText("Some Address");
        parkingLotAvailable.setText("10"); // 假設可用車位

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); // 只設置為 COLLAPSED
    }

    private void openGoogleMapsForNavigation(LatLng destination) {
        if (destination != null) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + destination.latitude + "," + destination.longitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(getContext(), "Google Maps is not installed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showParkingLotDetails(MapClusterItem item) {
        parkingLotName.setText("Name: " + item.getTitle());
        parkingLotAddress.setText("Address: " + item.getAddress());
        parkingLotAvailable.setText("Available: " + item.getAvailable());
        parkingLotPrice.setText("Price: " + item.getPrice());
        parkingLotTotal.setText("Total: " + item.getTotal());
        parkingLotManagerName.setText("Admin Name: " + item.getManagerName());
        parkingLotManagerPhone.setText("Admin Phone: " + item.getManagerPhone());

        currentDestination = item.getPosition();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED); // 只設置為 COLLAPSED
    }


    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}



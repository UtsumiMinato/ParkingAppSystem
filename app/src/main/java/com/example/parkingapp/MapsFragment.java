package com.example.parkingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
    private TextView parkingLotName, parkingLotAddress, parkingLotAvailable, parkingLotTotal, parkingLotManagerName, parkingLotManagerPhone;

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
        searchView = view.findViewById(R.id.idSearchView);
        mLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        parkingLotName = view.findViewById(R.id.parking_lot_name);
        parkingLotAddress = view.findViewById(R.id.parking_lot_address);
        parkingLotAvailable = view.findViewById(R.id.parking_lot_available);
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
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.69240510302933, 120.54115858531878), "停車場 1", "描述", "地址 1", 10, 50, "管理者 1", "123456789"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.69589083905699, 120.52695418898324), "停車場 2", "描述", "地址 2", 5, 30, "管理者 2", "987654321"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.697305531011292, 120.52502299857244), "停車場 3", "描述", "地址 3", 0, 20, "管理者 3", "111222333"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.697619904919158, 120.52751208843522), "停車場 4", "描述", "地址 4", 20, 100, "管理者 4", "444555666"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.701588810386625, 120.53150321528412), "停車場 5", "描述", "地址 5", 15, 70, "管理者 5", "777888999"));
        mClusterManager.addItem(new MapClusterItem(new LatLng(23.699034578122333, 120.54163123632712), "停車場 6", "描述", "地址 6", 8, 40, "管理者 6", "000111222"));

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

    private void showParkingLotDetails(MapClusterItem item) {
        parkingLotName.setText("Name: " + item.getTitle());
        parkingLotAddress.setText("Address: " + item.getAddress());
        parkingLotAvailable.setText("Available: " + item.getAvailable());
        parkingLotTotal.setText("Total: " + item.getTotal());
        parkingLotManagerName.setText("Admin Name: " + item.getManagerName());
        parkingLotManagerPhone.setText("Admin Phone: " + item.getManagerPhone());

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



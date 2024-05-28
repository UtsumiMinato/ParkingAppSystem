package com.example.parkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.parkingapp.databinding.ActivityMainBinding;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Button finBtn;
    private SearchView searchView;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final static int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ActivityMainBinding binding;

    private MapsFragment fragmentMap;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(binding.getRoot());

        //主Fragment 管理
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentMap = new MapsFragment();
        fragmentTransaction.add(R.id.fragment_test,fragmentMap,"Map");
        fragmentTransaction.show(fragmentMap);
        fragmentTransaction.commit();

        //管理nav
        // 设置底部导航监听器
        binding.navigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new MapsFragment();
                    break;
                case R.id.nav_car_plate:
                    selectedFragment = new CarPlateFragment();
                    break;
                case R.id.nav_payment:
                    selectedFragment = new PaymentFragment();
                    break;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_test, selectedFragment)
                        .commit();
            }
            return true;
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initializeMap();
        }



        // 初始化 SearchView
//        searchView = findViewById(R.id.editTextText);
//        if (searchView != null) {
//            searchView.clearFocus();
//            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
////                    filterList(query);
//                    return true;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    return false;
//                }
//            });
//        } else {
//            Toast.makeText(this, "SearchView not found in layout.", Toast.LENGTH_SHORT).show();
//        }

        // 初始化按钮
//        finBtn = findViewById(R.id.findBtn);
//        if (finBtn != null) {
//            finBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // 在按钮点击时执行的操作
//                    FindSpecific findSpecific = new FindSpecific();
//                    findSpecific.queryParkingLots("南港路");
//                }
//            });
//        } else {
//            Toast.makeText(this, "Button not found in layout.", Toast.LENGTH_SHORT).show();
//        }
    }



//    private void filterList(String text) {
//        List<Item> filteredList = new ArrayList<>();
//        for (Item item : itemList) {
//            if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
//                filteredList.add(item);
//            }
//        }
//
//        if (filteredList.isEmpty()) {
//            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
//        } else {
//            // Update the UI with the filtered list
//        }
//    }
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請開始說話...");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                if (searchView != null) {
                    searchView.setQuery(spokenText, true);
                } else {
                    Toast.makeText(this, "SearchView not found.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 檢查權限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true); // 啟用「回到目前位置」按鈕
            getDeviceLocation();
        }
    }

    private void getDeviceLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationProviderClient.getLastLocation()
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location location = task.getResult();
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                            }
                        });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap();
            } else {
                // 權限被拒絕，顯示消息或禁用功能
                Toast.makeText(this, "Permission denied to access location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

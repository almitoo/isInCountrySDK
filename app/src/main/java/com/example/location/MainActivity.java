package com.example.location;

import android.location.Location;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.isincountry.LocationSdk;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LocationSdk locationSdk;

    private GoogleMap myMap;
    private EditText countryCodeInput;
    private Button btnCheck;
    private TextView resultTextView;
    private LinearLayout  bottomLayout;

    private FusedLocationProviderClient locationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(MainActivity.this);

        locationSdk = new LocationSdk(this);

        countryCodeInput = findViewById(R.id.country_code_input);
        btnCheck = findViewById(R.id.btn_check);
        resultTextView = findViewById(R.id.resultTextView);

          bottomLayout = findViewById(R.id.info_layout);

        btnCheck.setOnClickListener(v -> checkIfInCountry());





    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        myMap = googleMap;

        LatLng defaultLocation = new LatLng(0, 0);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 3));

    }


    private void checkIfInCountry() {
        String countryCode = countryCodeInput.getText().toString().trim().toUpperCase();

        if (countryCode.isEmpty()) {
            Toast.makeText(this, "Please enter a country code", Toast.LENGTH_SHORT).show();
            return;
        }
        if (countryCode.length() != 2) {
            resultTextView.setText("Country code must be exactly 2 letters.");
            return;
        }

        updateMapToCurrentLocation();


        locationSdk.isInCountry(countryCode, new LocationSdk.LocationResultCallback() {
            @Override
            public void onSuccess(boolean success) {
                if (success) {
                    resultTextView.setText("IN THE BORDER");
                    bottomLayout.setBackgroundColor(getResources().getColor(R.color.green_light));
                } else {
                    resultTextView.setText("OUT THE BORDER");
                    bottomLayout.setBackgroundColor(getResources().getColor(R.color.red_light));
                }
            }

            @Override
            public void onError(String errorMessage) {
                resultTextView.setText("Error: " + errorMessage);
            }
        });
    }


    private void updateMapToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }


        locationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15)); // זום על המיקום
                            myMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));



                        } else {
                            Toast.makeText(MainActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }























    ///LAST CODE
//    private LocationSdk locationSdk;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        locationSdk = new LocationSdk(this);
//
//        Button btnGetLocation = findViewById(R.id.btn_get_location);
//        btnGetLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                requestLocationPermission();
//            }
//        });
//    }
//
//    private void requestLocationPermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            // Permission is already granted, invoke the SDK
//            invokeLocationSdk();
//        } else {
//            // Request permission
//            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//    }
//
//    private final ActivityResultLauncher<String> locationPermissionLauncher = registerForActivityResult(
//            new ActivityResultContracts.RequestPermission(),
//            isGranted -> {
//                if (isGranted) {
//                    invokeLocationSdk();
//                } else {
//                    Log.e("MainActivity", "Location permission denied.");
//                }
//            }
//    );
//
//    private void invokeLocationSdk() {
//        locationSdk.isInCountry("US", new LocationSdk.LocationResultCallback() {
//            @Override
//            public void onSuccess(boolean success) {
//                if (success) {
//                    Log.d("SDK", "Location sent successfully");
//                } else {
//                    Log.d("SDK", "API call failed");
//                }
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                Log.e("SDK", "Error: " + errorMessage);
//            }
//        });
//    }
}

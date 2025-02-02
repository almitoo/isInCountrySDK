package com.example.isincountry;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationSdk {

    private static final String BASE_URL = "http://10.0.2.2:3000";

    // change after publish
    //https://api-location-zeta.vercel.app
    private final ApiService apiService;
    private final FusedLocationProviderClient locationClient;
    private final Context context;

    public LocationSdk(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) // for json support
                .build();
        apiService = retrofit.create(ApiService.class);
        locationClient = LocationServices.getFusedLocationProviderClient(context);
        this.context = context.getApplicationContext();
    }

    public void isInCountry(String countryCode, LocationResultCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            callback.onError("Location permissions not granted");
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, // Priority
                10000L // Interval in milliseconds
            ).setMinUpdateIntervalMillis(5000L) // Fastest interval
            .build();

        locationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                locationClient.removeLocationUpdates(this);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    callApi(location.getLongitude(), location.getLatitude(), countryCode, callback);
                } else {
                    callback.onError("Unable to fetch location");
                }
            }
        }, Looper.getMainLooper());
    }

    private void callApi(double lon, double lat, String countryCode, LocationResultCallback callback) {
        LocationReq request = new LocationReq(lon, lat, countryCode);
        apiService.sendLocation(request).enqueue(new retrofit2.Callback<ApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse> call, retrofit2.Response<ApiService.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().success);
                } else {
                    callback.onError("API call failed");
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface LocationResultCallback {
        void onSuccess(boolean success);

        void onError(String errorMessage);
    }
}


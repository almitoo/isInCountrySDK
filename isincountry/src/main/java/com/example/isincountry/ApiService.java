package com.example.isincountry;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/location")
    Call<ApiResponse> sendLocation(@Body LocationReq locationReq);

    class ApiResponse {
        public boolean success;
    }
}

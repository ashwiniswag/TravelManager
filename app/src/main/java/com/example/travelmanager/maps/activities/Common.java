package com.example.travelmanager.maps.activities;

import com.example.travelmanager.maps.remotes.GoogleApiService;
import com.example.travelmanager.maps.remotes.RetrofitBuilder;

public class Common {
    private static final String BASE_URL = "https://maps.googleapis.com/";

    public static GoogleApiService getGoogleApiService() {
        return RetrofitBuilder.builder().create(GoogleApiService.class);
    }
}

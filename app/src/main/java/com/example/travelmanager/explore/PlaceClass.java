package com.example.travelmanager.explore;

import android.graphics.Bitmap;

public class PlaceClass {

    private String state,destination;
    private int bitmap;

    public PlaceClass(String state, String destination, int bitmap) {
        this.state = state;
        this.destination = destination;
        this.bitmap = bitmap;
    }

    public String getState() {
        return state;
    }

    public String getDestination() {
        return destination;
    }

    public int getBitmap() {
        return bitmap;
    }
}

package com.example.travelmanager.staticdata;

import android.graphics.Bitmap;

public class InfoClass {

    private String name,info;
    private Bitmap bitmap;

    public InfoClass(String name, String info, Bitmap bitmap) {
        this.name = name;
        this.info = info;
        this.bitmap = bitmap;
    }

    public String getNamee() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}

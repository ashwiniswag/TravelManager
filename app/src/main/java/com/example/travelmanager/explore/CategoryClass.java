package com.example.travelmanager.explore;

import android.graphics.Bitmap;

public class CategoryClass {

    private String cname;
    private int img;

    public CategoryClass(String cname, int img) {
        this.cname = cname;
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public String getCname() {
        return cname;
    }
}

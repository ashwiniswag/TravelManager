package com.example.travelmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintSet;

import java.util.List;

public class Adapter_Grid extends BaseAdapter {
    Context applicationContext;
    List<Bitmap> bitmap;
    LayoutInflater inflater;

    public Adapter_Grid(Context application, List<Bitmap> bitmap){
        this.bitmap=bitmap;
        this.applicationContext=application;
        inflater=LayoutInflater.from(applicationContext);
    }

    @Override
    public int getCount() {
        return bitmap.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=inflater.inflate(R.layout.items,null);
        ImageView img=v.findViewById(R.id.img);
        img.setImageBitmap(bitmap.get(position));
        return v;
    }
}

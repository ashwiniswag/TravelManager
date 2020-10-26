package com.example.travelmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class Gridadpter extends BaseAdapter {

    Context applicationcontext;
    List<Bitmap> bitmap;
    LayoutInflater inflater;

    public Gridadpter(Context applicationcontext,List<Bitmap> bitmap){
        this.applicationcontext=applicationcontext;
        this.bitmap=bitmap;
        inflater=LayoutInflater.from(applicationcontext);
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

        View v=inflater.inflate(R.layout.pic_holder,null);
        ImageView pics=v.findViewById(R.id.image);
        pics.setImageBitmap(bitmap.get(position));
        return v;
    }
}

package com.example.travelmanager.explore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelmanager.R;

import org.w3c.dom.Text;

import java.util.List;

public class Adapter_Destination extends BaseAdapter {

    List<String> plans;
    List<Integer> planpics;
    Context context;
    LayoutInflater inflater;

    public Adapter_Destination(Context context, List<String> plans, List<Integer> planpics) {
        this.plans = plans;
        this.planpics = planpics;
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return plans.size();
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
        View v= inflater.inflate(R.layout.destination_item,null);
        TextView text=v.findViewById(R.id.destination);
        ImageView img=v.findViewById(R.id.place_img);
        text.setText(plans.get(position));
        img.setImageResource(planpics.get(position));
        return v;
    }
}

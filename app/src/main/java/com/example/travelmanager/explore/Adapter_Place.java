package com.example.travelmanager.explore;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelmanager.Adapter;
import com.example.travelmanager.R;

import java.util.List;

public class Adapter_Place extends RecyclerView.Adapter<Adapter_Place.Viewholder> {

    List<PlaceClass> placeClasses;

    public Adapter_Place(List<PlaceClass> placeClasses) {
        this.placeClasses = placeClasses;
    }


    @NonNull
    @Override
    public Adapter_Place.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item,parent,false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Place.Viewholder holder, int position) {
        int bitmap=placeClasses.get(position).getBitmap();
        String state=placeClasses.get(position).getState();
        String destination=placeClasses.get(position).getDestination();
        holder.setdata(state,destination,bitmap);
    }

    @Override
    public int getItemCount() {
        return placeClasses.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private TextView state,destination;
        private ImageView img;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            state=itemView.findViewById(R.id.state);
            destination=itemView.findViewById(R.id.destination);
            img=itemView.findViewById(R.id.place_img);

        }

        public void setdata(String state,String destination,int bitmap){
            this.state.setText(state);
            this.destination.setText(destination);
            this.img.setImageResource(bitmap);
        }

    }

}

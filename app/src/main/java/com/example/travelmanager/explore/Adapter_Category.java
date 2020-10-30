package com.example.travelmanager.explore;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelmanager.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class Adapter_Category extends RecyclerView.Adapter<Adapter_Category.Viewholder> {

    List<CategoryClass> categoryClasses;

    public Adapter_Category(List<CategoryClass> categoryClasses) {
        this.categoryClasses = categoryClasses;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        int bitmap=categoryClasses.get(position).getImg();
        String text=categoryClasses.get(position).getCname();
        holder.setdata(text,bitmap);
    }

    @Override
    public int getItemCount() {
        return categoryClasses.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private CircularImageView imgc;
        private TextView text;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
//            img=itemView.findViewById(R.id.imgcat);
            text=itemView.findViewById(R.id.textcat);
            imgc=itemView.findViewById(R.id.imgcat);
        }

        public void setdata(String text,int img){
//            this.img.setImageResource(img);
            this.text.setText(text);
            imgc.setImageResource(img);
        }

    }

}

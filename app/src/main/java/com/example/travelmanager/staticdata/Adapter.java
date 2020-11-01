package com.example.travelmanager.staticdata;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelmanager.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {

    List<InfoClass> infoClassList;

    public Adapter(List<InfoClass> infoClassList) {
        this.infoClassList = infoClassList;
    }


    @NonNull
    @Override
    public Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.infopage,parent,false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.Viewholder holder, int position) {
        String place=infoClassList.get(position).getNamee();
        String info=infoClassList.get(position).getInfo();
        Bitmap bitmap=infoClassList.get(position).getBitmap();
        holder.setdata(place,info,bitmap);
    }

    @Override
    public int getItemCount() {
        return infoClassList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView place,info;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            this.imageView=itemView.findViewById(R.id.image);
            this.place=itemView.findViewById(R.id.site_name);
            this.info=itemView.findViewById(R.id.info);
        }

        public void setdata(String place,String info,Bitmap bitmap){
            this.place.setText(place);
            this.imageView.setImageBitmap(bitmap);
            this.info.setText(info);
        }

    }

}

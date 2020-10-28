package com.example.travelmanager;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {

    private List<ModdleClass> moddleClassList;

    public Adapter(List<ModdleClass> moddleClassList) {
        this.moddleClassList = moddleClassList;
    }

    @NonNull
    @Override
    public Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.Viewholder holder, int position) {
        Bitmap tripphoto = moddleClassList.get(position).getTripphoto();
        Bitmap user_image = moddleClassList.get(position).getUser_image();
        int likesignal = moddleClassList.get(position).getLikesignal();
        String caption = moddleClassList.get(position).getCaption();
        String likes = moddleClassList.get(position).getLikes();
        String comment = moddleClassList.get(position).getComment();
        String username = moddleClassList.get(position).getUsername();
        holder.setdata(tripphoto,user_image,likesignal,caption,likes,comment,username);
    }

    @Override
    public int getItemCount() {
        return moddleClassList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        private ImageView trip_pic,traveler_pic,likesignal;
        private TextView caption,likes,traveler_name,comment;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            trip_pic = itemView.findViewById(R.id.tour_pic);
            traveler_pic= itemView.findViewById(R.id.traveler_dp);
            likesignal = itemView.findViewById(R.id.icon_like);
            caption = itemView.findViewById(R.id.caption);
            likes = itemView.findViewById(R.id.likes);
            traveler_name = itemView.findViewById(R.id.traveler_name);
            comment = itemView.findViewById(R.id.comment);

        }

        public void setdata(Bitmap tripphoto,Bitmap user_image,int likesignal,String caption,String likes,String comment,String username){
            trip_pic.setImageBitmap(tripphoto);
            traveler_pic.setImageBitmap(user_image);
            traveler_name.setText(username);
            this.likesignal.setImageResource(likesignal);
            this.caption.setText(caption);
            this.likes.setText(likes);
            this.comment.setText(comment);
        }

    }

}

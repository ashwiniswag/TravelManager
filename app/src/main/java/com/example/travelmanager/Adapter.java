package com.example.travelmanager;

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
        int tripphoto = moddleClassList.get(position).getTripphoto();
        int user_image = moddleClassList.get(position).getUser_image();
        int likesignal = moddleClassList.get(position).getLikesignal();
        String caption = moddleClassList.get(position).getCaption();
        String likes = moddleClassList.get(position).getLikes();
        String comment = moddleClassList.get(position).getComment();
        holder.setdata(tripphoto,user_image,likesignal,caption,likes,comment);
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

        public void setdata(int tripphoto,int user_image,int likesignal,String caption,String likes,String comment){
            trip_pic.setImageResource(tripphoto);
            traveler_pic.setImageResource(user_image);
            this.likesignal.setImageResource(likesignal);
            this.caption.setText(caption);
            this.likes.setText(likes);
            this.comment.setText(comment);
        }

    }

}

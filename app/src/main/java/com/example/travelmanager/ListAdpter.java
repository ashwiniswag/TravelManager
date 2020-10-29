package com.example.travelmanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class ListAdpter extends ArrayAdapter {

    Activity activity;
    ArrayList<String> displayname,username;
    List<String> userid;

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=activity.getLayoutInflater();
        View v=inflater.inflate(R.layout.show_follower,null);
        final CircularImageView dp=v.findViewById(R.id.dp);
        TextView uname=v.findViewById(R.id.username);
        TextView dname=v.findViewById(R.id.displayname);
        Button follow=v.findViewById(R.id.follow);

//        if(!userid.isEmpty() && userid.size()>position){
//            dp.setImageBitmap(userid.get(position).second);
            uname.setText(username.get(position));
            dname.setText(displayname.get(position));
//        }

        StorageReference ref= FirebaseStorage.getInstance().getReference().child(userid.get(position)).child("DP");
        final long ONE_MEGABYTE =1024 * 1024 * 10;
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                if(bytes!=null){
                    Bitmap bitmap=BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    dp.setImageBitmap(bitmap);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following").push();
                ref.child("Userid").setValue(userid.get(position));

                DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("User").child(userid.get(position)).child("Follower").push();
                ref2.child("Userid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

            }
        });

        return v;
    }

    public ListAdpter(Activity context, ArrayList<String> display_name, ArrayList<String> username,List<String> userid) {
        super(context,R.layout.show_follower,username);
//        this.dps=dps;
        this.displayname=display_name;
        this.username=username;
        this.activity=context;
        this.userid=userid;
    }

//    @NonNull
//    @Override
//    public String toString() {
//        return username;
//    }
}

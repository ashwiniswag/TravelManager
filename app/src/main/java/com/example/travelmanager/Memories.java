package com.example.travelmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Memories extends Fragment {

    GridView gridView;
    Adapter_Grid adapter;
    List<Bitmap> bitmap;

    public Memories() {
        // Required empty public constructor
        bitmap=new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        gridView=f
        View v=inflater.inflate(R.layout.fragment_memories, container, false);
//        return inflater.inflate(R.layout.fragment_memories, container, false);
        gridView=v.findViewById(R.id.gridview);
        adapter=new Adapter_Grid(super.getActivity(), bitmap);
        gridView.setAdapter(adapter);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Posts");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> it=snapshot.getChildren().iterator();
                StorageReference sref= FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                final long ONE_MEGABYTE =1024 * 1024 * 10;
//                Bitmap bit;
                while (it.hasNext()){
                    DataSnapshot item=it.next();
                    String s=item.child("Postid").getValue().toString();
                    sref.child(s).child(s).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            if(bytes!=null){
                                Bitmap bit= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                bitmap.add(bit);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        populat();
        return v;
    }
    
//    public populat(){
//
//    }

}

package com.example.travelmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.travelmanager.itineary.StartPlanning;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class FindFollower extends AppCompatActivity {

//    androidx.appcompat.widget.SearchView search;
    ListView list;
    ListAdpter adapter;

//    List<Pair<String,Bitmap>> userid;
    ArrayList<String> display_name,username,following,userid;
    List<Bitmap> dps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_follower);

//        search=findViewById(R.id.search);
        display_name=new ArrayList<>();
        username=new ArrayList<>();
        userid=new ArrayList<>();
        following=new ArrayList<>();
//        display_name;=new String{""};


//        dps=new ArrayList<>();
        list=findViewById(R.id.list);
        adapter=new ListAdpter(this,display_name,username,userid);
        list.setAdapter(adapter);
        getfollowing();
//        populate();

//        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
////                adapter.notifyDataSetChanged();
//                return true;
//            }
//        });

    }

    public void getfollowing(){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> it=snapshot.getChildren().iterator();
                while(it.hasNext()){
                    DataSnapshot snap=it.next();
                    following.add(snap.child("Userid").getValue().toString());
                }
                populate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void populate(){
//        Toast.makeText(FindFollower.this,"FInding",Toast.LENGTH_SHORT).show();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("UserIds");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> item=snapshot.getChildren().iterator();
                while(item.hasNext()) {
                    DataSnapshot info=item.next();
                    if(!following.contains(info.child("Userid").getValue().toString()) && !info.child("Userid").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        display_name.add(info.child("DisplayName").getValue().toString());
                        username.add(info.child("UserName").getValue().toString());
//                        userid.add(new Pair<String, Bitmap>(info.child("Userid").getValue().toString(),null));
//                        display_name.
//                        getdps(info.child("Userid").getValue().toString());
                        userid.add(info.child("Userid").getValue().toString());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    public void getdps(final String userids){
//        Toast.makeText(getApplicationContext(),"Downloading",Toast.LENGTH_SHORT).show();
//        StorageReference sref= FirebaseStorage.getInstance().getReference().child(userids).child("DP");
//        final long ONE_MEGABYTE =1024 * 1024 * 10;
//        sref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                if(bytes!=null){
//                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
////                    dps.add(bitmap);
//                    userid.add(new Pair<String, Bitmap>(userids,bitmap));
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}

package com.example.travelmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelmanager.explore.Explore;
import com.example.travelmanager.itineary.StartPlanning;
import com.example.travelmanager.maps.mapfinalactivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Iterator;

public class Profile extends AppCompatActivity{

    TextView displayName,username,bio;
    TextView trip,follower,following;
//    int t,fr,fl;
//    ImageView dp;

    BottomNavigationView bottomNavigationView;

    TabLayout tabLayout;
    TabItem tabtrip,tabmemory;
    ViewPager viewPager;

    ImageView image;

    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        trip=findViewById(R.id.trips_count);
        follower=findViewById(R.id.followers_count);
        following=findViewById(R.id.following_count);

        displayName=findViewById(R.id.display_name);
        username=findViewById(R.id.username);
        bio=findViewById(R.id.bio);

        tabLayout = findViewById(R.id.plan);
        tabtrip = findViewById(R.id.trip);
        tabmemory = findViewById(R.id.memory);
        viewPager = findViewById(R.id.viewpager);
        image = findViewById(R.id.dp);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
//        image();
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        display();

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.explore:
                        startActivity(new Intent(getApplicationContext(), Explore.class));
                        finish();
                        break;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                        break;
                    case R.id.plans:
                        startActivity(new Intent(getApplicationContext(), StartPlanning.class));
                        finish();
                        break;
                    case R.id.post:
                        startActivity(new Intent(getApplicationContext(),Post.class));
                        finish();
                        break;
                    default:
                        return false;
                }

                return true;

            }
        });

    }
//    public void setImage() {
//        this.image = image;
//    }


//    public void userinfo(final String userid){
//        Toast.makeText(getApplicationContext(),"UserInfo",Toast.LENGTH_SHORT).show();
//        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("UserInformation");
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                displayName.setText(snapshot.child("DisplayName").getValue().toString());
//                username.setText(snapshot.child("UserName").getValue().toString());
//
//                if(snapshot.child("Bio").getValue()!=null){
//                    bio.setText(snapshot.child("Bio").getValue().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
//
//
//    public void displayImage(final String userid){
//        Toast.makeText(getApplicationContext(),"DisplayImg",Toast.LENGTH_SHORT).show();
//        StorageReference sref = FirebaseStorage.getInstance().getReference().child(userid).child("DP");
//
//        final long ONE_MEGABYTE =1024 * 1024 * 10;
////        Glide.with
//        sref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                if(bytes!=null){
//                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                    image.setImageBitmap(bitmap);
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(),"This image is not present " + userid,Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void noOfTrips(final String userid){
//        Toast.makeText(getApplicationContext(),"No_ofTrips",Toast.LENGTH_SHORT).show();
//        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Posts");
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Iterator<DataSnapshot> it=snapshot.getChildren().iterator();
//                int c=0;
//                while(it.hasNext()){
//                    c++;
//                }
////                t=c;
//                trip.setText(c);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    public void noOfFollowers(final String userid){
//        Toast.makeText(getApplicationContext(),"No_ofFollowers",Toast.LENGTH_SHORT).show();
//        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Follower");
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Iterator<DataSnapshot> it=snapshot.getChildren().iterator();
//                int c=0;
//                while (it.hasNext()){
//                    c++;
//                }
//                follower.setText(c);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    public void noOfFollowing(final String userid){
//        Toast.makeText(getApplicationContext(),"No_ofFollowing",Toast.LENGTH_SHORT).show();
//        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Following");
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Iterator<DataSnapshot> it=snapshot.getChildren().iterator();
//                int c=0;
//                while (it.hasNext()){
//                    c++;
//                }
//                following.setText(c);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    public void display(){
        Toast.makeText(getApplicationContext(),"Display",Toast.LENGTH_SHORT).show();
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        userinfo(userid);
//        displayImage(userid);
//        noOfTrips(userid);
//        noOfFollowers(userid);
//        noOfFollowing(userid);


        // userinfo
        Toast.makeText(getApplicationContext(),"UserInfo",Toast.LENGTH_SHORT).show();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("UserInformation");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                displayName.setText(snapshot.child("DisplayName").getValue().toString());
                username.setText(snapshot.child("UserName").getValue().toString());

                if(snapshot.child("Bio").getValue()!=null){
                    bio.setText(snapshot.child("Bio").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // displayImg
        Toast.makeText(getApplicationContext(),"DisplayImg",Toast.LENGTH_SHORT).show();
        StorageReference sref = FirebaseStorage.getInstance().getReference().child(userid).child("DP");

        final long ONE_MEGABYTE =1024 * 1024 * 10;
        sref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                if(bytes!=null){
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    image.setImageBitmap(bitmap);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"This image is not present " + userid,Toast.LENGTH_SHORT).show();
            }
        });
//
//         noOfTrips
        Toast.makeText(getApplicationContext(),"No_ofTrips",Toast.LENGTH_SHORT).show();
        DatabaseReference ref1=FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Posts");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> it=snapshot.getChildren().iterator();
                int c=0;
                while(it.hasNext()){
                    DataSnapshot item=it.next();
                    c++;
                }
//                t=c;
                trip.setText(Integer.toString(c));
            }
//
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//
//
//        // noOfFollowers
//
        Toast.makeText(getApplicationContext(),"No_ofFollowers",Toast.LENGTH_SHORT).show();
        ref=FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Follower");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> it=snapshot.getChildren().iterator();
                int c=0;
                while (it.hasNext()){
                    c++;
                }
                follower.setText(Integer.toString(c));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed Follower",Toast.LENGTH_SHORT).show();
            }
        });
//
//        // noOfFollowing
        Toast.makeText(getApplicationContext(),"No_ofFollowing",Toast.LENGTH_SHORT).show();
        ref=FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Following");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> it=snapshot.getChildren().iterator();
                int c=0;
                while (it.hasNext()){
                    it.next();
                    c++;
                }
                following.setText(Integer.toString(c));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

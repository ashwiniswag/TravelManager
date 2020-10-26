package com.example.travelmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class Profile extends AppCompatActivity{

    TextView displayName,username,bio;
//    TextView

//    ImageView dp;


    TabLayout tabLayout;
    TabItem tabtrip,tabmemory;
    ViewPager viewPager;

    ImageView image;

    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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


    }
//    public void setImage() {
//        this.image = image;
//    }


    public void display(){
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

        StorageReference sref = FirebaseStorage.getInstance().getReference().child(userid).child("DP");

        final long ONE_MEGABYTE =1024 * 1024 * 5;
//        Glide.with
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

    }

}

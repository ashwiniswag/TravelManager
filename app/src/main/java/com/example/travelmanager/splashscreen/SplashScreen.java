package com.example.travelmanager.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.example.travelmanager.Login;
import com.example.travelmanager.MainActivity;
import com.example.travelmanager.R;
import com.example.travelmanager.Username;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    Handler handle;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar=findViewById(R.id.progress);
        progressBar.getProgress();
        handle=new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                //check();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        },1500);

    }

    public void check(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("User");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserInformation").getValue()==null){
                    startActivity(new Intent(getApplicationContext(), Username.class));
                    finish();
                }
                else{
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    startActivity(new Intent(getApplicationContext(),Profile.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

package com.example.travelmanager.itineary.addtrip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelmanager.R;

public class MainActivity extends AppCompatActivity {

    private static int splash_Time_Out = 3000;
    SharedPreferences sharedPref;
    ImageView splashScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        splashScreen = findViewById(R.id.splashScreen);
        sharedPref = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String username = sharedPref.getString("username", null);
                String password = sharedPref.getString("password", null);
                if (username == null && password == null) {
                    Intent intent = new Intent(MainActivity.this, Log_in.class);
                    startActivity(intent);
                    finish();
                } else {
                    SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(MainActivity.this);
                    String profile_email = sharedPreferencesManager.getEmail();
                    Intent intent = new Intent(MainActivity.this, NavigatonDrawer.class);
                    intent.putExtra("profileEmail", profile_email);
                    startActivity(intent);
                    finish();
                }
            }
        }, splash_Time_Out);
    }

}

package com.example.travelmanager.explore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.travelmanager.MainActivity;
import com.example.travelmanager.Post;
import com.example.travelmanager.Profile;
import com.example.travelmanager.R;
import com.example.travelmanager.itineary.DaysStore;
import com.example.travelmanager.itineary.StartPlanning;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Explore extends AppCompatActivity {

    RecyclerView category,popular_destination;
    GridView grid;

    List<CategoryClass> categoryClass;
    List<PlaceClass> placeClasses;
    List<String> plans;
    List<Integer> planpics;

    Adapter_Category cadapter;
    Adapter_Destination dadapter;
    Adapter_Place padapter;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        category=findViewById(R.id.category);
        popular_destination=findViewById(R.id.place_explore);
        grid=findViewById(R.id.destination_explore);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        category.setLayoutManager(layoutManager);

        categoryClass=new ArrayList<>();
        cadapter=new Adapter_Category(categoryClass);
        category.setAdapter(cadapter);

        LinearLayoutManager lm=new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        popular_destination.setLayoutManager(lm);
        placeClasses=new ArrayList<>();
        padapter=new Adapter_Place(placeClasses);
        popular_destination.setAdapter(padapter);
        populateplaces();
        populatecategory();
        plans=new ArrayList<>();
        planpics=new ArrayList<>();

        dadapter=new Adapter_Destination(this,plans,planpics);
        grid.setAdapter(dadapter);
        populatedestination();


        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.explore);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        break;
                    case R.id.post:
                        startActivity(new Intent(getApplicationContext(), Post.class));
                        finish();
                        break;
                    case R.id.plans:
                        startActivity(new Intent(getApplicationContext(), StartPlanning.class));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        finish();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    public void populateplaces(){
        placeClasses.add(new PlaceClass("Himanchal Pradesh","Shimla",R.drawable.shimla));
        placeClasses.add(new PlaceClass("Uttrakhand","Nanital",R.drawable.nainital));
        placeClasses.add(new PlaceClass("West Bengal","Darjeeling",R.drawable.darjeeling));
        placeClasses.add(new PlaceClass("Rajasthan","Jaipur",R.drawable.jaipur));
        placeClasses.add(new PlaceClass("Rajasthan","Udaipur",R.drawable.udaipur));
        placeClasses.add(new PlaceClass("Gujarat","Kutch",R.drawable.kutch));
        padapter.notifyDataSetChanged();
    }

    public void populatecategory(){
        categoryClass.add(new CategoryClass("Hotels",R.drawable.hotel2x));
        categoryClass.add(new CategoryClass("Market",R.drawable.market2x));
        categoryClass.add(new CategoryClass("Hospital",R.drawable.hospital2x));
        categoryClass.add(new CategoryClass("Food",R.drawable.food2x));
        categoryClass.add(new CategoryClass("School",R.drawable.school2x));
        categoryClass.add(new CategoryClass("Parks",R.drawable.park2x));
        cadapter.notifyDataSetChanged();
/*        <!--        <item>ATM Booth</item>-->
<!--        <item>Bank</item>-->
<!--        <item>Bakery</item>-->
<!--        <item>Bar</item>-->
<!--        <item>Book Store</item>-->
<!--        <item>Bus Station</item>-->
<!--        <item>Car Rental</item>-->
<!--        <item>Car Repair</item>-->
<!--        <item>Car Wash</item>-->
<!--        <item>Coffee Shop</item>-->
<!--        <item>Clothing Store</item>-->
<!--        <item>Dentist</item>-->
<!--        <item>Doctor</item>-->
<!--        <item>Electronics Store</item>-->
<!--        <item>Embassy</item>-->
<!--        <item>Fire Service</item>-->
<!--        <item>Gas Station</item>-->
<!--        <item>Govt Office</item>-->
<!--        <item>Gym</item>-->
<!--        <item>Hospital</item>-->
<!--        <item>Jewelry Store</item>-->
<!--        <item>Laundry</item>-->
<!--        <item>Mosque</item>-->
<!--        <item>Movie Theater</item>-->
<!--        <item>Park</item>-->
<!--        <item>Pharmacy</item>-->
<!--        <item>Police Nearby</item>-->
<!--        <item>Post Office</item>-->
<!--        <item>Restaurant</item>-->
<!--        <item>School</item>-->
<!--        <item>Shopping Mall</item>-->*/
    }

    public void populatedestination(){
        plans.add("Adventure Sports");
        plans.add("Beaches");
        plans.add("Heritage");
        plans.add("Hill Station");
        plans.add("Honeymoon Destination");
        plans.add("Nature Lap");
        plans.add("Pilgrimage");
        plans.add("Trekking");

        planpics.add(R.drawable.jaipur);
        planpics.add(R.drawable.jaipur);
        planpics.add(R.drawable.jaipur);
        planpics.add(R.drawable.jaipur);
        planpics.add(R.drawable.jaipur);
        planpics.add(R.drawable.jaipur);
        planpics.add(R.drawable.jaipur);
        planpics.add(R.drawable.jaipur);



//        planpics.add(R.drawable.beaches);
//        planpics.add(R.drawable.heritage);
//        planpics.add(R.drawable.hill_station);
//        planpics.add(R.drawable.honeymoon_destination);
//        planpics.add(R.drawable.nature_lap);
//        planpics.add(R.drawable.pilgrimage);
//        planpics.add(R.drawable.trekking);
        dadapter.notifyDataSetChanged();
    }
}

package com.example.travelmanager.itineary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.travelmanager.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class DaysStore extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem day1,day2,day3,day4,day5,day6;
    ViewPager viewPager;
    Adpter_Pager adpter_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_store);

        tabLayout=findViewById(R.id.day);
        day1=findViewById(R.id.day1);
        day2=findViewById(R.id.day2);
        day3=findViewById(R.id.day3);
        day4=findViewById(R.id.day4);
        day5=findViewById(R.id.day5);
        day6=findViewById(R.id.day6);

        viewPager = findViewById(R.id.viewpager);
        adpter_pager=new Adpter_Pager(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adpter_pager);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    }
}

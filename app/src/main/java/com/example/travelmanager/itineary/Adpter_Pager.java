package com.example.travelmanager.itineary;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class Adpter_Pager extends FragmentPagerAdapter {

    private int noOfTabs;

    public Adpter_Pager(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs=noOfTabs;
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                return new Day1();
            case 1:
                return new Day2();
            case 2:
                return new Day3();
            case 3:
                return new Day4();
            case 4:
                return new Day5();
            case 5:
                return new Day6();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}

package com.example.travelmanager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private int noofTabs;

    public PagerAdapter(FragmentManager fm,int noofTabs) {
        super(fm);
        this.noofTabs=noofTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                return new Trips();
            case 1:
                return new Memories();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noofTabs;
    }
}

package com.example.travelmanager.maps.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.travelmanager.maps.fragment.LocationFragment;
import com.example.travelmanager.maps.fragment.NearByFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private static final int total_pages = 2;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NearByFragment();
            case 1:
                return new LocationFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return total_pages;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Near By places";
            case 1:
                return "Your Location";
        }
        return null;
    }
}
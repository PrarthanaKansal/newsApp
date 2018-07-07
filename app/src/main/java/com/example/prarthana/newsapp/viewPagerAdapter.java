package com.example.prarthana.newsapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class viewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList= new ArrayList<>();
    List<String> tabTitles= new ArrayList<>();

    public viewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        tabTitles.add(title);
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}

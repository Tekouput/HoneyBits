package com.teko.honeybits.honeybits.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.teko.honeybits.honeybits.fragments.home.FavoritesFragment;
import com.teko.honeybits.honeybits.fragments.home.FeedFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment selectedFragment = new FeedFragment();
        Log.i("Current home tab", i + "");
        switch (i){
            case 0:
                selectedFragment = new FeedFragment();
                break;
            case 1:
                selectedFragment = new FavoritesFragment();
                break;
        }
        return selectedFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

package com.teko.honeybits.honeybits.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teko.honeybits.honeybits.R;
import com.teko.honeybits.honeybits.adapters.HomePagerAdapter;
public class HomeFragment extends Fragment {

    HomePagerAdapter homePagerAdapter;
    ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View main = inflater.inflate(R.layout.fragment_home, container, false);

        TabLayout homeFragmentMenu = main.findViewById(R.id.home_fragment_menu);
        homeFragmentMenu.addTab(homeFragmentMenu.newTab().setText("Your feed"));
        homeFragmentMenu.addTab(homeFragmentMenu.newTab().setText("Editors' Choice"));
        homeFragmentMenu.setTabGravity(TabLayout.GRAVITY_FILL);

        homePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        pager = main.findViewById(R.id.home_pager);
        pager.setAdapter(homePagerAdapter);
        pager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(homeFragmentMenu));

        homeFragmentMenu.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return main;
    }
}
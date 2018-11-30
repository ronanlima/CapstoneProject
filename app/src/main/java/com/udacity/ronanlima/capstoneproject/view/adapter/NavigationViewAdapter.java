package com.udacity.ronanlima.capstoneproject.view.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.udacity.ronanlima.capstoneproject.view.ArchitectFragment;
import com.udacity.ronanlima.capstoneproject.view.DecorationFragment;

/**
 * Created by rlima on 19/11/18.
 */

public class NavigationViewAdapter extends FragmentStatePagerAdapter {

    public static final int QUANT_FRAGMENT = 2;
    public static final int FIRST_PAGE = 0;
    public static final int SECOND_PAGE = 1;

    public NavigationViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FIRST_PAGE:
                return new ArchitectFragment();
            case SECOND_PAGE:
                return new DecorationFragment();
        }
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return QUANT_FRAGMENT;
    }
}

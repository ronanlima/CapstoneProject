package com.udacity.ronanlima.capstoneproject;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.udacity.ronanlima.capstoneproject.view.adapter.NavigationViewAdapter;
import com.udacity.ronanlima.capstoneproject.viewmodel.FirebaseViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final int ITEM_ARCHITECT = 0;
    public static final int ITEM_DECORATION = 1;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;
    private FirebaseViewModel viewModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(ITEM_ARCHITECT, true);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(ITEM_DECORATION, true);
                    return true;
            }
            return false;
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (prevMenuItem != null) {
                prevMenuItem.setChecked(false);
            } else {
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
            }
            bottomNavigationView.getMenu().getItem(position).setChecked(true);
            prevMenuItem = bottomNavigationView.getMenu().getItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.setAdapter(new NavigationViewAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewModel = ViewModelProviders.of(this).get(FirebaseViewModel.class);
        if (savedInstanceState == null) {
            viewModel.retrieveProjects();
        }
    }

}

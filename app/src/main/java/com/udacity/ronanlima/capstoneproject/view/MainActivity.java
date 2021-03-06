package com.udacity.ronanlima.capstoneproject.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.udacity.ronanlima.capstoneproject.AppExecutors;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Project;
import com.udacity.ronanlima.capstoneproject.database.AppDatabase;
import com.udacity.ronanlima.capstoneproject.view.adapter.NavigationViewAdapter;
import com.udacity.ronanlima.capstoneproject.viewmodel.FirebaseViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int ITEM_ARCHITECT = 0;
    private static final int ITEM_DECORATION = 1;
    public static final String BUNDLE_PROJECT = "BUNDLE_PROJECT";

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.fab_email)
    FloatingActionButton fabEmail;
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
        fabEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(getString(R.string.fab_mailto), getString(R.string.fab_extra_email), null));
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.fab_extra_subject));
                i.putExtra(Intent.EXTRA_TEXT, getString(R.string.fab_extra_text));
                startActivity(Intent.createChooser(i, getString(R.string.title_chooser_email)));
            }
        });
        if (savedInstanceState == null || viewModel.getDataProject().getValue() == null) {
            getDataProject();
        }
    }

    /**
     * On the first time, request the data from the FirebaseDatabase. Then, get the data from room.
     */
    private void getDataProject() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Project> projects = AppDatabase.getInstance(MainActivity.this).projectDAO().loadAllProjects();
                if (projects == null || projects.isEmpty()) {
                    viewModel.retrieveProjects(false);
                } else {
                    viewModel.getDataProject().postValue(projects);
                }
            }
        });
    }

}

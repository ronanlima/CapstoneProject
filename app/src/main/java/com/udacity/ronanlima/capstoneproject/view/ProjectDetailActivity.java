package com.udacity.ronanlima.capstoneproject.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.udacity.ronanlima.capstoneproject.AppExecutors;
import com.udacity.ronanlima.capstoneproject.MainActivity;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Image;
import com.udacity.ronanlima.capstoneproject.data.Project;
import com.udacity.ronanlima.capstoneproject.database.AppDatabase;
import com.udacity.ronanlima.capstoneproject.viewmodel.FirebaseViewModel;

import java.util.List;

/**
 * Class that represents the detail of selected project.
 *
 * @author ronanlima
 */
public class ProjectDetailActivity extends AppCompatActivity {

    private FirebaseViewModel viewModel;
    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        viewModel = ViewModelProviders.of(this).get(FirebaseViewModel.class);

        if (savedInstanceState == null) {
            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
//                    List<Project> projects = AppDatabase.getInstance(ProjectDetailActivity.this).projectDAO().loadAllProjects();
                    project = AppDatabase.getInstance(ProjectDetailActivity.this).projectDAO().loadFirstProject("0003");
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    FullViewFragment fragment = new FullViewFragment();
                    Bundle b = new Bundle();
                    b.putString(GalleryFragment.ARGUMENT_URI_IMAGE, project.getUriImagemCapa());
                    fragment.setArguments(b);
                    transaction.add(R.id.fragment_container, fragment, FullViewFragment.TAG).commit();
                }
            });

//            project = getIntent().getParcelableExtra(MainActivity.BUNDLE_PROJECT);

//            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
//                @Override
//                public void run() {
//                    List<Image> images = AppDatabase.getInstance(ProjectDetailActivity.this).imageDAO().loadAllImages(project.getId());
//                    if (images == null || images.isEmpty()) {
//                        viewModel.retrieveImages(project.getId());
//                    } else {
//                        viewModel.getDataImage().postValue(images);
//                    }
//                }
//            });
//            ProjectFragment projectFragment = createProjectFragment(project);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.add(R.id.fragment_container, projectFragment, ProjectFragment.TAG).commit();
        } else {
            project = savedInstanceState.getParcelable(MainActivity.BUNDLE_PROJECT);
        }
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            hideSystemUI();
//        } else {
//            showSystemUI();
//        }
//    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private ProjectFragment createProjectFragment(Project project) {
        ProjectFragment projectFragment = new ProjectFragment();
        Bundle b = new Bundle();
        b.putParcelable(MainActivity.BUNDLE_PROJECT, project);
        projectFragment.setArguments(b);
        return projectFragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.BUNDLE_PROJECT, project);
    }
}

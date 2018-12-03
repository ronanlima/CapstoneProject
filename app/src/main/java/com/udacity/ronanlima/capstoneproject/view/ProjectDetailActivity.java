package com.udacity.ronanlima.capstoneproject.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.udacity.ronanlima.capstoneproject.MainActivity;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Project;

/**
 * Class that represents the detail of selected project.
 *
 * @author ronanlima
 */
public class ProjectDetailActivity extends AppCompatActivity {

    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            project = getIntent().getParcelableExtra(MainActivity.BUNDLE_PROJECT);
            ProjectFragment projectFragment = createProjectFragment(project);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, projectFragment, ProjectFragment.TAG).commit();
        } else {
            project = savedInstanceState.getParcelable(MainActivity.BUNDLE_PROJECT);
        }
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

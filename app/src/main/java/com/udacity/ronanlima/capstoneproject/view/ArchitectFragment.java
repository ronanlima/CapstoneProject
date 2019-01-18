package com.udacity.ronanlima.capstoneproject.view;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Project;
import com.udacity.ronanlima.capstoneproject.enums.Proposito;
import com.udacity.ronanlima.capstoneproject.view.adapter.ProjectAdapter;
import com.udacity.ronanlima.capstoneproject.viewmodel.FirebaseViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rlima on 19/11/18.
 */

public class ArchitectFragment extends Fragment implements ProjectAdapter.OnProjectClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.shimmer_recycler_view)
    ShimmerRecyclerView shimmerRecyclerView;
    private FirebaseViewModel viewModel;
    private ProjectAdapter adapter;

    Observer observerProjects = new Observer<List<Project>>() {
        @Override
        public void onChanged(@Nullable List<Project> projects) {
            List<Project> listArchitecture = mountArchitecureList(projects);
            shimmerRecyclerView.hideShimmerAdapter();
            if (!listArchitecture.isEmpty() && adapter != null) {
                adapter.setList(projects);
            } else {
                adapter = new ProjectAdapter(ArchitectFragment.this);
                recyclerView.setAdapter(adapter);
                adapter.setList(projects);
            }
        }
    };

    @NonNull
    private List<Project> mountArchitecureList(@Nullable List<Project> projects) {
        List<Project> listArchitecture = new ArrayList<>();
        for (Project p : projects) {
            if (p.getIdProposito().equals(Proposito.ARQUITETURA.getId())) {
                listArchitecture.add(p);
            }
        }
        return listArchitecture;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_architect, container, false);
        ButterKnife.bind(this, view);
        shimmerRecyclerView.showShimmerAdapter();
        viewModel = ViewModelProviders.of(getActivity()).get(FirebaseViewModel.class);
        observeProjects();
        setRetainInstance(true);
        return view;
    }

    /**
     * Verify if exists active observer to projects and in negative case, append one.
     */
    private void observeProjects() {
        if (!viewModel.getDataProject().hasActiveObservers()) {
            viewModel.getDataProject().observe(this, observerProjects);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sglm);
    }

    @Override
    public void onItemClickListener(Project project, View... views) {
        Intent i = new Intent(getActivity(), ProjectDetailActivity.class);
        Bundle b = new Bundle();
        b.putParcelable(MainActivity.BUNDLE_PROJECT, project);
        i.putExtras(b);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), views[0], views[0].getTransitionName());
            getActivity().getWindow().setReenterTransition(new Fade());
            startActivity(i, options.toBundle());
        } else {
            startActivity(i);
        }
    }

}

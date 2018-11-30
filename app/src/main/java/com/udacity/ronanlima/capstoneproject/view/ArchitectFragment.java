package com.udacity.ronanlima.capstoneproject.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ArchitectFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    FirebaseViewModel viewModel;
    ProjectAdapter adapter;

    Observer observerProjects = new Observer<List<Project>>() {
        @Override
        public void onChanged(@Nullable List<Project> projects) {
            List<Project> listArchitecture = mountArchitecureList(projects);
            if (!listArchitecture.isEmpty() && adapter != null) {
                adapter.setList(projects);
            } else {
                adapter = new ProjectAdapter();
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
}

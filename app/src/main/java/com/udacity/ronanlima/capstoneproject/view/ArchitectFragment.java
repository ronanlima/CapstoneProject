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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Project;
import com.udacity.ronanlima.capstoneproject.enums.Proposito;
import com.udacity.ronanlima.capstoneproject.util.NetworkUtils;
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
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tv_loading)
    TextView tvLoading;
    @BindView(R.id.network_off)
    LinearLayout layoutNetworkOff;
    private FirebaseViewModel viewModel;
    private ProjectAdapter adapter;

    Observer observerProjects = new Observer<List<Project>>() {
        @Override
        public void onChanged(@Nullable List<Project> projects) {
            configProjectAdapter(projects);
        }
    };

    Observer observerUpdate = new Observer<Integer>() {
        @Override
        public void onChanged(@Nullable Integer countProjects) {
            swipeRefreshLayout.setRefreshing(false);
            hideLoadingLayout();
        }
    };

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (NetworkUtils.isConnected(getActivity())) {
                viewModel.retrieveProjects(true);
                showLoadingLayout();
            } else {
                swipeRefreshLayout.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                layoutNetworkOff.setVisibility(View.VISIBLE);
            }
        }
    };

    private void configProjectAdapter(@Nullable List<Project> projects) {
        List<Project> listArchitecture = mountArchitecureList(projects);
        shimmerRecyclerView.hideShimmerAdapter();
        if (!listArchitecture.isEmpty() && adapter != null) {
            adapter.setList(listArchitecture);
        } else {
            adapter = new ProjectAdapter(this);
            adapter.setList(listArchitecture);
        }
        recyclerView.setAdapter(adapter);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        hideLoadingLayout();
    }

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
        observerUpdate();
        if (savedInstanceState != null && viewModel.getDataProject().getValue() != null) {
            configProjectAdapter(viewModel.getDataProject().getValue());
        }
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        setRetainInstance(true);
        return view;
    }

    /**
     * Verify if exists active observer to projects and in negative case, append one.
     */
    private void observeProjects() {
        viewModel.getDataProject().observe(this, observerProjects);
    }

    private void observerUpdate() {
        viewModel.getLiveDataUpdate().observe(this, observerUpdate);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StaggeredGridLayoutManager sglm =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sglm);
        verifyInternetConnection();
    }

    private void verifyInternetConnection() {
        if (NetworkUtils.isConnected(getActivity())) {
            recyclerView.setVisibility(View.VISIBLE);
            layoutNetworkOff.setVisibility(View.GONE);
        } else {
            layoutNetworkOff.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
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

    private void showLoadingLayout() {
        recyclerView.setVisibility(View.GONE);
        tvLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoadingLayout() {
        recyclerView.setVisibility(View.VISIBLE);
        tvLoading.setVisibility(View.GONE);
        layoutNetworkOff.setVisibility(View.GONE);
    }

}

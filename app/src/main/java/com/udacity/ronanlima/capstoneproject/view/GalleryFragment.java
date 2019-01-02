package com.udacity.ronanlima.capstoneproject.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.ronanlima.capstoneproject.MainActivity;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Image;
import com.udacity.ronanlima.capstoneproject.data.Project;
import com.udacity.ronanlima.capstoneproject.view.adapter.ItemGalleryAdapter;
import com.udacity.ronanlima.capstoneproject.viewmodel.FirebaseViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class that represents the gallery of selected project.
 *
 * @author ronanlima
 * @since dezembro/2018
 */
public class GalleryFragment extends Fragment {
    public static final String TAG = GalleryFragment.class.getSimpleName().toUpperCase();

    @BindView(R.id.rv_gallery)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FirebaseViewModel viewModel;
    private ItemGalleryAdapter adapter;
    private ProjectDetailActivity activity;
    private Project project;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        project = getArguments().getParcelable(MainActivity.BUNDLE_PROJECT);
        viewModel = ViewModelProviders.of(getActivity()).get(FirebaseViewModel.class);
        configToolbar();
        setRetainInstance(true);
        return view;
    }

    /**
     * Set the toolbar
     */
    private void configToolbar() {
        activity = (ProjectDetailActivity) getActivity();
        toolbar.setTitle(project.getNomeProjeto());
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Image> images = viewModel.getDataImage().getValue();
        adapter = new ItemGalleryAdapter(images);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }
}

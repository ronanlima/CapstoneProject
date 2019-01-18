package com.udacity.ronanlima.capstoneproject.view;

import android.annotation.TargetApi;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.ronanlima.capstoneproject.AppExecutors;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Image;
import com.udacity.ronanlima.capstoneproject.data.Project;
import com.udacity.ronanlima.capstoneproject.util.NetworkUtils;
import com.udacity.ronanlima.capstoneproject.view.adapter.ImageAdapter;
import com.udacity.ronanlima.capstoneproject.viewmodel.FirebaseViewModel;
import com.udacity.ronanlima.capstoneproject.widget.ProportionThreeTwoImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class tha represents the project selected.
 *
 * @author ronanlima
 */
public class ProjectFragment extends Fragment implements ImageAdapter.OnImageItemClickListener {
    public static final String TAG = ProjectFragment.class.getSimpleName().toUpperCase();
    public static final int SLIDE_DURATION = 300;
    public static final int EXPLODE_DURATION = 300;

    private FirebaseViewModel viewModel;
    private Project project;
    @BindView(R.id.iv_principal)
    ProportionThreeTwoImageView ivPrincipal;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_info_projeto)
    TextView tvInfoProjeto;
    @BindView(R.id.rv_imagens)
    RecyclerView rvImages;
    private ProjectDetailActivity activity;
    private ImageAdapter adapter;

    /**
     * Config the adapter to display images
     *
     * @param images
     */
    private void configListImage(@Nullable List<Image> images) {
        List<Image> listAux = createReducedList(images);
        if (adapter != null && !images.isEmpty()) {
            adapter.setList(listAux);
        } else {
            adapter = new ImageAdapter(ProjectFragment.this);
            adapter.setList(listAux);
            rvImages.setHasFixedSize(true);
        }
        rvImages.setAdapter(adapter);
    }

    Observer observer = new Observer<List<Image>>() {

        @Override
        public void onChanged(@Nullable List<Image> images) {
            configListImage(images);
        }

    };

    /**
     * Creates a predefined list to show in detail project.
     *
     * @param images
     * @return
     */
    @NonNull
    private List<Image> createReducedList(@Nullable List<Image> images) {
        int length = 4;
        if (images != null && images.size() < length) {
            length = images.size();
        }
        List<Image> listAux = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            listAux.add(images.get(i));
        }
        return listAux;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_project, container, false);
        ButterKnife.bind(this, v);
        initViewModel();
        if (adapter != null) {
            configListImage(adapter.getList());
        }
        project = getArguments().getParcelable(MainActivity.BUNDLE_PROJECT);
        configToolbar();
        animateEnterTransition();
        tvInfoProjeto.setText(project.getDescricao());
        setRetainInstance(true);
        return v;
    }

    /**
     * Animate the enter animation if the version of device is highter than lollipop
     */
    private void animateEnterTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = slideInfoProject();
            getActivity().getWindow().setEnterTransition(slide);
            getActivity().getWindow().setReturnTransition(new Fade());
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    private Slide slideInfoProject() {
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.addTarget(tvInfoProjeto);
        slide.setDuration(SLIDE_DURATION);
        slide.setInterpolator(AnimationUtils.loadInterpolator(getActivity(), android.R.interpolator.linear_out_slow_in));
        return slide;
    }

    /**
     * Get view model and set an observer for images.
     */
    private void initViewModel() {
        viewModel = ViewModelProviders.of(getActivity()).get(FirebaseViewModel.class);
        if (!viewModel.getDataImage().hasActiveObservers()) {
            viewModel.getDataImage().observe(this, observer);
        }
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
        verifyInternetConnection();
    }

    /**
     * Verify if the user has internet connection and then, search the images of project. Otherwise,
     * show the appropriate layout.
     */
    private void verifyInternetConnection() {
        if (NetworkUtils.isConnected(getActivity())) {
            setImagemPrincipal();
            rvImages.hasFixedSize();
        } else {
            ivPrincipal.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_logo));
            ivPrincipal.setAlpha(0.7f);
        }
    }

    private void setImagemPrincipal() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    File file = new File(project.getUriImagemCapa());
                    final RequestCreator load = Picasso.get().load(file).placeholder(R.drawable.img_project_default);

                    try {
                        load.get();
                        if (getActivity() != null) {
                            AppExecutors.getInstance().getMainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    load.into(ivPrincipal);
                                }
                            });
                        }
                    } catch (IOException e) {
                        Log.i(TAG, getString(R.string.exception_falha_carregar_imagem, project.getUriImagemCapa()));
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void onClick() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        GalleryFragment fragment = new GalleryFragment();
        Bundle b = new Bundle();
        b.putParcelable(MainActivity.BUNDLE_PROJECT, project);
        fragment.setArguments(b);
        animateTransitionGallery();
        ft.replace(R.id.fragment_container, fragment, GalleryFragment.TAG).addToBackStack(ProjectFragment.TAG).commitAllowingStateLoss();
    }

    /**
     * Animate the transition if the version of Android is higher than lollipop
     */
    private void animateTransitionGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            explode.setDuration(EXPLODE_DURATION);
            setExitTransition(explode);
        }
    }
}

package com.udacity.ronanlima.capstoneproject.view;

import android.animation.ObjectAnimator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.ronanlima.capstoneproject.MainActivity;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Image;
import com.udacity.ronanlima.capstoneproject.data.Project;
import com.udacity.ronanlima.capstoneproject.util.NetworkUtils;
import com.udacity.ronanlima.capstoneproject.view.adapter.ImageAdapter;
import com.udacity.ronanlima.capstoneproject.viewmodel.FirebaseViewModel;
import com.udacity.ronanlima.capstoneproject.widget.ProportionThreeTwoImageView;

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

    Observer observer = new Observer<List<Image>>() {
        @Override
        public void onChanged(@Nullable List<Image> images) {
            List<Image> listAux = createReducedList(images);
            if (adapter != null && !images.isEmpty()) {
                adapter.setList(listAux);
            } else {
                adapter = new ImageAdapter(ProjectFragment.this);
                rvImages.setAdapter(adapter);
                adapter.setList(listAux);
                rvImages.setHasFixedSize(true);
            }
        }

        /**
         * Creates a predefined list to show in detail project.
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
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_project, container, false);
        ButterKnife.bind(this, v);
        initViewModel();
        project = getArguments().getParcelable(MainActivity.BUNDLE_PROJECT);
        configToolbar();
        setRetainInstance(true);
        return v;
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
        tvInfoProjeto.setText(project.getDescricao());
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    final RequestCreator load = Picasso.get().load(project.getImagemCapa()).placeholder(R.drawable.img_project_default);

                    try {
                        final Bitmap bitmap = load.get();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                load.into(ivPrincipal);
                            }
                        });
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewCompat.setTransitionName(ivPrincipal, getString(R.string.transition_cover));
                                    Palette palette = Palette.generate(bitmap, 12);
                                    final int darkMutedColor = palette.getDarkMutedColor(getResources().getColor(R.color.colorPrimary));
                                    Window window = getActivity().getWindow();
                                    int startColor = window.getStatusBarColor();
                                    ObjectAnimator.ofArgb(window, "statusBarColor", startColor, darkMutedColor).start();
                                }
                            });
                        }
                    } catch (IOException e) {
                        Log.i(TAG, getString(R.string.exception_falha_carregar_imagem, project.getImagemCapa()));
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        }).start();
    }

    @Override
    public void onClick() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        GalleryFragment fragment = new GalleryFragment();
        ft.replace(R.id.fragment_container, fragment).commit();
    }
}

package com.udacity.ronanlima.capstoneproject.view;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
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
import com.udacity.ronanlima.capstoneproject.data.Project;
import com.udacity.ronanlima.capstoneproject.util.NetworkUtils;
import com.udacity.ronanlima.capstoneproject.widget.ProportionThreeTwoImageView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class tha represents the project selected.
 *
 * @author ronanlima
 */
public class ProjectFragment extends Fragment {
    public static final String TAG = ProjectFragment.class.getSimpleName().toUpperCase();
    private Project project;
    @BindView(R.id.iv_principal)
    ProportionThreeTwoImageView ivPrincipal;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_info_projeto)
    TextView tvInfoProjeto;
    private ProjectDetailActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_project, container, false);
        ButterKnife.bind(this, v);
        project = getArguments().getParcelable(MainActivity.BUNDLE_PROJECT);
        configToolbar();
        return v;
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
        } else {
            ivPrincipal.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_logo));
            ivPrincipal.setAlpha(0.7f);
        }
    }

    private void setImagemPrincipal() {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();
    }
}

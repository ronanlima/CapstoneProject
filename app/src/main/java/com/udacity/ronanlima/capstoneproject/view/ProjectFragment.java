package com.udacity.ronanlima.capstoneproject.view;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private ProjectDetailActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_project, container, false);
        ButterKnife.bind(this, v);
        configToolbar();
        project = getArguments().getParcelable(MainActivity.BUNDLE_PROJECT);
        return v;
    }

    /**
     * Set the toolbar
     */
    private void configToolbar() {
        activity = (ProjectDetailActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle(project.getNomeProjeto());
        verifyInternetConnection();
    }

    /**
     * Verify if the user has internet connection and then, search the images of project. Otherwise,
     * show the appropriate layout.
     */
    private void verifyInternetConnection() {
        if (NetworkUtils.isConnected(getActivity())) {
            setImagemPrincipal();
        } else {
            // TODO criar layout sem conexão
        }
    }

    private void setImagemPrincipal() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final RequestCreator load = Picasso.get().load(project.getImagemCapa());

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
                                Palette palette = Palette.generate(bitmap, 12);
                                final int darkMutedColor = palette.getDarkMutedColor(getResources().getColor(R.color.colorPrimary));
//                                    Window window = getActivity().getWindow();
//                                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//                                    window.setStatusBarColor(darkMutedColor);

                                int startColor = getActivity().getWindow().getStatusBarColor();
//                                    int endColor = ContextCompat.getColor(getActivity(), darkMutedColor);
                                ObjectAnimator.ofArgb(getActivity().getWindow(), "statusBarColor", startColor, darkMutedColor).start();
                            }
                        });

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                int startColor = getWindow().getStatusBarColor();
//                int endColor = ContextCompat.getColor(context, R.color.your_color);
//                ObjectAnimator.ofArgb(getWindow(), "statusBarColor", startColor, endColor).start();
//            }
                    }
                } catch (IOException e) {
                    Log.i(TAG, getString(R.string.exception_falha_carregar_imagem, project.getImagemCapa()));
                    Log.e(TAG, e.getMessage());
                }
            }
        }).start();
    }
}

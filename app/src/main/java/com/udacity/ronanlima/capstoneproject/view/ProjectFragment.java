package com.udacity.ronanlima.capstoneproject.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;
import com.udacity.ronanlima.capstoneproject.MainActivity;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Project;
import com.udacity.ronanlima.capstoneproject.widget.ProportionThreeTwoImageView;

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
        activity = (ProjectDetailActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        project = getArguments().getParcelable(MainActivity.BUNDLE_PROJECT);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle(project.getNomeProjeto());
        Picasso.get().load(project.getImagemCapa()).into(ivPrincipal);
        Palette palette = Palette.generate(ivPrincipal.getDrawingCache(), 12);
        int darkMutedColor = palette.getDarkMutedColor(getResources().getColor(R.color.colorPrimary));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(darkMutedColor);

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                int startColor = getWindow().getStatusBarColor();
//                int endColor = ContextCompat.getColor(context, R.color.your_color);
//                ObjectAnimator.ofArgb(getWindow(), "statusBarColor", startColor, endColor).start();
//            }
        }
    }
}

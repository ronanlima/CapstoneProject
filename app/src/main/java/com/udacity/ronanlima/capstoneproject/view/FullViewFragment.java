package com.udacity.ronanlima.capstoneproject.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.ronanlima.capstoneproject.AppExecutors;
import com.udacity.ronanlima.capstoneproject.R;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class that represents the full view of selected image
 *
 * @author ronanlima
 * @since janeiro/2019
 */
public class FullViewFragment extends Fragment {
    public static final String TAG = FullViewFragment.class.getSimpleName().toUpperCase();

    @BindView(R.id.iv_full_view)
    ImageView ivFullView;
    private float mScaleFactor = 1.0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_view, container, false);
        ButterKnife.bind(this, view);
        final String uriImage = getArguments().getString(GalleryFragment.ARGUMENT_URI_IMAGE);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                File file = new File(uriImage);
                final RequestCreator load = Picasso.get().load(file);
                try {
                    load.get();
                    AppExecutors.getInstance().getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            load.into(ivFullView);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(1f, Math.min(mScaleFactor, 5.0f));
            ivFullView.setScaleX(mScaleFactor);
            ivFullView.setScaleY(mScaleFactor);
            return true;
        }
    }

}

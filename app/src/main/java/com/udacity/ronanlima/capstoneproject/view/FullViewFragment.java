package com.udacity.ronanlima.capstoneproject.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
public class FullViewFragment extends Fragment implements View.OnSystemUiVisibilityChangeListener {
    public static final String TAG = FullViewFragment.class.getSimpleName().toUpperCase();

    @BindView(R.id.iv_full_view)
    ImageView ivFullView;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_view, container, false);
        mScaleGestureDetector = new ScaleGestureDetector(getActivity(), new ScaleListener());
        fullScreen();
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
        ivFullView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mScaleGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
        return view;
    }

    private void fullScreen() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
            ivFullView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            ivFullView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(1f, Math.min(mScaleFactor, 10.0f));
            ivFullView.setScaleX(mScaleFactor);
            ivFullView.setScaleY(mScaleFactor);
            return true;
        }
    }

}

package com.udacity.ronanlima.capstoneproject.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udacity.ronanlima.capstoneproject.PicassoDiskImageLoader;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Image;
import com.udacity.ronanlima.capstoneproject.viewmodel.FirebaseViewModel;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;

import java.util.ArrayList;
import java.util.List;

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

    @BindView(R.id.scroll_gallery_view)
    ScrollGalleryView scrollGalleryView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private List<Image> images;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_view, container, false);
        ButterKnife.bind(this, view);
        FirebaseViewModel viewModel = ViewModelProviders.of(getActivity()).get(FirebaseViewModel.class);
        images = viewModel.getDataImage().getValue();
        configToolbar();
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int position = getArguments().getInt(GalleryFragment.ARGUMENT_POSITION_IMAGE, 0);
        final List<MediaInfo> infos = new ArrayList<>(FullViewFragment.this.images.size());
        for (Image img : images) {
            String uriImagem = img.getUriImagem();
            if (uriImagem != null && !uriImagem.isEmpty()) {
                infos.add(MediaInfo.mediaLoader(new PicassoDiskImageLoader(uriImagem)));
            }
        }

        if (!infos.isEmpty()) {
            buildView(position, infos);
        } else {
            buildEmptyGallery();
        }
    }

    /**
     * Build a view with images
     *
     * @param position
     * @param infos
     */
    private void buildView(int position, List<MediaInfo> infos) {
        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(true)
                .setFragmentManager(getFragmentManager())
                .addMedia(infos);
        scrollGalleryView.getViewPager().setCurrentItem(position);
    }

    /**
     * Build an empty view
     */
    private void buildEmptyGallery() {
        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(false)
                .setFragmentManager(getFragmentManager())
                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.img_project_default)));
        scrollGalleryView.hideThumbnails();
        Toast.makeText(getActivity(), getString(R.string.toast_empty_gallery), Toast.LENGTH_SHORT).show();
    }


    /**
     * Set the toolbar
     */
    private void configToolbar() {
        ProjectDetailActivity activity = (ProjectDetailActivity) getActivity();
        toolbar.setTitle("");
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}

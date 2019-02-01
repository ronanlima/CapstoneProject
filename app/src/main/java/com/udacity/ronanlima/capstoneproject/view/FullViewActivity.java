package com.udacity.ronanlima.capstoneproject.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
 * Created by rlima on 30/01/19.
 */

public class FullViewActivity extends AppCompatActivity {
    @BindView(R.id.scroll_gallery_view)
    ScrollGalleryView scrollGalleryView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private List<Image> images;
    private FirebaseViewModel viewModel;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_full_view);

        ButterKnife.bind(this);
        position = getIntent().getIntExtra(GalleryFragment.ARGUMENT_POSITION_IMAGE, 0);
        String projectId = getIntent().getStringExtra(GalleryFragment.ARGUMENT_PROJECT_ID);
        viewModel = ViewModelProviders.of(this).get(FirebaseViewModel.class);
        configObserverImage();
        configToolbar();
        viewModel.retrieveImagesFromLocal(projectId);
    }

    private void configObserverImage() {
        if (!viewModel.getDataImage().hasActiveObservers()) {
            viewModel.getDataImage().observe(this, new Observer<List<Image>>() {
                @Override
                public void onChanged(@Nullable List<Image> images) {
                    final List<MediaInfo> infos = new ArrayList<>(images.size());
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
            });
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
                .setFragmentManager(getSupportFragmentManager())
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
                .setFragmentManager(getSupportFragmentManager())
                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.img_project_default)));
        scrollGalleryView.hideThumbnails();
        Toast.makeText(this, getString(R.string.toast_empty_gallery), Toast.LENGTH_SHORT).show();
    }


    /**
     * Set the toolbar
     */
    private void configToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}

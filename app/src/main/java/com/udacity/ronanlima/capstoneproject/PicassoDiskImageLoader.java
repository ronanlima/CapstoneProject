package com.udacity.ronanlima.capstoneproject;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import java.io.File;

public class PicassoDiskImageLoader implements MediaLoader {
    private String uri;
    private Integer thumbnailWidth;
    private Integer thumbnailHeight;

    public PicassoDiskImageLoader(String uri) {
        this.uri = uri;
    }

    public PicassoDiskImageLoader(String uri, Integer thumbnailWidth, Integer thumbnailHeight) {
        this.uri = uri;
        this.thumbnailWidth = thumbnailWidth;
        this.thumbnailHeight = thumbnailHeight;
    }

    @Override
    public boolean isImage() {
        return true;
    }

    @Override
    public void loadMedia(Context context, final ImageView imageView, final MediaLoader.SuccessCallback callback) {
        File file = new File(uri);
        Picasso.get()
                .load(file)
                .placeholder(R.drawable.placeholder_image)
                .into(imageView, new ImageCallback(callback));
    }

    @Override
    public void loadThumbnail(Context context, final ImageView thumbnailView, final MediaLoader.SuccessCallback callback) {
        thumbnailView.setContentDescription(uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf(".")));
        File file = new File(uri);
        Picasso.get()
                .load(file)
                .resize(thumbnailWidth == null ? 100 : thumbnailWidth,
                        thumbnailHeight == null ? 100 : thumbnailHeight)
                .placeholder(R.drawable.placeholder_image)
                .centerInside()
                .into(thumbnailView, new ImageCallback(callback));
    }

    private static class ImageCallback implements Callback {
        private final MediaLoader.SuccessCallback callback;

        public ImageCallback(SuccessCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onSuccess() {
            callback.onSuccess();
        }

        @Override
        public void onError(Exception e) {

        }

    }
}

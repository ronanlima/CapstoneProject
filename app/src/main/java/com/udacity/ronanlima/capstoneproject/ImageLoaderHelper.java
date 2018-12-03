package com.udacity.ronanlima.capstoneproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by rlima on 30/11/18.
 */

public class ImageLoaderHelper {
    private static ImageLoaderHelper sInstance;

    public static ImageLoaderHelper getInstance(Context context, DownloadListener downloadListener) {
        if (sInstance == null) {
            sInstance = new ImageLoaderHelper(context.getApplicationContext(), downloadListener);
        }

        return sInstance;
    }

    private final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(20);
    private ImageLoader mImageLoader;
    private DownloadListener downloadListener;

    private ImageLoaderHelper(Context applicationContext, final DownloadListener downloadListener) {
        RequestQueue queue = Volley.newRequestQueue(applicationContext);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                downloadListener.onDownloadComplete(value);
                mImageCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return mImageCache.get(key);
            }
        };
        mImageLoader = new ImageLoader(queue, imageCache);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public interface DownloadListener {
        void onDownloadComplete(Bitmap bitmap);
    }
}

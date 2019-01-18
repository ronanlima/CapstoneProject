package com.udacity.ronanlima.capstoneproject.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.ronanlima.capstoneproject.AppExecutors;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Image;
import com.udacity.ronanlima.capstoneproject.database.AppDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.supercharge.shimmerlayout.ShimmerLayout;
import lombok.Getter;

/**
 * Created in dec/2018
 *
 * @author ronanlima
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private static final String TAG = ImageAdapter.class.getSimpleName().toUpperCase();
    public static final String FILE_EXTENSION = ".png";
    public static final int COMPRESS_QUALITY = 100;

    private Context mContext;
    @Getter
    private List<Image> list;
    private OnImageItemClickListener clickListener;

    public ImageAdapter(OnImageItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        final Image image = list.get(position);
        if (image.getUriImagem() == null || image.getUriImagem().isEmpty()) {
            retrieveImageFromNetwork(holder, image);
        } else {
            retrieveImageFromAppDirectory(holder, image);
        }
        holder.ivItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick();
            }
        });
    }

    /**
     * Retrieve image from directory when they already exists on device.
     *
     * @param holder
     * @param image
     */
    private void retrieveImageFromAppDirectory(@NonNull final ImageViewHolder holder, final Image image) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                File file = new File(image.getUriImagem());
                final RequestCreator load = Picasso.get().load(file);
                try {
                    load.get();
                    AppExecutors.getInstance().getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            displayImage(load, holder);
                            changeContentDescriptionOfImage(image.getNome(), holder);
                        }
                    });
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * Change the default content description of poster project.
     *
     * @param imageName
     * @param holder
     */
    private void changeContentDescriptionOfImage(String imageName, ImageViewHolder holder) {
        holder.ivItem.setContentDescription(imageName);
    }

    /**
     * Set the image into imageView and stop shimmer animation
     *
     * @param load
     * @param holder
     */
    private void displayImage(RequestCreator load, @NonNull ImageViewHolder holder) {
        load.into(holder.ivItem);
        holder.shimmerLayout.stopShimmerAnimation();
        holder.shimmerLayout.setVisibility(View.GONE);
    }

    /**
     * Retrieve image from network, when that dont exist locally.
     *
     * @param holder
     * @param image
     */
    private void retrieveImageFromNetwork(@NonNull final ImageViewHolder holder, final Image image) {
        AppExecutors.getInstance().getNetworkIO().execute(new Runnable() {
            @Override
            public void run() {
                final RequestCreator load = Picasso.get().load(image.getUrlImagem());
                try {
                    Bitmap bitmap = load.get();
                    AppExecutors.getInstance().getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            displayImage(load, holder);
                            changeContentDescriptionOfImage(image.getNome(), holder);
                        }
                    });
                    saveImageLocally(bitmap, image);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void saveImageLocally(Bitmap bitmap, final Image image) throws FileNotFoundException {
        String fileName = String.format("%s_%s%s", image.getIdProjeto(), image.getNome(), FILE_EXTENSION);
        File file = new File(mContext.getFilesDir(), fileName);
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS_QUALITY, fos);
        image.setUriImagem(file.getPath());
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(mContext).imageDAO().insertImage(image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<Image> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_item)
        ImageView ivItem;
        @BindView(R.id.shimmer_placeholder)
        ShimmerLayout shimmerLayout;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnImageItemClickListener {
        void onClick();
    }
}

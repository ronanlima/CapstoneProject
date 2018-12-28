package com.udacity.ronanlima.capstoneproject.view.adapter;

import android.content.Context;
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

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.supercharge.shimmerlayout.ShimmerLayout;
import lombok.Getter;

/**
 * This class represents the view of each image on gallery
 *
 * @author ronanlima
 * @since dezembro/2018
 */
public class ItemGalleryAdapter extends RecyclerView.Adapter<ItemGalleryAdapter.ItemGalleryViewHolder> {
    public static final String TAG = ItemGalleryAdapter.class.getSimpleName().toUpperCase();

    private Context mContext;
    @Getter
    private List<Image> list;

    public ItemGalleryAdapter(List<Image> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ItemGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_gallery, parent, false);
        return new ItemGalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemGalleryViewHolder holder, int position) {
        Image image = list.get(position);
        retrieveImageFromAppDirectory(holder, image);
    }

    /**
     * Retrieve image from directory.
     *
     * @param holder
     * @param image
     */
    private void retrieveImageFromAppDirectory(@NonNull final ItemGalleryAdapter.ItemGalleryViewHolder holder, final Image image) {
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
                        }
                    });
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * Set the image into imageView and stop shimmer animation
     *
     * @param load
     * @param holder
     */
    private void displayImage(RequestCreator load, @NonNull ItemGalleryAdapter.ItemGalleryViewHolder holder) {
        load.into(holder.ivItem);
        holder.shimmerLayout.stopShimmerAnimation();
        holder.shimmerLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setList(List<Image> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ItemGalleryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_item)
        ImageView ivItem;
        @BindView(R.id.shimmer_placeholder)
        ShimmerLayout shimmerLayout;

        public ItemGalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

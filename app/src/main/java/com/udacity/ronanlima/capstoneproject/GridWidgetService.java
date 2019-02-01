package com.udacity.ronanlima.capstoneproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.squareup.picasso.Picasso;
import com.udacity.ronanlima.capstoneproject.data.Image;
import com.udacity.ronanlima.capstoneproject.data.Project;
import com.udacity.ronanlima.capstoneproject.database.AppDatabase;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewFactory(getApplicationContext());
    }
}

class GridRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = GridRemoteViewFactory.class.getSimpleName().toUpperCase();

    Context mContext;
    List<Image> images;

    public GridRemoteViewFactory(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        List<Project> projects = AppDatabase.getInstance(mContext).projectDAO().loadAllProjects();
        if (projects == null) {
            return;
        }
        Random r = new Random();
        int nextProject = r.nextInt(projects.size() - 1);
        images = AppDatabase.getInstance(mContext).imageDAO().loadAllImagesFromProject(projects.get(nextProject).getId());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return images == null ? 0 : images.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (images == null || images.isEmpty()) {
            return null;
        }

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.image_widget);
        String uriImagem = images.get(position).getUriImagem();
        if (uriImagem == null || uriImagem.isEmpty()) {
            return views;
        }
        File file = new File(uriImagem);
        try {
            Bitmap bitmap = Picasso.get().load(file).get();
            views.setImageViewBitmap(R.id.widget_image, bitmap);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

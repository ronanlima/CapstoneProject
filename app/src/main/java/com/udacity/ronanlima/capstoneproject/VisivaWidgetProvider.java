package com.udacity.ronanlima.capstoneproject;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;
import com.udacity.ronanlima.capstoneproject.data.Image;
import com.udacity.ronanlima.capstoneproject.database.AppDatabase;
import com.udacity.ronanlima.capstoneproject.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class VisivaWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        RemoteViews views;
        if (width < 300) {
            List<Image> images = AppDatabase.getInstance(context).imageDAO().loadAllImages();
            String uriImagem = "";
            if (images != null && !images.isEmpty()) {
                int nextRandom = Util.getNextRandom(images);
                uriImagem = images.get(nextRandom).getUriImagem();
            }
            views = getSingleProjectRemoteView(context, uriImagem);
        } else {
            views = getProjectGridView(context);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews getProjectGridView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);
        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);
        return views;
    }

    @NonNull
    private static RemoteViews getSingleProjectRemoteView(Context context, String uriImagem) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.image_widget);
        if (!uriImagem.isEmpty()) {
            File file = new File(uriImagem);
            try {
                Bitmap bitmap = Picasso.get().load(file).get();
                views.setImageViewBitmap(R.id.widget_image, bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return views;
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        VisivaArqService.startActionUpdateWidget(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        VisivaArqService.startActionUpdateWidget(context);
    }

    public static void updateVisivaWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, id);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


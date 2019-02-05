package com.udacity.ronanlima.capstoneproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.udacity.ronanlima.capstoneproject.data.Project;
import com.udacity.ronanlima.capstoneproject.database.AppDatabase;
import com.udacity.ronanlima.capstoneproject.view.MainActivity;
import com.udacity.ronanlima.capstoneproject.view.ProjectDetailActivity;

import java.io.File;
import java.io.IOException;

/**
 * Implementation of App Widget functionality.
 */
public class VisivaWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String projectId) {

        if (projectId != null && !projectId.isEmpty()) {
            Project project = AppDatabase.getInstance(context).projectDAO().loadProjectById(projectId);
            RemoteViews views = getSingleProjectRemoteView(context, project);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @NonNull
    private static RemoteViews getSingleProjectRemoteView(Context context, Project project) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.image_widget);

        if (project.getUriImagemCapa() != null && !project.getUriImagemCapa().isEmpty()) {
            File file = new File(project.getUriImagemCapa());
            try {
                Bitmap bitmap = Picasso.get().load(file).get();
                views.setImageViewBitmap(R.id.widget_image, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            RequestCreator load = Picasso.get().load(project.getImagemCapa());
            try {
                Bitmap bitmap = load.get();
                views.setImageViewBitmap(R.id.widget_image, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PendingIntent pendingIntent = createPendingIntent(context, project);
        views.setOnClickPendingIntent(R.id.layout_widget, pendingIntent);
        views.setEmptyView(R.id.layout_widget_geral, R.id.empty_view);
        return views;
    }

    private static PendingIntent createPendingIntent(Context context, Project project) {
        Intent i = new Intent(context, ProjectDetailActivity.class);
        Bundle b = new Bundle();
        b.putParcelable(MainActivity.BUNDLE_PROJECT, project);
        i.putExtras(b);
        return PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        VisivaArqService.startActionUpdateWidget(context);
    }

    public static void updateVisivaWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, String projectId) {
        for (int id : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, id, projectId);
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


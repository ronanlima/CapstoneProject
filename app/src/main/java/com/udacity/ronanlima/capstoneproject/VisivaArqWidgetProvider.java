package com.udacity.ronanlima.capstoneproject;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.udacity.ronanlima.capstoneproject.data.Project;
import com.udacity.ronanlima.capstoneproject.database.AppDatabase;
import com.udacity.ronanlima.capstoneproject.widget.ListWidgetService;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class VisivaArqWidgetProvider extends AppWidgetProvider {

    private static Bundle extras;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.visiva_arq_widget);
        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtras(extras);
        views.setRemoteAdapter(R.id.list_message, intent);
        views.setEmptyView(R.id.list_message, R.id.empty_view);
//        String quantString = String.format("%02d", quantProjects);
//        String quantProjFmt = context.getString(R.string.widget_info_quant_proj, quantString);
//        views.setTextViewText(R.id.tv_info_quant_proj, quantProjFmt);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(final Context context) {
//        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                List<Project> projects = AppDatabase.getInstance(context).projectDAO().loadAllProjects();
//                quantProjects = projects.size();
//                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, VisivaArqWidgetProvider.class));
//                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.tv_info_quant_proj);
//            }
//        });
    }

    public static void updateVisivaArqWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Bundle bundle) {
        extras = bundle;
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


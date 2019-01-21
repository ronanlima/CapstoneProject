package com.udacity.ronanlima.capstoneproject;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.ronanlima.capstoneproject.widget.ListWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class VisivaArqWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.visiva_arq_widget);
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.list_message, intent);
        views.setEmptyView(R.id.list_message, R.id.empty_view);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        VisivaArqService.startActionUpdateWidget(context);
    }

    @Override
    public void onEnabled(final Context context) {

    }

    public static void updateVisivaArqWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDisabled(Context context) {

    }
}


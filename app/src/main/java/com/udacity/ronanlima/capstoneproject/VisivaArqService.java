package com.udacity.ronanlima.capstoneproject;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by rlima on 18/01/19.
 */

public class VisivaArqService extends IntentService {
    public static final String ACTION_UPDATE_WIDGET = "com.udacity.ronanlima.capstoneproject.update_widget";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public VisivaArqService() {
        super("VisivaArqService");
    }

    public static void startActionUpdateWidget(Context context) {
        Intent i = new Intent(context, VisivaArqService.class);
        i.setAction(ACTION_UPDATE_WIDGET);
        context.startService(i);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals(ACTION_UPDATE_WIDGET)) {
                handleActionUpdateWidget();
            }
        }
    }

    private void handleActionUpdateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, VisivaWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        VisivaWidgetProvider.updateVisivaWidget(this, appWidgetManager, appWidgetIds, getProjectId());
    }

    @NonNull
    private String getProjectId() {
        SharedPreferences sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        return sharedPreferences.getString(BuildConfig.ID_LAST_SELECTED_PROJECT, "");
    }
}

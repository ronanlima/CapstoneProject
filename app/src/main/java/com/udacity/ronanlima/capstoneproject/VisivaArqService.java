package com.udacity.ronanlima.capstoneproject;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by rlima on 18/01/19.
 */

public class VisivaArqService extends IntentService {
    public static final String ACTION_UPDATE_QUANT_PROJ = "com.udacity.ronanlima.capstoneproject.update_quant_proj";
    public static final String ACTION_UPDATE_WIDGET = "com.udacity.ronanlima.capstoneproject.update_widget";
    public static final String BUNDLE_QUANT_PROJ = "BUNDLE_QUANT_PROJ";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public VisivaArqService() {
        super("VisivaArqService");
    }

    public static void startActionUpdateQuantProj(Context context, Bundle bundle) {
        Intent intent = new Intent(context, VisivaArqService.class);
        intent.setAction(ACTION_UPDATE_QUANT_PROJ);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    public static void startActionUpdateWidget(Context context) {
        Intent i = new Intent(context, VisivaArqService.class);
        i.setAction(ACTION_UPDATE_WIDGET);
        context.startService(i);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals(ACTION_UPDATE_QUANT_PROJ)) {
                handleActionUpdateQuantProj(intent.getExtras());
            } else if (intent.getAction().equals(ACTION_UPDATE_WIDGET)) {
                handleActionUpdateWidget();
            }
        }
    }

    private void handleActionUpdateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, VisivaArqWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_message);
        VisivaArqWidgetProvider.updateVisivaArqWidget(this, appWidgetManager, appWidgetIds);
    }

    private void handleActionUpdateQuantProj(Bundle extras) {
        updatePreferences(extras);
        startActionUpdateWidget(this);
    }

    private void updatePreferences(Bundle bundle) {
        SharedPreferences sharedPreferences = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int quantProj = bundle.getInt(BUNDLE_QUANT_PROJ, 1);
        String fmt = String.format("%02d", quantProj);
        String quantFmt = getApplicationContext().getString(R.string.widget_info_quant_proj, fmt);
        editor.putString(BuildConfig.PREF_QUANT_PROJ, quantFmt);
        editor.commit();
    }
}

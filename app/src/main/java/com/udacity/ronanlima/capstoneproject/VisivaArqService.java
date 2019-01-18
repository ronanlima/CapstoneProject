package com.udacity.ronanlima.capstoneproject;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by rlima on 18/01/19.
 */

public class VisivaArqService extends IntentService {
    public static final String TAG = VisivaArqService.class.getSimpleName().toUpperCase();
    public static final String ACTION_UPDATE_QUANT_PROJ = "com.udacity.ronanlima.capstoneproject.update_quant_proj";
    public static final String BUNDLE_QUANT_PROJ = "BUNDLE_QUANT_PROJ";
    public static final String BUNDLE_MESSAGE_ONE = "BUNDLE_MESSAGE_ONE";
    public static final String BUNDLE_MESSAGE_TWO = "BUNDLE_MESSAGE_TWO";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public VisivaArqService() {
        super("VisivaArqService");
    }

    public static void updateQuantProj(Context context, Bundle bundle) {
        Intent intent = new Intent(context, VisivaArqService.class);
        intent.setAction(ACTION_UPDATE_QUANT_PROJ);
        intent.putExtras(bundle);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            if (intent.getAction().equals(ACTION_UPDATE_QUANT_PROJ)) {
                handleActionUpdateQuantProj(intent.getExtras());
            }
        }
    }

    private void handleActionUpdateQuantProj(Bundle extras) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, VisivaArqWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_message);
        updateBundle(extras);
        VisivaArqWidgetProvider.updateVisivaArqWidget(this, appWidgetManager, appWidgetIds, extras);
    }

    private void updateBundle(Bundle bundle) {
        int quantProj = bundle.getInt(BUNDLE_QUANT_PROJ, 1);
        String fmt = String.format("%02d", quantProj);
        String quantFmt = getApplicationContext().getString(R.string.widget_info_quant_proj, fmt);
        bundle.putString(BUNDLE_MESSAGE_ONE, quantFmt);
        bundle.putString(BUNDLE_MESSAGE_TWO, getApplicationContext().getString(R.string.tv_widget_click_here));
    }
}

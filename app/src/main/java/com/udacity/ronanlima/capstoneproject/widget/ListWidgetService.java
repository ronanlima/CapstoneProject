package com.udacity.ronanlima.capstoneproject.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.ronanlima.capstoneproject.BuildConfig;
import com.udacity.ronanlima.capstoneproject.R;

/**
 * Created by rlima on 18/01/19.
 */

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewFactory(this.getApplicationContext());
    }
}

class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    Context mContext;
    private String messageFmt;

    public ListRemoteViewFactory(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        messageFmt = sharedPreferences.getString(BuildConfig.PREF_QUANT_PROJ, "01");
    }

    @Override
    public void onDestroy() {
        messageFmt = null;
    }

    @Override
    public int getCount() {
        return messageFmt == null ? 0 : 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (messageFmt == null) {
            return null;
        }

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.adapter_widget);
        views.setTextViewText(R.id.tv_widget_adapter, messageFmt);
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

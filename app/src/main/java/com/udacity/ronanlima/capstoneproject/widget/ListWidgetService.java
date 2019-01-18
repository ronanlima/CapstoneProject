package com.udacity.ronanlima.capstoneproject.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.VisivaArqService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rlima on 18/01/19.
 */

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        if (intent != null) {
            List<String> messages = new ArrayList<>();
            messages.add(intent.getStringExtra(VisivaArqService.BUNDLE_MESSAGE_ONE));
            messages.add(intent.getStringExtra(VisivaArqService.BUNDLE_MESSAGE_TWO));
            return new ListRemoteViewFactory(this.getApplicationContext(), messages);
        } else {
            return new ListRemoteViewFactory(this.getApplicationContext(), null);
        }
    }
}

class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    Context mContext;
    private List<String> messages;

    public ListRemoteViewFactory(Context context, List<String> messages) {
        this.mContext = context;
        this.messages = messages;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        messages = null;
    }

    @Override
    public int getCount() {
        return messages == null ? 0 : messages.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (messages == null) {
            return null;
        }

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.adapter_widget);
        views.setTextViewText(R.id.tv_widget_adapter, messages.get(position));
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

package com.stream.jerye.queue.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.stream.jerye.queue.R;
import com.stream.jerye.queue.room.RoomActivity;

/**
 * Created by jerye on 6/30/2017.
 */

public class QueueWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_WIDGET_CONNECTION = "com.stream.jerye.queue.firebase.ACTION_WIDGET_CONNECTION";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {

            Log.d("Widget", appWidgetId + " created");
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, RoomActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider_layout);
            Log.d("Widget", appWidgetId + " id");
            Intent adapterIntent = new Intent(context, QueueWidgetRemoteService.class).putExtra("appWidgetIds", appWidgetIds);
            views.setRemoteAdapter(R.id.widget_list_view, adapterIntent);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("Widget", "onReceive");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, getClass()));
        Log.d("Widget", appWidgetIds.length + " length");
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
    }
}

package com.udacity.stockhawk.ui.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.sync.QuoteSyncJob;
import com.udacity.stockhawk.ui.history.StockDetailsChartActivity;

import timber.log.Timber;


/**
 * Created by carvalhorr on 4/7/17.
 */

public class StockWidgetProvider extends AppWidgetProvider {

    private static final String WIDGET_STOCK_ITEM_CLICKED = "com.udacity.stockhawk.ACTION_STOCK_WIDGET_ITEM_CLICKED";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (QuoteSyncJob.ACTION_DATA_UPDATED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stock_list);

        } else if (WIDGET_STOCK_ITEM_CLICKED.equals(intent.getAction())) {

            String symbol = intent.getStringExtra(StockDetailsChartActivity.EXTRA_STOCK_SYMBOL);
            StockDetailsChartActivity.startActivity(context, symbol);

        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int i = 0; i < appWidgetIds.length; ++i) {

            // intent to connect to the RemoteViewsService
            Intent intent = new Intent(context, StockWidgetRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            // set list remove views
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_stock);
            rv.setRemoteAdapter(R.id.stock_list, intent);
            rv.setEmptyView(R.id.stock_list, R.id.error_message);

            // set pending intent template to handle item click on the widget
            Intent clickIntentTemplate = new Intent(context, StockDetailsChartActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.stock_list, clickPendingIntentTemplate);

            // update the widget
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }

    }




}

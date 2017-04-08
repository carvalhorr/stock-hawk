package com.udacity.stockhawk.ui.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.history.StockDetailsChartActivity;
import com.udacity.stockhawk.util.FormatUtil;

/**
 * Created by carvalhorr on 4/7/17.
 */
public class StockWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockRemoteViewsFactory(getApplicationContext(), intent);
    }

}

class StockRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor stockQuotes;

    private Context context;
    private int appWidgetId;


    public StockRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        // save and clear current calling identity
        final long identityToken = Binder.clearCallingIdentity();

        // close current cursor
        if (stockQuotes != null) {
            stockQuotes.close();
        }

        // query new data
        stockQuotes = context.getContentResolver().query(Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[Contract.Quote.QUOTE_COLUMNS.size()]),
                null,
                null,
                Contract.Quote.COLUMN_SYMBOL + " ASC");

        // restore calling identity
        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {
        if (stockQuotes != null) {
            stockQuotes.close();
            stockQuotes = null;
            context = null;
        }
    }

    @Override
    public int getCount() {
        return (stockQuotes != null) ? stockQuotes.getCount() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        stockQuotes.moveToPosition(position);

        // create the remoteviews for the item
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.list_item_quote);

        // set values
        String symbol = stockQuotes.getString(Contract.Quote.POSITION_SYMBOL);
        rv.setTextViewText(R.id.symbol, symbol);
        rv.setTextViewText(R.id.change, FormatUtil.formatPercentage(stockQuotes.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE) / 100));

        // change style
        rv.setViewVisibility(R.id.price, View.GONE);
        if (stockQuotes.getInt(Contract.Quote.POSITION_VALID) == 0) {
            rv.setInt(R.id.stock_item_root_view, "setBackgroundResource", R.drawable.non_existing_symbol);
            rv.setViewVisibility(R.id.message, View.VISIBLE);
            rv.setViewVisibility(R.id.quote_info, View.GONE);
        } else {
            rv.setInt(R.id.stock_item_root_view, "setBackgroundResource", 0);
            rv.setViewVisibility(R.id.message, View.GONE);
            rv.setViewVisibility(R.id.quote_info, View.VISIBLE);
        }

        float rawAbsoluteChange = stockQuotes.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
        if (rawAbsoluteChange > 0) {
            rv.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
        } else {
            rv.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
        }

        // handle click

        final Intent fillInIntent = new Intent();
        fillInIntent.putExtra(StockDetailsChartActivity.EXTRA_STOCK_SYMBOL, symbol);
        rv.setOnClickFillInIntent(R.id.symbol, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
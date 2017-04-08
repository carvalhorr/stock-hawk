package com.udacity.stockhawk.ui.history;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StockDetailsChartActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Entry>> {

    private static final int STOCK_HISTORY_LOADER = 2;
    public static final String EXTRA_STOCK_SYMBOL = "symbol";

    private String stockSymbol;

    @BindView(R.id.stock_chart)
    LineChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stock_details_chart);

        ButterKnife.bind(this);

        // get the stock symbol
        if (!getIntent().hasExtra(EXTRA_STOCK_SYMBOL)) {
            throw new RuntimeException(getString(R.string.runtime_message_missing_stock_symbol));
        }
        stockSymbol = getIntent().getStringExtra(EXTRA_STOCK_SYMBOL);

        setTitle(stockSymbol.toUpperCase());

        getLoaderManager().initLoader(STOCK_HISTORY_LOADER, null, this);

    }

    @Override
    public Loader<List<Entry>> onCreateLoader(int id, Bundle args) {
        return new StockChartDataAsyncTaskLoader(this, stockSymbol);
    }

    @Override
    public void onLoadFinished(Loader<List<Entry>> loader, List<Entry> data) {

        if (data.size() > 0) {

            LineDataSet lineDataSet = new LineDataSet(data, getString(R.string.stock_history_chart_label));

            lineDataSet.setDrawCircles(false);
            lineDataSet.setMode(LineDataSet.Mode.LINEAR);
            lineDataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            lineDataSet.setFillColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            lineDataSet.setDrawFilled(true);

            LineData lineData = new LineData(lineDataSet);

            chart.getXAxis().setEnabled(false);

            chart.setData(lineData);
            chart.invalidate();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Entry>> loader) {

    }

    public static void startActivity(@NonNull Context context, @NonNull String symbol) {
        Intent detailsIntent = new Intent(context, StockDetailsChartActivity.class);
        detailsIntent.putExtra(StockDetailsChartActivity.EXTRA_STOCK_SYMBOL, symbol);
        context.startActivity(detailsIntent);
    }

}

package com.udacity.stockhawk.ui.history;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import com.github.mikephil.charting.data.Entry;
import com.udacity.stockhawk.data.Contract;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Async task loader responsible for querying a stock history from the database using a data provider
 * <p>
 * Created by carvalhorr on 4/4/17.
 */

public class StockChartDataAsyncTaskLoader extends AsyncTaskLoader<List<Entry>> {

    private String stockSymbol;

    public StockChartDataAsyncTaskLoader(Context context, String stockSymbol) {
        super(context);
        this.stockSymbol = stockSymbol;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Entry> loadInBackground() {

        // query the history from the database
        Cursor cursor = getContext().getContentResolver().query(
                Contract.Quote.makeUriForStock(stockSymbol),
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[Contract.Quote.QUOTE_COLUMNS.size()]),
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() != 1) {
            return null;
        }
        cursor.moveToFirst();

        // get the CSV hostory data
        String stockHistory = cursor.getString(Contract.Quote.POSITION_HISTORY);

        cursor.close();

        // create list of entries from the csv string for the chart
        List<Entry> entries = new ArrayList<Entry>();

        Scanner scanner = new Scanner(stockHistory);
        while (scanner.hasNextLine()) {

            String[] line = scanner.nextLine().split(",");

            long date = TimeUnit.MILLISECONDS.toDays(Long.parseLong(line[0].trim(), 10));
            float closeQuote = Float.parseFloat(line[1].trim());

            Entry entry = new Entry(date, closeQuote);
            entries.add(entry);
        }
        scanner.close();

        Collections.sort(entries, new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                return o1.getX() > o2.getX() ? 1 : -1;
            }
        });
        return entries;
    }
}

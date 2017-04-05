package com.udacity.stockhawk.control;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;
import android.test.mock.MockCursor;

import com.github.mikephil.charting.data.Entry;
import com.udacity.stockhawk.data.Contract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.Provider;
import java.util.List;

import timber.log.Timber;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.*;

/**
 * Created by carvalhorr on 4/5/17.
 */
@RunWith(AndroidJUnit4.class)
public class StockChartDataAsyncTaskLoaderTests {

    private static final String STOCK_HISTORY_STRING = "1491174000000, 144.770004\n" +
            "1490569200000, 143.660004\n" +
            "1489968000000, 140.639999\n" +
            "1489363200000, 139.990005\n" +
            "1453680000000, 97.339996\n" +
            "1453161600000, 101.419998\n" +
            "1452470400000, 97.129997\n" +
            "1451865600000, 96.959999\n" +
            "1451260800000, 105.260002\n" +
            "1429484400000, 130.279999\n" +
            "1428879600000, 124.75\n" +
            "1428274800000, 127.099998\n";

    @Test
    public void stockChartDataAsyncLoaderReturnRightNUmberOfPoints() {

        StockChartDataAsyncTaskLoader loader = new StockChartDataAsyncTaskLoader(new MockStockContext(), "1");

        List<Entry> entries = loader.loadInBackground();

        assertEquals(12, entries.size());
    }


    public class MockStockContentProvider extends MockContentProvider {

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            Timber.d("called query on mock provider");
            Cursor c = mock(SQLiteCursor.class);
            when(c.getString(Contract.Quote.POSITION_HISTORY)).thenReturn(STOCK_HISTORY_STRING);
            when(c.getCount()).thenReturn(1);
            return c;
        }

    }

    public class MockStockContext extends MockContext {

        @Override
        public ContentResolver getContentResolver() {
            MockContentResolver resolver = new MockContentResolver();
            resolver.addProvider(Contract.AUTHORITY, new MockStockContentProvider());
            return resolver;
        }

        @Override
        public Context getApplicationContext() {
            return this;
        }

    }
}



package com.udacity.stockhawk.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.net.Uri;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.StockHawkApp;
import com.udacity.stockhawk.data.Contract;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by carvalhorr on 4/6/17.
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class StockChartDetailsActivityTests {

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


    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(
            MainActivity.class);

    /**
     * Navigation test that makes sure the details activity is displayed.
     * <p>
     * It is relying on the data that already exist.
     * TODO Add dependency injection to mock the data and not rely on existing components.
     *
     * @throws InterruptedException
     */
    @Test
    public void whenUserClickOnStockThenDetailsAcitivityIsDisplayed() throws InterruptedException {

        // click on first stock
        onView(allOf(
                withId(R.id.recycler_view), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // make sure details acitivty is displayed
        onView(withId(R.id.stock_chart)).check(ViewAssertions.matches(isDisplayed()));

    }

    private ContentResolver getMockContentResolver() {
        MockContentResolver resolver = new MockContentResolver();
        resolver.addProvider(Contract.AUTHORITY, new MockStockContentProvider());
        return resolver;

    }

    public class MockStockContentProvider extends MockContentProvider {

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            Cursor c = mock(SQLiteCursor.class);
            when(c.getString(Contract.Quote.POSITION_HISTORY)).thenReturn(STOCK_HISTORY_STRING);
            when(c.getInt(Contract.Quote.POSITION_VALID)).thenReturn(1);
            when(c.getFloat(Contract.Quote.POSITION_PRICE)).thenReturn(1f);
            when(c.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE)).thenReturn(1f);
            when(c.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE)).thenReturn(1f);
            when(c.getString(Contract.Quote.POSITION_SYMBOL)).thenReturn("GOOG");
            when(c.getCount()).thenReturn(1);
            return c;
        }

        @Override
        public int bulkInsert(Uri uri, ContentValues[] values) {
            return values.length;
        }

        @Override
        public Uri insert(Uri uri, ContentValues values) {
            getContext().getContentResolver().notifyChange(Contract.Quote.URI, null);
            return Contract.Quote.makeUriForStock("1");
        }
    }

}

package com.udacity.stockhawk.ui;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.stockhawk.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

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

}

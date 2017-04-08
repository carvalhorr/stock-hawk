package com.udacity.stockhawk.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by carvalhorr on 4/8/17.
 */

public class FormatUtil {

    public static String formatCurrency(float value) {
        final DecimalFormat dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        return dollarFormat.format(value);
    }

    public static String formatPercentage(float value) {
        final DecimalFormat percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
        return percentageFormat.format(value);
    }
}

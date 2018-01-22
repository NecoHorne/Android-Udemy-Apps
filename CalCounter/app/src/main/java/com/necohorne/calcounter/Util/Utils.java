package com.necohorne.calcounter.Util;

import java.text.DecimalFormat;

/**
 * Created by necoh on 2018/01/08.
 */

public class Utils {

    public static String formatNumber(int value) {
        DecimalFormat formatter = new DecimalFormat( "#,###,###" );
        String formatted = formatter.format( value );
        return formatted;
    }
}

package com.necohorne.earthquakewatcher.Util;

import java.util.Random;

/**
 * Created by necoh on 2018/01/18.
 */

public class Constants {
    public static final String URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_week.geojson";
    public static final int LIMIT = 30;

    public static int randomInt(int max, int min) {

        return new Random().nextInt(max - min) + min;
    }
}

package com.example.android.baking.utils;

import android.util.DisplayMetrics;

public final class DeviceUtils {

    public static boolean isDeviceTablet(){
        DisplayMetrics metrics = new DisplayMetrics();

        int widthPixels = metrics.widthPixels;
        float scaleFactor = metrics.density;

        float widthDp = widthPixels / scaleFactor;

        return widthDp >= 600;
    }
}

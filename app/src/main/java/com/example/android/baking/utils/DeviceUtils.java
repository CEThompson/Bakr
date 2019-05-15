package com.example.android.baking.utils;

import android.util.DisplayMetrics;

import timber.log.Timber;

public final class DeviceUtils {

    public static boolean isDeviceTablet(){
        DisplayMetrics metrics = new DisplayMetrics();

        int widthPixels = metrics.widthPixels;
        Timber.d("WidthPixels is " + String.valueOf(widthPixels));
        float scaleFactor = metrics.density;
        Timber.d("Density is " + String.valueOf(scaleFactor));

        float widthDp = widthPixels / scaleFactor;
        Timber.d("Width is " + String.valueOf(widthDp));
        return widthDp >= 600;
    }
}

package com.hellorn.core;

import android.content.Context;

public class RNBundleManager {

    public static String hasCache(Context context, String hybridId) {
        return context.getFilesDir() + "/index/index.android.bundle";
    }



}

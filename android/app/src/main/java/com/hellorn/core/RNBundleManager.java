package com.hellorn.core;

import android.content.Context;

import com.hellorn.util.FileUtil;

public class RNBundleManager {

    public static String hasCache(Context context, String hybridId) {

        String db = context.getFilesDir().getAbsolutePath() + "/rn.db";
        Qp qp = QPEngineManager.queryQp(db, hybridId);
        if (qp != null) {
            return FileUtil.findJSBundleFiles(qp.url, ".bundle");
        }
        return null;

        //return context.getFilesDir() + "/index/index.android.bundle";
    }


}

package com.hellorn;

import android.app.Application;

import com.facebook.soloader.SoLoader;
import com.hellorn.core.QPEngineManager;
import com.hellorn.core.ReactInstancePool;

public class MainApplication extends Application {

    private static MainApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
        mApp = this;
        QPEngineManager.init(this);
        if (!BuildConfig.DEBUG) {
            ReactInstancePool.getInstance(this).preLoad(this);
        }
    }

    public static MainApplication getInstance() {
        return mApp;
    }
}

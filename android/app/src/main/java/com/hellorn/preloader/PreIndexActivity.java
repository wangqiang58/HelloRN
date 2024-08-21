package com.hellorn.preloader;

import android.os.Bundle;

import com.facebook.react.modules.core.PermissionListener;

public class PreIndexActivity extends MrReactActivity {

    public static final ReactInfo reactInfo = new ReactInfo() {
        @Override
        public String getMainComponentName() {
            return "index.android.bundle";
        }

        @Override
        public Bundle getLaunchOptions() {
            return null;
        }
    };

    @Override
    public ReactInfo getReactInfo() {
        return reactInfo;
    }

    @Override
    public void invokeDefaultOnBackPressed() {

    }

    @Override
    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {

    }
}

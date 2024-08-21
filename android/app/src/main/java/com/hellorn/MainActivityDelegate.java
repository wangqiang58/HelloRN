package com.hellorn;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.hermes.reactexecutor.HermesExecutorFactory;
import com.facebook.react.PackageList;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptExecutorFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivityDelegate extends ReactActivityDelegate {

    public Activity activity;
    public String componentName;


    public MainActivityDelegate(Activity activity, String componentName){
        super(activity,componentName);
        this.activity = activity;
    }

    @Override
    protected ReactNativeHost getReactNativeHost() {
        return new ReactNativeHost(activity.getApplication()) {

            @Override
            public boolean getUseDeveloperSupport() {
                return BuildConfig.DEBUG;
            }

            @Nullable
            @Override
            protected String getJSBundleFile() {
                String path = getPlainActivity().getFilesDir() + "/bundle/index/index.android.bundle";
                File file = new File(path);
                if (file.exists()) {
                    Log.d("RN", "index 文件存在");
                } else {
                    Log.d("RN", "index 文件不存在");
                }
                return path;
            }

            @Nullable
            @Override
            protected JavaScriptExecutorFactory getJavaScriptExecutorFactory() {
                return new HermesExecutorFactory();
            }


            @Override
            protected List<ReactPackage> getPackages() {
                List<ReactPackage> packages = new ArrayList<>();
                packages.add(new DeviceInfoPackage());
                packages.addAll(new PackageList(this).getPackages());
                return packages;
            }

            @Override
            protected String getJSMainModuleName() {
                return "index";
            }
        };
    }
}

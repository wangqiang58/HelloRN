package com.hellorn;

import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.hermes.reactexecutor.HermesExecutorFactory;
import com.facebook.react.PackageList;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactHost;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptExecutorFactory;
import com.facebook.react.defaults.DefaultReactHost;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ReactActivity {

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new ReactActivityDelegate(this,getMainComponentName()){
            @Override
            protected ReactNativeHost getReactNativeHost() {
                return new ReactNativeHost(MainActivity.this.getApplication()) {
                    @Override
                    public boolean getUseDeveloperSupport() {
                        return BuildConfig.DEBUG;
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

        };
    }

    @Nullable
    @Override
    protected String getMainComponentName() {
        return "home";
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}

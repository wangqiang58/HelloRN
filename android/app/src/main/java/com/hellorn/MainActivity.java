package com.hellorn;

import androidx.annotation.Nullable;

import com.facebook.react.PackageList;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactHost;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.defaults.DefaultReactHost;
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

            @Override
            public ReactHost getReactHost() {
                return  DefaultReactHost.getDefaultReactHost(MainActivity.this,getReactNativeHost());
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

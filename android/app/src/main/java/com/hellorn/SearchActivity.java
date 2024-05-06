package com.hellorn;

import androidx.annotation.Nullable;

import com.facebook.react.PackageList;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;

import java.util.List;

public class SearchActivity extends ReactActivity {

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        return new ReactActivityDelegate(this,getMainComponentName()){
            @Override
            protected ReactNativeHost getReactNativeHost() {
                return new ReactNativeHost(SearchActivity.this.getApplication()) {
                    @Override
                    public boolean getUseDeveloperSupport() {
                        return BuildConfig.DEBUG;
                    }

                    @Override
                    protected List<ReactPackage> getPackages() {
                        return new PackageList(this).getPackages();
                    }

                    @Override
                    protected String getJSMainModuleName() {
                        return "bu_search";
                    }
                };
            }
        };
    }

    @Nullable
    @Override
    protected String getMainComponentName() {
        return "search";
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

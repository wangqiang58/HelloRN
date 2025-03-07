package com.hellorn;

import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.hermes.reactexecutor.HermesExecutorFactory;
import com.facebook.react.PackageList;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptExecutorFactory;

import java.io.File;
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

                    @Nullable
                    @Override
                    protected String getJSBundleFile() {
                        String path =  SearchActivity.this.getFilesDir() + "/bundle/search/search.android.bundle";
                        File file = new File(path);
                        if (file.exists()){
                            Log.d("RN","search 文件存在");
                        }else{
                            Log.d("RN","search 文件不存在");
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

package com.hellorn.core;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.react.PackageList;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.soloader.SoLoader;
import com.hellorn.MainApplication;

public class RNPageActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {

    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;
    private String appKey;
    private String hybridId;

    public void parseParams() {
        appKey = getIntent().getStringExtra("appKey");
        hybridId = getIntent().getStringExtra("hybridId");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoLoader.init(this, false);
        parseParams();

//        if (BuildConfig.DEBUG) {
//            loadJSBundleFromMetro();
//            return;
//        }
        mReactInstanceManager = ReactInstancePool.getInstance(MainApplication.getInstance()).getReactInstance(MainApplication.getInstance());
        mReactInstanceManager.onHostResume(this, this);
        mReactRootView = new ReactRootView(this);

        mReactInstanceManager.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
            @Override
            public void onReactContextInitialized(ReactContext context) {
                //加载业务包
                ReactContext mContext = mReactInstanceManager.getCurrentReactContext();
                CatalystInstance instance = mContext.getCatalystInstance();
                boolean isFromLocal = loadJSBundleFromFile(instance, context);
                if (isFromLocal) {
                    mReactRootView.startReactApplication(mReactInstanceManager, appKey, null);
                    setContentView(mReactRootView);
                } else {
                    loadJsBundleFromNetwork(instance);
                    mReactRootView.startReactApplication(mReactInstanceManager, appKey, null);
                    setContentView(mReactRootView);
                }
                mReactInstanceManager.removeReactInstanceEventListener(this);
            }
        });
        mReactInstanceManager.createReactContextInBackground();

    }

    private void loadJSBundleFromMetro() {
        mReactRootView = new ReactRootView(this);
        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setCurrentActivity(this)
                .setJSMainModulePath("index")
                .addPackages(new PackageList(getApplication()).getPackages())
                .setUseDeveloperSupport(true)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();

        mReactRootView.startReactApplication(mReactInstanceManager, appKey, null);
        setContentView(mReactRootView);
    }

    private boolean loadJSBundleFromFile(CatalystInstance instance, ReactContext context) {
        // 是否有本地存储
        String bundlePath = QPEngineManager.hasCache(getApplicationContext(), hybridId);
        // 加载对应的 load
        if (!bundlePath.isEmpty()) {
            instance.loadSplitBundleFromFile(bundlePath, bundlePath);
            return true;
        }
        return false;
    }

    private void loadJsBundleFromNetwork(CatalystInstance instance) {
       instance.runJSBundle();
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
            mReactInstanceManager.showDevOptionsDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostDestroy(this);
        }
        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
        }
    }

}

package com.hellorn.core;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.hermes.reactexecutor.HermesExecutorFactory;
import com.facebook.react.PackageList;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hellorn.MainApplication;
import com.hellorn.bridge.StudyPackage;

import java.util.List;

public class RNPageActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {

    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;
    private String appKey;
    private String hybridId;

    public static int LAUNCH_MODE_METRO = 0;
    public static int LAUNCH_MODE_OFFLINE = 1;

    private int launchMode;

    public void parseParams() {
        appKey = getIntent().getStringExtra("appKey");
        hybridId = getIntent().getStringExtra("hybridId");
        launchMode = getIntent().getIntExtra("launchMode", LAUNCH_MODE_METRO);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseParams();
        if (launchMode == LAUNCH_MODE_METRO) {
            loadJSBundleFromMetro();
            return;
        }
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
                if (!isFromLocal) {
                    loadJsBundleFromNetwork(instance);
                }
                mReactRootView.startReactApplication(mReactInstanceManager, appKey, null);
                setContentView(mReactRootView);
                emitLifeCycleEvent("onCreate");
                mReactInstanceManager.removeReactInstanceEventListener(this);
            }
        });
        mReactInstanceManager.createReactContextInBackground();
    }

    private void emitLifeCycleEvent(String type) {
        mReactInstanceManager.getCurrentReactContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("LifeCycleEvent", createWritableMap(type));
    }

    public WritableMap createWritableMap(String type) {
        WritableMap map = new WritableNativeMap();
        map.putString("type", type);
        map.putString("hybridId", hybridId);

        return map;
    }

    private void loadJSBundleFromMetro() {
        mReactRootView = new ReactRootView(this);
        List<ReactPackage> packages = new PackageList(this.getApplication()).getPackages();
        packages.add(new StudyPackage());

        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setCurrentActivity(this)
                .setJSMainModulePath("index")
                .addPackages(packages)
                .setUseDeveloperSupport(true)
                .setJavaScriptExecutorFactory(new HermesExecutorFactory())
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        mReactInstanceManager.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
            @Override
            public void onReactContextInitialized(ReactContext context) {
                emitLifeCycleEvent("onCreate");
                mReactInstanceManager.removeReactInstanceEventListener(this);
            }
        });


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
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause(this);
            emitLifeCycleEvent("onPause");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(this, this);
            if (mReactInstanceManager.getCurrentReactContext() != null)
                emitLifeCycleEvent("onResume");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReactInstanceManager != null) {
            emitLifeCycleEvent("onDestroy");
            mReactInstanceManager.onHostDestroy(this);
            mReactInstanceManager.destroy();
        }
        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
        }
        mReactRootView = null;
        mReactInstanceManager = null;
    }

}

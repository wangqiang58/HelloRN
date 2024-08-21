package com.hellorn;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.hermes.reactexecutor.HermesExecutorFactory;
import com.facebook.react.PackageList;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.CatalystInstanceImpl;
import com.facebook.react.bridge.JavaScriptExecutorFactory;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.soloader.SoLoader;

import java.io.File;
import java.util.List;

public class All2Activity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {

    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;

    public String getJSBundleAssetName() {
        return "index.android.bundle";
    }


    public String getJsModulePathPath() {
        return "index";
    }


    public String getResName() {
        return "home";
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoLoader.init(this, false);
        if (BuildConfig.DEBUG) {
            mReactRootView = ReactNativePreLoader.getReactRootView(this, "home");
            if (mReactRootView != null) {
                mReactRootView.getReactInstanceManager().recreateReactContextInBackground();
                setContentView(mReactRootView);
                Log.d("RN", "预加载成功...");
                return;
            }


            Log.d("RN", "预加载失败...");

            mReactRootView = new ReactRootView(this);
            mReactInstanceManager = ReactInstanceManager.builder()
                    .setApplication(getApplication())
                    .setCurrentActivity(this)
//                    .setBundleAssetName(getJSBundleAssetName())
                    .setJSMainModulePath(getJsModulePathPath())
                    .addPackages(new PackageList(getApplication()).getPackages())
                    .setUseDeveloperSupport(true)
                    .setInitialLifecycleState(LifecycleState.RESUMED)
                    .build();
            mReactRootView.startReactApplication(mReactInstanceManager, getResName(), null);
            setContentView(mReactRootView);
            return;
        }

        mReactInstanceManager = MainApplication2.getInstance().getRcInstanceManager();
        mReactInstanceManager.onHostResume(this, this);
        mReactRootView = new ReactRootView(this);

        mReactInstanceManager.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
            @Override
            public void onReactContextInitialized(ReactContext context) {
                MainApplication2.getInstance().setIsLoad(true);

                //加载业务包
                ReactContext mContext = mReactInstanceManager.getCurrentReactContext();
                CatalystInstance instance = mContext.getCatalystInstance();

                loadForSystemOrAssets(instance, context);

                mReactRootView.startReactApplication(mReactInstanceManager, getResName(), null);
                setContentView(mReactRootView);

                mReactInstanceManager.removeReactInstanceEventListener(this);
            }
        });

        if (MainApplication2.getInstance().getIsLoad()) {
            ReactContext mContext = mReactInstanceManager.getCurrentReactContext();
            CatalystInstance instance = mContext.getCatalystInstance();

            loadForSystemOrAssets(instance, mContext);

            mReactRootView.startReactApplication(mReactInstanceManager, getResName(), null);
            setContentView(mReactRootView);

        }

        mReactInstanceManager.createReactContextInBackground();

    }

    private void loadForSystemOrAssets(CatalystInstance instance, ReactContext context) {
        String filePath$Name = "";
        // 是否有本地存储？
        Boolean hasCache = true;//RNToolsManager.hasCache(getApplicationContext());
        String basePhat = ""; //RNToolsManager.currentVersionByBundleName(getApplicationContext()).get("moduleType");

        // 加载对应的 load
        if (hasCache) {
            filePath$Name = getFilesDir() + "/bundle/index/index.android.bundle"; //getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/Bundle/"+ basePhat + "/" + getJSBundleAssetName();
        } else {
            filePath$Name = "assets://" + getJSBundleAssetName();
        }


        if (!hasCache) {
            // loadScriptFromAssets FromAssets 不再适用
            ((CatalystInstanceImpl) instance).loadScriptFromAssets(context.getAssets(), filePath$Name, false);
            return;
        }

        ((CatalystInstanceImpl) instance).loadSplitBundleFromFile(filePath$Name, filePath$Name);

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

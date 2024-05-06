package com.hellorn;

import android.app.Application;

import com.facebook.hermes.reactexecutor.HermesExecutorFactory;
import com.facebook.react.PackageList;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.common.LifecycleState;
import com.facebook.soloader.SoLoader;

import java.util.List;

public class MainApplication2 extends Application {
    public List<ReactPackage> packages;
    private  ReactInstanceManager cacheReactInstanceManager;
    private Boolean isload = false;

    private static MainApplication2 mApp;
    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
        mApp = this;

        packages = new PackageList(this).getPackages();
        packages.add(new DeviceInfoPackage());

        cacheReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(this)
                .addPackages(packages)
                .setJavaScriptExecutorFactory(new HermesExecutorFactory())
                .setJSBundleFile(getFilesDir()+"/bundle/common.android.bundle")
                .setInitialLifecycleState(LifecycleState.BEFORE_CREATE).build();

    }

    public static MainApplication2 getInstance(){
        return mApp;
    }

    // 获取 已经缓存过的 rcInstanceManager
    public ReactInstanceManager getRcInstanceManager () {
        return this.cacheReactInstanceManager;
    }


    public void setIsLoad(Boolean isload) {
        this.isload = isload;
    }

    public boolean getIsLoad(){
        return this.isload;
    }
}

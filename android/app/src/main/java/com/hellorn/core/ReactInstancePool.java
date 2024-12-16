package com.hellorn.core;

import android.app.Application;
import android.os.Handler;

import com.facebook.hermes.reactexecutor.HermesExecutorFactory;
import com.facebook.react.PackageList;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.common.LifecycleState;
import com.hellorn.bridge.DeviceInfoPackage;

import java.util.LinkedList;
import java.util.List;

public class ReactInstancePool {

    private static ReactInstancePool instance;

    private LinkedList<ReactInstanceManager> cacheReactInstanceManager = new LinkedList<>();
    public List<ReactPackage> packages;

    private ReactInstancePool(Application application) {
        packages = new PackageList(application).getPackages();
        packages.add(new DeviceInfoPackage());
    }

    public static ReactInstancePool getInstance(Application application) {
        if (instance == null) {
            instance = new ReactInstancePool(application);
        }
        return instance;
    }

    public void preLoad(Application application) {
        createReactInstance(application);
    }

    public ReactInstanceManager getReactInstance(Application application) {
        if (cacheReactInstanceManager.isEmpty()) {
            createReactInstance(application);
            return cacheReactInstanceManager.getFirst();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    createReactInstance(application);
                }
            }, 2000);
            return cacheReactInstanceManager.getFirst();
        }
    }

    private void createReactInstance(Application application) {
        ReactInstanceManager instance = ReactInstanceManager.builder()
                .setApplication(application)
                .addPackages(packages)
                .setJavaScriptExecutorFactory(new HermesExecutorFactory())
                .setBundleAssetName("common.android.bundle")
                .setInitialLifecycleState(LifecycleState.BEFORE_CREATE).build();
        cacheReactInstanceManager.addLast(instance);
    }


}

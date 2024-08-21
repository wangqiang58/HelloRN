package com.hellorn;

import android.app.Activity;
import android.app.Application;
import android.content.MutableContextWrapper;
import android.util.ArrayMap;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.react.PackageList;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;

import java.util.Map;

public class ReactNativePreLoader {

    private static final Map<String, ReactRootView> cache = new ArrayMap<>();

    public static void preLoad(Activity activity, String componentName) {
        if (cache.get(componentName) != null) {
            return;
        }
        //1.创建ReactRootView
        ReactRootView rootView = new ReactRootView(new MutableContextWrapper(activity.getApplication()));
        MainActivityDelegate delegate = new MainActivityDelegate(activity,componentName);
        ReactInstanceManager reactInstanceManager = delegate.getReactNativeHost().getReactInstanceManager();
        rootView.startReactApplication(
                reactInstanceManager,
                componentName,
                null);
        //2.添加到缓
        cache.put(componentName, rootView);
    }


    public static ReactRootView getReactRootView(Activity activity,String componentName) {
        ReactRootView reactRootView =  cache.get(componentName);
        if(reactRootView.getContext() instanceof MutableContextWrapper){
            ((MutableContextWrapper) reactRootView.getContext()).setBaseContext(
                    activity
            );
        }
        return reactRootView;
    }


    public static void deatchView(Activity activity,String component) {
        try {
            ReactRootView rootView = getReactRootView(activity,component);
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } catch (Throwable e) {
            Log.e("ReactNativePreLoader", e.getMessage());
        }
    }

}

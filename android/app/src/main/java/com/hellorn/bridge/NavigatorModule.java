package com.hellorn.bridge;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class NavigatorModule extends ReactContextBaseJavaModule {
    @NonNull
    @Override
    public String getName() {
        return "Navigator";
    }

    private ReactApplicationContext mContext;

    public NavigatorModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @ReactMethod
    public void goBack() {
        getCurrentActivity().finish();
        Toast.makeText(mContext,"点击返回123",Toast.LENGTH_SHORT).show();
    }
}

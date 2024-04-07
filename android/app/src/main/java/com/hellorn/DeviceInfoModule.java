package com.hellorn;

import static cn.xdf.ucan.troy.lib.utils.PackageUtils.getPackageName;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import java.util.HashMap;
import java.util.Map;

public class DeviceInfoModule extends ReactContextBaseJavaModule {

    private ReactApplicationContext mContext;

    public DeviceInfoModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "DeviceInfoModule";
    }

    @ReactMethod
    public void alert(String title, String message) {
        AlertDialog.Builder tDialog = new AlertDialog.Builder(getCurrentActivity());
        tDialog.setTitle(title);
        tDialog.setMessage(message);
        tDialog.setPositiveButton("确定", null);
        tDialog.setNegativeButton("取消", null);
        tDialog.show();
    }

    @ReactMethod
    public void startWebActivity(String url) {
        Intent intent = new Intent(getCurrentActivity(),WebActivity.class);
        intent.putExtra("url",url);
        getCurrentActivity().startActivity(intent);
    }



    @ReactMethod
    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }


    @ReactMethod
    public void getDeviceInfos(Callback successCallback, Callback errorCallback) throws PackageManager.NameNotFoundException {
        WritableMap maps = Arguments.createMap();
        Context context = getReactApplicationContext();
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        String versionName = packageInfo.versionName;
        int versionCode = packageInfo.versionCode;
        maps.putString("appVersion", "" + versionCode);
        maps.putString("appName", versionName);
        successCallback.invoke(maps);
    }


}

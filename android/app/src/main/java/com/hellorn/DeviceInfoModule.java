package com.hellorn;

import android.app.AlertDialog;
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
    public DeviceInfoModule(ReactApplicationContext reactContext){
        super(reactContext);
        mContext = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "DeviceInfoModule";
    }

    @ReactMethod
    public void alert(String title,String message){
        AlertDialog.Builder tDialog = new AlertDialog.Builder(getCurrentActivity());
        tDialog.setTitle(title);
        tDialog.setMessage(message);
        tDialog.setPositiveButton("确定", null);
        tDialog.setNegativeButton("取消",null);
        tDialog.show();
    }

    @ReactMethod
    public void showToast(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }


    @ReactMethod
    public void getDeviceInfos(Callback successCallback, Callback errorCallback){
        WritableMap maps = Arguments.createMap();
        maps.putString("appVersion","1.1.1");
        maps.putString("appName","HelloRN");
        successCallback.invoke(maps);
    }


}

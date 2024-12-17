package com.hellorn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hellorn.core.DownloadCallback;
import com.hellorn.core.DownloadManager;
import com.hellorn.core.Qp;
import com.hellorn.core.RNPageActivity;
import com.hellorn.util.ZipUtil;

public class WelcomeActivity extends ComponentActivity implements DefaultHardwareBackBtnHandler {
    private ReactInstanceManager mReactInstanceManager;
    // 发送消息给 React Native
    private LifecycleState mLifecycleState
            = LifecycleState.BEFORE_RESUME;


    @Override
    protected void onPause() {
        super.onPause();
        mLifecycleState = LifecycleState.BEFORE_RESUME;

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLifecycleState = LifecycleState.RESUMED;

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(this, this);
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.destroy();
        }
    }


    private void sendMessageToReactNative(String message) {

        // 初始化 ReactInstanceManager
        ReactContext reactContext = mReactInstanceManager.getCurrentReactContext();
        if (reactContext != null) {
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("eventName", message);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, AllActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, RNPageActivity.class);
                intent.putExtra("appKey", "home");
                intent.putExtra("hybridId", "001");
                startActivity(intent);
            }
        });

        findViewById(R.id.btn6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ReactNativeFlipper.initializeFlipper(MainApplication.instance, MainApplication.instance.getReactNativeHost().getReactInstanceManager());
                DownloadManager.download("http://192.168.68.128:8000/index.zip", getFilesDir() + "/" + "index.zip", new DownloadCallback() {
                    @Override
                    public void onResult(boolean result) {
                        Log.d("RN", "下载" + result);
                    }
                });
            }
        });

        findViewById(R.id.btn7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ReactNativeFlipper.initializeFlipper(MainApplication.instance, MainApplication.instance.getReactNativeHost().getReactInstanceManager());

                ZipUtil.unzipFolder(getFilesDir() + "/" + "index.zip", getFilesDir().getAbsolutePath());
                boolean result = DownloadManager.initDB(getFilesDir().getAbsolutePath() + "/rn.db");
                Log.d("RN", "初始化DB" + result);
            }
        });

        findViewById(R.id.btn8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = DownloadManager.insertRecord(getFilesDir().getAbsolutePath() + "/rn.db", "001", 1, getFilesDir() + "/" + "index");
                Log.d("RN", "插入数据结果:" + result);
            }
        });

        findViewById(R.id.btn9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Qp qp = DownloadManager.queryQp(getFilesDir().getAbsolutePath() + "/rn.db", "001");
                Log.d("RN", "查询数据结果:" + qp);
            }
        });
    }
}

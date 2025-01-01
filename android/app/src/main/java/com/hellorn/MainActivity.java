package com.hellorn;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hellorn.core.DownloadCallback;
import com.hellorn.core.QPEngineManager;
import com.hellorn.core.Qp;
import com.hellorn.core.RNPageActivity;
import com.hellorn.util.ZipUtil;

import android.Manifest;

public class MainActivity extends ComponentActivity implements DefaultHardwareBackBtnHandler {
    private ReactInstanceManager mReactInstanceManager;
    // 发送消息给 React Native
    private LifecycleState mLifecycleState
            = LifecycleState.BEFORE_RESUME;
    private static final int PERMISSION_REQUEST_CODE = 1;

    Qp qp = new Qp("002", 1, "http://192.168.10.5:8000/index.zip", "1efe393ba86584a188b3dc8a59675bac");


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

    private void requestStoragePermission() {
        // 检查权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        } else {
            // 权限已授予
            Toast.makeText(this, "权限已授予", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 权限授予成功
                Toast.makeText(this, "权限授予成功", Toast.LENGTH_SHORT).show();
            } else {
                // 权限授予失败
                Toast.makeText(this, "权限授予失败", Toast.LENGTH_SHORT).show();
            }
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
        requestStoragePermission();
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RNPageActivity.class);
                intent.putExtra("appKey", "home");
                intent.putExtra("hybridId", "002");
                startActivity(intent);
            }
        });

        findViewById(R.id.btn6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QPEngineManager.download(qp, new DownloadCallback() {
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
                ZipUtil.unzipFolder(getFilesDir() + "/" + "index.zip", getFilesDir().getAbsolutePath());
                // boolean result = QPEngineManager.initDB(getFilesDir().getAbsolutePath() + "/rn.db");
                //Log.d("RN", "初始化DB" + result);
            }
        });

        findViewById(R.id.btn8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = QPEngineManager.insertRecord(getFilesDir().getAbsolutePath() + "/rn.db", "001", 1, getFilesDir() + "/" + "index");
                Log.d("RN", "插入数据结果:" + result);
            }
        });

        findViewById(R.id.btn9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Qp qp = QPEngineManager.queryQp(getFilesDir().getAbsolutePath() + "/rn.db", "001");
                Log.d("RN", "查询数据结果:" + qp);
            }
        });
    }
}

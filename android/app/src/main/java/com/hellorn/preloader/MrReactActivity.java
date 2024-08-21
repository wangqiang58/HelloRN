package com.hellorn.preloader;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.facebook.common.logging.FLog;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.PermissionAwareActivity;

public abstract class MrReactActivity extends Activity
        implements DefaultHardwareBackBtnHandler, PermissionAwareActivity {

    ReactRootView mReactRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (false && Build.VERSION.SDK_INT >= 23) {
            // Get permission to show redbox in dev builds.
            if (!Settings.canDrawOverlays(this)) {
                Intent serviceIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(serviceIntent);
//                FLog.w(ReactConstants.TAG, REDBOX_PERMISSION_MESSAGE);
//                Toast.makeText(this, REDBOX_PERMISSION_MESSAGE, Toast.LENGTH_LONG).show();
            }
        }
        mReactRootView = ReactPreLoader.getRootView(getReactInfo());
        if (mReactRootView != null) {
            Log.i("MrReactActivity", "use pre-load view");
        } else {
            Log.i("MrReactActivity", "createRootView");
//            mReactRootView = createRootView();
//            if (mReactRootView != null) {
//                mReactRootView.startReactApplication(
//                        getReactNativeHost().getReactInstanceManager(),
//                        getMainComponentName(),
//                        getLaunchOptions());
//            }
        }
        setContentView(mReactRootView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
            mReactRootView = null;
            ReactPreLoader.onDestroy(getReactInfo());
        }
    }

    public abstract ReactInfo getReactInfo();
}
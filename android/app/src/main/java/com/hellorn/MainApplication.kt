package com.hellorn

import android.app.Application
import android.util.Log
import com.facebook.hermes.reactexecutor.HermesExecutorFactory
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JavaScriptExecutorFactory
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.flipper.ReactNativeFlipper
import com.facebook.soloader.SoLoader
import java.io.File

class MainApplication : Application(), ReactApplication {


    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)
        if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
            // If you opted-in for the New Architecture, we load the native entry point for this app.
            load()
        }
        ReactNativeFlipper.initializeFlipper(this, reactNativeHost.reactInstanceManager)
    }

    override var reactNativeHost: ReactNativeHost =
        object : ReactNativeHost(this) {

            override fun getPackages(): List<ReactPackage> =
                PackageList(this).packages.apply {
                    add(DeviceInfoPackage())
                }

            override fun getJSMainModuleName(): String {
                return "common"
            }

            override fun getJavaScriptExecutorFactory(): JavaScriptExecutorFactory? {
                return HermesExecutorFactory()
            }

            override fun getJSBundleFile(): String? {

                val path = "${this@MainApplication.filesDir}/bundle/common.android.bundle"
                if (File(path).exists()) {
                    Log.d("RN", "common文件存在")
                } else {
                    Log.d("RN", "common文件不存在")
                }
                return path
            }

            override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG
        }

    override val reactHost: ReactHost
        get() = getDefaultReactHost(this.applicationContext, reactNativeHost)

}

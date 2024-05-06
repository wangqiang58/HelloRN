package com.hellorn

import android.app.Application
import android.util.Log
import com.facebook.hermes.reactexecutor.HermesExecutorFactory
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JavaScriptExecutorFactory
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.react.flipper.ReactNativeFlipper
import com.facebook.soloader.SoLoader
import java.io.File

class MainApplication : Application(), ReactApplication {

    companion object {
        lateinit var instance: MainApplication
    }

    fun getRcInstanceManager():ReactInstanceManager{
        return this.reactNativeHost.reactInstanceManager
    }

    override val reactNativeHost: ReactNativeHost =

        object : ReactNativeHost(this) {

            override fun getPackages(): List<ReactPackage> =
                PackageList(this).packages.apply {
                    // Packages that cannot be autolinked yet can be added manually here, for example:
                    add(DeviceInfoPackage())
                }

            override fun getJSMainModuleName(): String {
                return "index"
            }

            override fun getJSBundleFile(): String? {
//              val bundlePath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "bundles/index.android.bundle"
//              val bundlePath = "${this@MainApplication.filesDir}/common.android.bundle"
                val bundlePath = "${this@MainApplication.filesDir}/index/index.android.bundle"
                val file = File(bundlePath)
                Log.d("RN", bundlePath)
                if (file.exists()) {
                    Log.d("RN", "存在")
                    return bundlePath
                } else {
                    Log.d("RN", "不存在")
                    return super.getJSBundleFile()
                }
//              return  super.getJSBundleFile()
            }

//          override fun getBundleAssetName(): String? {
//              return "index.android.bundle"
//          }

            override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG
        }

    override val reactHost: ReactHost
        get() = getDefaultReactHost(this.applicationContext, reactNativeHost)

    override fun onCreate() {
        super.onCreate()
        instance = this
        SoLoader.init(this, false)
        if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
            // If you opted-in for the New Architecture, we load the native entry point for this app.
            load()
        }
        ReactNativeFlipper.initializeFlipper(this, reactNativeHost.reactInstanceManager)
    }
}

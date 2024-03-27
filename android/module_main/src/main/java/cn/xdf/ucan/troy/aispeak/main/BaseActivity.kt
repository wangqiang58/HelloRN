package cn.xdf.ucan.troy.aispeak.main

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import cn.xdf.ucan.troy.aispeak.ModuleMainApplication.mPresenter
import cn.xdf.ucan.troy.lib.utils.PermissionUtils
import cn.xdf.ucan.troy.lib.xdf.permission.PermissionRequestHelper

abstract class BaseActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun checkPermission(permission: String): Boolean {
        var hasPermission = !PermissionUtils.checkPermission(this, permission)
        if (hasPermission) {
            //没有录音权限，取申请
            requestRecordAudioPermission(permission)
        }
        return hasPermission
    }

    private fun requestRecordAudioPermission(permission: String) {
        try {
            //app退到后台申请权限的时候会报错
            //java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState

            //Context context, int dialogType, boolean enableSkip, String permissionRejectTitle, String permissionRejectContent, String leftBtnText, String rightBtnText, PermissionRequestListener listener
            PermissionRequestHelper.showPermissionDialog(
                this,
                PermissionRequestHelper.PERMISSION_DIALOG_EVA,
                false,
                "标题",
                "",
                "取消",
                "设置",
                null
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    abstract fun <T> refreshListview(data: T)
    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.detachActivityContext()
    }
    fun showToast(){
        runOnUiThread{
            Toast.makeText(this,"请求错误，稍后请重试",Toast.LENGTH_LONG).show()
        }
    }
    override fun onResume() {
        super.onResume()
        mPresenter.attachActivityContext(this)
        checkPermission(Manifest.permission.RECORD_AUDIO)
    }
}
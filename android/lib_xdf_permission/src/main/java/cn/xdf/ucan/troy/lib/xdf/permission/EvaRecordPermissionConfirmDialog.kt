package cn.xdf.ucan.troy.lib.xdf.permission

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window

/**
 * Created by kdd on 2019-12-10 11:02
 *
 *
 * Desc：提示二次确认框
 */
class EvaRecordPermissionConfirmDialog(context: Context, val enableSkip: Boolean) :
    BaseDialog(context, Gravity.CENTER, false) {

    init {
        initView()
    }

    private fun initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.widget_dialog_eva_record_permission_confirm)

//        ll_skip.visibility = if (enableSkip) View.VISIBLE else View.GONE
//        tv_left.visibility = if (enableSkip) View.VISIBLE else View.GONE
//
//        tv_left.setOnClickListener {
//            mListener?.onLeftBtnClicked()
//        }
//
//        tv_right.setOnClickListener {
//            mListener?.onRightBtnClicked()
//        }
//
//        tv_skip.setOnClickListener {
//            mListener?.onSkipClicked()
//        }
    }

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.mListener = onItemClickListener
    }

    fun setTitle(title: String) {
        //tv_title.text = title
    }

    fun setLeftBtnText(text: String) {
       // tv_left.text = text
    }

    fun setRightBtnText(text: String) {
       // tv_right.text = text
    }

    interface OnItemClickListener {
        fun onLeftBtnClicked()
        fun onRightBtnClicked()
        fun onSkipClicked()
    }
}
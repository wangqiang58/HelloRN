package cn.xdf.ucan.troy.aispeak

import cn.xdf.ucan.troy.aispeak.bean.TopicInfoBean
import cn.xdf.ucan.troy.aispeak.mvp.AIPresenter
import cn.xdf.ucan.troy.lib.network.client.OkHttpClientManager

object ModuleMainApplication {
    public lateinit var mPresenter: AIPresenter
    public var mDebugLog:Boolean = false
    public lateinit var mList: ArrayList<TopicInfoBean>

    fun initData(debugLog:Boolean){
        OkHttpClientManager.setsIsLog(debugLog)
        mPresenter = AIPresenter
//        mList = mPresenter.getTopicListAndRefreshView()
    }
}
package cn.xdf.ucan.troy.aispeak.mvvm.vm

import androidx.lifecycle.ViewModel
import cn.xdf.ucan.troy.aispeak.mvvm.repository.AIApi
import cn.xdf.ucan.troy.aispeak.mvvm.repository.AiRepository

open class BaseViewModel:ViewModel() {
    public val mAiRepository = AiRepository(AIApi::class.java)

}
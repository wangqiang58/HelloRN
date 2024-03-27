package cn.xdf.ucan.troy.aispeak.mvvm.vm

import androidx.lifecycle.viewModelScope
import cn.xdf.ucan.troy.aispeak.bean.AIBaseResponseBean
import cn.xdf.ucan.troy.aispeak.bean.TopicInfoBean
import cn.xdf.ucan.troy.aispeak.bean.TopicRequestBean
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class TopicViewModel:BaseViewModel() {
    val mShareFLow = MutableSharedFlow<AIBaseResponseBean<ArrayList<TopicInfoBean>>>()
    fun requestTopicList(requestBean: TopicRequestBean){
        viewModelScope.launch(){
            var data = mAiRepository.fetcher.getTopicList(requestBean)
            mShareFLow.emit(data)
        }
    }
}
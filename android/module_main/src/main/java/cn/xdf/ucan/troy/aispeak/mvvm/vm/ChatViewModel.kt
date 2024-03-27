package cn.xdf.ucan.troy.aispeak.mvvm.vm

import android.util.Base64
import android.util.Log
import androidx.lifecycle.viewModelScope
import cn.xdf.ucan.troy.aispeak.bean.AIBaseResponseBean
import cn.xdf.ucan.troy.aispeak.bean.NextCommandData
import cn.xdf.ucan.troy.aispeak.bean.NextCommandRequestBean
import cn.xdf.ucan.troy.aispeak.bean.SSEContentBean
import cn.xdf.ucan.troy.aispeak.main.ChatActivity
import cn.xdf.ucan.troy.aispeak.mvp.AIPresenter
import cn.xdf.ucan.troy.lib.network.client.OkHttpClientManager
import cn.xdf.ucan.troy.lib.utils.DebugUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources

class ChatViewModel : BaseViewModel() {
    companion object {
        private const val SSEUrl = "totoro/api/sse/get_sse_content"
    }

    val mShareFLow = MutableSharedFlow<AIBaseResponseBean<NextCommandData>>()
    val mSSEContentFlow = MutableSharedFlow<SSEContentBean>()
    val mSSEUIContentFlow = MutableSharedFlow<SSEContentBean>()

    private var mSSEBuilder = StringBuilder()
    private var mEventSource: EventSource? = null

    fun requestNextCommand(nextCommandRequestBean: NextCommandRequestBean) {
        viewModelScope.launch {
            var data = mAiRepository.fetcher.getNextCommand(nextCommandRequestBean)
            mShareFLow.emit(data)
        }
    }

    /**
     * 取消队列的问题 cancel
     * 队列一次消费问题
     */
    fun getSSEContent(requestId: String) {
        var url = DebugUtils.BaseUrl + SSEUrl + "?requestId=${requestId}"
        var request = Request.Builder().url(url).addHeader("Accept", "text/event-stream").build()
        mEventSource = EventSources.createFactory(OkHttpClientManager.getInstance())
            .newEventSource(request, object :
                EventSourceListener() {
                override fun onOpen(eventSource: EventSource, response: okhttp3.Response) {
                    super.onOpen(eventSource, response)
//                    if (AIPresenter.mContext == null || AIPresenter.mContext!!.get() !is ChatActivity) return
//                    AIPresenter.mCloseSSE = false
//                    mSSEBuilder.delete(0,mSSEBuilder.length-1)
                    Log.i("niufeiting", " onOpen ${mSSEBuilder.toString()}")
                }

                override fun onEvent(
                    eventSource: EventSource,
                    id: String?,
                    type: String?,
                    data: String
                ) {
                    if (data != null) {
                        var content = String(Base64.decode(data, Base64.DEFAULT))
                        viewModelScope.launch {
                            mSSEBuilder.append("$content")
                            if (content == "," || content == ".") {
                                Log.i(
                                    "niufeiting",
                                    " onEvent emit content: ${mSSEBuilder.toString()}"
                                )
                                var bean = SSEContentBean(mSSEBuilder.toString(), 0)
                                mSSEBuilder.delete(0, mSSEBuilder.length - 1)
                                mSSEContentFlow.emit(bean)
                            }
                        }

                        viewModelScope.launch {
                            mSSEUIContentFlow.emit(SSEContentBean(content, 0))
                        }
                    }
                }

                override fun onClosed(eventSource: EventSource) {
                    viewModelScope.launch {
                        Log.i("niufeiting", " onClosed emit ${mSSEBuilder.toString()}")
                        mSSEContentFlow.emit(SSEContentBean(mSSEBuilder.toString(), 1))
                        mSSEBuilder.delete(0, mSSEBuilder.length - 1)
                    }
                }

                override fun onFailure(
                    eventSource: EventSource,
                    t: Throwable?,
                    response: okhttp3.Response?
                ) {
                }
            })
    }

    fun closeSSE() {
        if (mEventSource != null) {
            mEventSource!!.cancel()
            mEventSource = null
        }
    }
}
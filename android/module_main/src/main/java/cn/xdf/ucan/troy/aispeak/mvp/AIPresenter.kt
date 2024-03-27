package cn.xdf.ucan.troy.aispeak.mvp

import android.app.Activity
import android.util.Base64
import android.util.Log
import cn.xdf.ucan.troy.aispeak.bean.StudentInputBean
import cn.xdf.ucan.troy.aispeak.bean.TopicInfoBean
import cn.xdf.ucan.troy.aispeak.main.BaseActivity
import cn.xdf.ucan.troy.aispeak.main.ChatActivity
import cn.xdf.ucan.troy.aispeak.main.TopicActivity
import cn.xdf.ucan.troy.aispeak.net.AISpeakFetcher
import cn.xdf.ucan.troy.lib.network.client.OkHttpClientManager
import cn.xdf.ucan.troy.lib.utils.DebugUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.util.concurrent.LinkedBlockingQueue

object AIPresenter {

    private val mAISPeakFetcher: AISpeakFetcher = AISpeakFetcher()
    var mContext: WeakReference<Activity>? = null
    private var mEventSource: EventSource? = null
    private var TAG: String = "AIPresenter"
    @Volatile
    var mCloseSSE = true
    private var mSSEBuilder = StringBuilder()
    public var mQueueList = LinkedBlockingQueue<String>()
    public fun attachActivityContext(activity: BaseActivity) {
        mContext = WeakReference(activity)
    }

    public fun detachActivityContext() {
        mContext = null
        closeSSeConnect()
    }

    fun getTopicListAndRefreshView(): ArrayList<TopicInfoBean> {
        var list: ArrayList<TopicInfoBean> = ArrayList()
        mAISPeakFetcher.getTopicList("", 0).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {

                if (mContext == null || mContext!!.get() !is TopicActivity) return@subscribe
                if (it.data != null) {
                    (mContext!!.get() as TopicActivity).refreshListview(it.data!!)
                }
                list = it.data!!
            }
        return list!!
    }

    fun getNextCommandAndRefreshView(
        userId: String,
        sessionId: String,
        version: Long,
        topicId: Long,
        studentInput: StudentInputBean?
    ) {
//        Log.i(
//            "ChatActivity",
//            "getNextCommandAndRefreshView params  userId : ${userId}, sessionId: ${sessionId}, version: ${version}, topicId : ${topicId},  studentInput : ${studentInput.toString()}"
//        )
        mAISPeakFetcher.getNextCommand(userId, sessionId, version, topicId, studentInput)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (mContext == null || mContext!!.get() !is ChatActivity) return@subscribe
                if (it.status == 1 && it.data != null) {
                    (mContext!!.get() as ChatActivity).refreshListview(it.data!!)
                }
            }
    }


    fun getSSEContent(requestId: String) {
        var url = DebugUtils.BaseUrl + mAISPeakFetcher.SSEUrl + "?requestId=${requestId}"
        var request = Request.Builder().url(url).addHeader("Accept", "text/event-stream").build()
        mEventSource = EventSources.createFactory(OkHttpClientManager.getInstance())
            .newEventSource(request, object :
                EventSourceListener() {
                override fun onOpen(eventSource: EventSource, response: okhttp3.Response) {
                    super.onOpen(eventSource, response)
                    if (mContext == null || mContext!!.get() !is ChatActivity) return
                    mCloseSSE = false
                }

                override fun onEvent(
                    eventSource: EventSource,
                    id: String?,
                    type: String?,
                    data: String
                ) {
                    if (mContext == null || mContext!!.get() !is ChatActivity) return
                    if (data != null) {
                        var content = String(Base64.decode(data, Base64.DEFAULT))
                        mSSEBuilder.append("$content")
                        if (content == "," || content == ".") {
                            addQueue(mSSEBuilder.toString())
                            mSSEBuilder.delete(0, mSSEBuilder.length - 1)
                        }
                        (mContext!!.get() as ChatActivity).refreshCurrentCommandContent(content)
                    }
                }

                override fun onClosed(eventSource: EventSource) {
                    addQueue(mSSEBuilder.toString())
                    mSSEBuilder.delete(0, mSSEBuilder.length - 1)
                    mCloseSSE = true
                }

                override fun onFailure(
                    eventSource: EventSource,
                    t: Throwable?,
                    response: okhttp3.Response?
                ) {
                    Log.i(TAG, "sse error = ${t.toString()}")
                }
            })
    }

    fun addQueue(text: String) {
        if ((mContext!!.get() as ChatActivity).playCurrentCommand(text)) {//正在播放时，将流氏数据加入队列。等待播放
            mQueueList.offer(text)
        }
    }

    fun removeQueue(): String {
        if (mQueueList.isNullOrEmpty()) return " "
        return mQueueList.poll()
    }

    private fun closeSSeConnect() {
        if (mEventSource != null) {
            mEventSource!!.cancel()
            mQueueList.clear()
            mSSEBuilder.clear()
            mEventSource = null
        }
    }

    fun getStreamContent(requestId: String) {
        mAISPeakFetcher.getStreamContent(requestId)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        if (mContext!!.get() !is ChatActivity) return
                        val inputStream = response.body()?.byteStream()
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        var line: String?
                        val res = StringBuilder()
                        try {
                            while (reader.readLine().also { line = it } != null) {
                                if (line == null || line?.length!! < 10) {
                                    continue
                                }
                                res.append(line)
                                if (mContext == null || mContext!!.get() !is ChatActivity) return
                                (mContext!!.get() as ChatActivity).refreshCurrentCommandContent(
                                    line!!
                                )
                            }
                            reader.close()
                            inputStream?.close()
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }

            })
    }
}

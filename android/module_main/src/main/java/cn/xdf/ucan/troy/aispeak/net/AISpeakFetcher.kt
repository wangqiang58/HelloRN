package cn.xdf.ucan.troy.aispeak.net


import android.util.Log
import cn.xdf.ucan.troy.aispeak.bean.AIBaseResponseBean
import cn.xdf.ucan.troy.aispeak.bean.NextCommandData
import cn.xdf.ucan.troy.aispeak.bean.NextCommandRequestBean
import cn.xdf.ucan.troy.aispeak.bean.TopicInfoBean
import cn.xdf.ucan.troy.aispeak.bean.TopicRequestBean
import cn.xdf.ucan.troy.aispeak.bean.StudentInputBean
import cn.xdf.ucan.troy.lib.xdf.network.fetcher.BaseCommonRetrofitFetcher
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit

class AISpeakFetcher : BaseCommonRetrofitFetcher<Any, Any> {
    private var mAISpeakAPi: IAISpeakApi
    private var mRetrofit: Retrofit = createRetrofit(null)

    constructor() {
        mAISpeakAPi = mRetrofit.create(IAISpeakApi::class.java)
    }


    fun getTopicList(
        userId: String,
        topicId: Int
    ): Observable<AIBaseResponseBean<ArrayList<TopicInfoBean>>> {
        return mAISpeakAPi.getTopicList(TopicRequestBean(userId))
            .onErrorReturn { e ->
                Log.i("data !!! ", " error " + e.printStackTrace())
                return@onErrorReturn AIBaseResponseBean<ArrayList<TopicInfoBean>>(
                    "错误啦",
                    "请求错误",
                    23,
                    0,
                    null
                )
            }
    }


    override fun getInterceptorList(): MutableList<Interceptor> {
        return ArrayList()
    }

    fun getNextCommand(
        userId: String,
        sessionId: String,
        version: Long,
        topicId: Long,
        studentInput: StudentInputBean?
    ): Observable<AIBaseResponseBean<NextCommandData>> {
        return mAISpeakAPi.getNextCommand(
            NextCommandRequestBean(
                userId,
                sessionId,
                version,
                topicId,
                studentInput
            )
        ).onErrorReturn { e ->
            return@onErrorReturn AIBaseResponseBean<NextCommandData>(
                "错误啦",
                "请求错误",
                23,
                0,
                null
            )
        }
    }

    fun getStreamContent(requestId: String): Call<ResponseBody> {
        return mAISpeakAPi.getStreamContent(requestId)
    }

    fun getContent(requestId: String): Observable<AIBaseResponseBean<String>> {
        return mAISpeakAPi.getContent(requestId)
    }
    public var SSEUrl = "totoro/api/sse/get_sse_content"

}
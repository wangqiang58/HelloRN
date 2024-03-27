package cn.xdf.ucan.troy.aispeak.net

import cn.xdf.ucan.troy.aispeak.bean.AIBaseResponseBean
import cn.xdf.ucan.troy.aispeak.bean.NextCommandData
import cn.xdf.ucan.troy.aispeak.bean.NextCommandRequestBean
import cn.xdf.ucan.troy.aispeak.bean.TopicInfoBean
import cn.xdf.ucan.troy.aispeak.bean.TopicRequestBean
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface IAISpeakApi {


    @Headers("Content-Type:application/json")
    @POST("totoro/api/topic/query_topic_list")
    fun getTopicList(
        @Body topicRequestBean: TopicRequestBean
    ): Observable<AIBaseResponseBean<ArrayList<TopicInfoBean>>>

    @Headers("Content-Type:application/json")
    @POST("totoro/api/command/get_next_command")
    fun getNextCommand(@Body nextCommandRequestBean: NextCommandRequestBean): Observable<AIBaseResponseBean<NextCommandData>>

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("totoro/api/command/get_stream_content")
    fun getStreamContent(@Query("requestId") requestId: String?): Call<ResponseBody>

    @GET("totoro/api/command/get_content")
    fun getContent(@Query("requestId") requestId: String?): Observable<AIBaseResponseBean<String>>
}
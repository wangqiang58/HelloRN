package cn.xdf.ucan.troy.aispeak.mvvm.repository

import cn.xdf.ucan.troy.aispeak.bean.AIBaseResponseBean
import cn.xdf.ucan.troy.aispeak.bean.NextCommandData
import cn.xdf.ucan.troy.aispeak.bean.NextCommandRequestBean
import cn.xdf.ucan.troy.aispeak.bean.TopicInfoBean
import cn.xdf.ucan.troy.aispeak.bean.TopicRequestBean
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AIApi {
    @Headers("Content-Type:application/json")
    @POST("totoro/api/topic/query_topic_list")
    suspend fun getTopicList(@Body topicRequestBean: TopicRequestBean
    ):AIBaseResponseBean<ArrayList<TopicInfoBean>>

    @Headers("Content-Type:application/json")
    @POST("totoro/api/command/get_next_command")
    suspend fun getNextCommand(@Body nextCommandRequestBean: NextCommandRequestBean): AIBaseResponseBean<NextCommandData>

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("totoro/api/command/get_stream_content")
    suspend fun getStreamContent(@Query("requestId") requestId: String?): ResponseBody

    @GET("totoro/api/command/get_content")
    suspend fun getContent(@Query("requestId") requestId: String?): AIBaseResponseBean<String>
}
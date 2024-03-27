package cn.xdf.ucan.troy.lib.xdf.network.network

import com.google.gson.annotations.SerializedName

/**
 * Description: Api描述类，用于承载业务信息以及基础业务逻辑判断
 * Wiki：(if needed)
 * Author: wangmin
 * Date: 2023/05/15
 **/
class ApiResponse<T>(
    @field:SerializedName("haltTimestamp") val haltTimestamp: String? = "", // ??干嘛的
    @field:SerializedName("errorCode") val errorCode: String? = "",
    @field:SerializedName("status") var status: String? = "-1",
    @field:SerializedName("message") var message: String? = "",
    @field:SerializedName("data") var data:  T? = null,
)
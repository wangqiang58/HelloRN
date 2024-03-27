package cn.xdf.ucan.troy.lib.xdf.network.base

import cn.xdf.ucan.troy.lib.xdf.network.constant.NetworkConstant
import cn.xdf.ucan.troy.lib.xdf.network.network.ApiResponse
import cn.xdf.ucan.troy.lib.xdf.network.network.ResponseState
import cn.xdf.ucan.troy.lib.xdf.network.network.ResponseUtils

/**
 * Description: Base Repository
 * Wiki：(if needed)
 * Author: wangmin
 * Date: 2023/05/15
 **/
open class BaseRepository {
    /**
     * 返回结果状态 @see[NetworkConstant.STATUS_TYPE] Success Failed Exception
     */
    suspend fun <T> stateResponse(block: suspend () -> ApiResponse<T>): ResponseState<T> {
        return ResponseUtils.stateResponse(block)
    }
}

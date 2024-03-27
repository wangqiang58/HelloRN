package cn.xdf.ucan.troy.lib.xdf.network.network

import cn.xdf.ucan.troy.lib.xdf.network.constant.NetworkConstant
import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.net.ConnectException
import kotlinx.coroutines.CancellationException


/**
 * Description: Response Check Utils
 * Wiki：(if needed)
 * Author: wangmin
 * Date: 2023/05/15
 **/
object ResponseUtils {
    private const val errorMsg = "errorMsg"

    /**
     * 返回结果状态 @see[ResponseState] Success Failed Exception
     */
    suspend fun <T> stateResponse(block: suspend () -> ApiResponse<T>): ResponseState<T> {
        return try {
            val response = block.invoke()
            if (response.status == NetworkConstant.STATUS_TYPE.OK) {
                ResponseState.Success(response.data)
            } else {
                ResponseState.Failed(response.errorCode.orEmpty(), response.message.orEmpty())
            }
        } catch (e: Exception) {
            parseResponseError(e)
            ResponseState.Error(e, errorMsg)
        }
    }

    /**
     * parse Api Response Exception
     */
    private fun parseResponseError(exception: Exception) {
        when (exception) {
            is HttpException -> {
                // do something
            }

            is ConnectException -> {
                // do something
            }

            is JsonParseException -> {
                // do something
            }

            is CancellationException -> {
                // do something
            }

            else -> {
                // do something
            }
        }
    }
}
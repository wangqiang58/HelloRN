package cn.xdf.ucan.troy.lib.xdf.network.network

/**
 * Description:服务器数据返回状态
 * Wiki：(if needed)
 * Author: wangmin
 * Date: 2023/05/15
 **/
sealed interface ResponseState<T> {
    class Success<T>(val data: T?) : ResponseState<T>

    class Failed<T>(val code: String, val msg: String) : ResponseState<T>

    class Error<T>(val throwable: Throwable, val msg: String) : ResponseState<T>
}
package cn.xdf.ucan.troy.aispeak.bean

open class AIBaseResponseBean<T>(var desc: String = "",
                                 var message: String = "",
                                 var errorStatus: Long = 0,
                                 var status : Int = 0,
                                 var data:T? ) {
}
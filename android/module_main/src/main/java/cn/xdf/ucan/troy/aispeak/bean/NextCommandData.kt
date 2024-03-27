package cn.xdf.ucan.troy.aispeak.bean

data class NextCommandData(
    var version: Long = 0,
    var sessionId: String = "",
    var firstCommandContent: NextCommandResponseBean? = null,
    var secondCommandContent: NextCommandResponseBean? = null
) {
}
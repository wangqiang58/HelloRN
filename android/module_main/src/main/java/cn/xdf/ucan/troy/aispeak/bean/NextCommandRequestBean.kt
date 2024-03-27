package cn.xdf.ucan.troy.aispeak.bean

data class NextCommandRequestBean(
    var userId: String,
    var sessionId: String ?,
    var version: Long,
    var topicId: Long,
    var studentInput: StudentInputBean ?
) {


}
package cn.xdf.ucan.troy.aispeak.bean

/**
 * 展示聊天消息的模型
 */
data class NextCommandResponseBean(

    var actionRole: String ,
    var commandType: String ,
    var commandContentDetail: CommandContentDetail?,
    var requestId: String ?,
) {

    public var commandUUID:String = ""
    public var alreadyPlay = false
    public fun appendText(content: String) {
        this.commandContentDetail!!.text += "$content";
    }

    override fun hashCode(): Int {
        return commandUUID.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is NextCommandResponseBean) return false
        return this.commandUUID === other.commandUUID
    }
}
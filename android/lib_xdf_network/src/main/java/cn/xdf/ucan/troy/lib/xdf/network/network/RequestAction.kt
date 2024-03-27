package cn.xdf.ucan.troy.lib.xdf.network.network

/**
 * @Description: 页面请求Action
 * @Wiki:
 * @Author: WangMin
 * @CreateDate: 2023/6/12 4:01 PM
 */
sealed class RequestAction

object FirstInit : RequestAction()     // 首次更新
object PullToRefresh : RequestAction()  // 下拉刷新
object LoadMore : RequestAction()  // 上拉加载更多
object Resume : RequestAction() // 页面再次可见时
object NetworkChange : RequestAction()  // 网络情况改变
object Retry : RequestAction() // 重试
object Polling : RequestAction() // 轮询
object Rank : RequestAction() // 排序
class SwitchTab(position: Int) : RequestAction()  // 切换Tab
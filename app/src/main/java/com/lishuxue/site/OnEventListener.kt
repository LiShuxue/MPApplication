package com.lishuxue.site

/**
 * 事件监听接口
 */
interface OnEventListener {
    /**
     * Service层触发，表示Service已准备完毕
     */
    fun onServiceReady()

    /**
     * Service层触发，通知View层的订阅处理器处理
     */
    fun notifyPageSubscribeHandler(event: String?, params: String?, viewIds: IntArray?)

    /**
     * Page层触发，通知Service层的订阅处理器处理
     */
    fun notifyServiceSubscribeHandler(event: String?, params: String?, viewId: Int)

    /**
     * Service层触发，通知Page层处理页面相关api事件
     */
    fun onPageEvent(event: String?, params: String?)
}
package com.lishuxue.site

/**
 * H5 --> Native 的调用接口
 */
interface IBridge {
    /**
     * 发布事件，由Service层或View层的JSBridge调用
     */
    fun publish(event: String?, params: String?, viewIds: String?)

    /**
     * 调用事件，由Service层或View层的JSBridge调用
     */
    fun invoke(event: String?, params: String?, callbackId: String?)

    /**
     * 事件处理完成后的回调
     */
    fun callback(callbackId: String?, result: String?)
}
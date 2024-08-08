package com.lishuxue.site

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.JavascriptInterface


/**
 * js与native通信的通道类
 */
class JSInterface(handler: IBridge?) {
    private val mpBridgeHandler: IBridge?
    private val mHandler = Handler(Looper.getMainLooper())

    init {
        Log.d(TAG, "供js中调用的JSInterface初始化: 主要是publish和invoke方法")
        mpBridgeHandler = handler
    }

    @JavascriptInterface
    fun publish(event: String?, params: String?, viewIds: String?) {
        mHandler.post {
            mpBridgeHandler?.publish(event, params, viewIds)
        }
    }

    @JavascriptInterface
    fun invoke(event: String?, params: String?, callbackId: String?) {
        mHandler.post {
            mpBridgeHandler?.invoke(event, params, callbackId)
        }
    }

    companion object {
        const val TAG = "MPApplication-JSInterface"
    }
}
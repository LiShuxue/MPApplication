package com.lishuxue.site

import MPWebView
import android.content.Context
import android.util.Log
import android.widget.LinearLayout


/**
 * app service 层，小程序运行时的基石，即framework的运行时
 */
class AppService(
    context: Context,
    listener: OnEventListener
) : LinearLayout(context), IBridge {
    private val serviceWebView: MPWebView
    private val serviceEventListener: OnEventListener

    init {
        Log.d(TAG, "初始化AppService，赋值serviceWebView 和 serviceEventListener")
        serviceWebView = MPWebView(context)
        serviceEventListener = listener
        serviceWebView.setJsHandler(this, "AppServiceJSBridge")
        addView(
            serviceWebView, LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(TAG, "serviceWebView加载service.html ")
        // 加载service.html
        serviceWebView?.loadUrl("file:///android_asset/service.html")
    }

    override fun publish(event: String?, params: String?, viewIds: String?) {
        Log.d(TAG, "触发到原生AppService中的 publish: $event")
        if ("serviceReady" == event) {
            onEventServiceReady();
        } else if ("appDataChange" == (event)) {
            onEventAppDataChanged(event, params, viewIds!!);
        }
    }

    override fun invoke(event: String?, params: String?, callbackId: String?) {
        TODO("Not yet implemented")
    }

    override fun callback(callbackId: String?, result: String?) {
        TODO("Not yet implemented")
    }

    /**
     * service.html加载完成
     */
    private fun onEventServiceReady() {
        Log.d(MPActivity.TAG, "AppService中onServiceReady")
        serviceEventListener.onServiceReady()
    }

    /**
     * 页面数据改变事件
     */
    private fun onEventAppDataChanged(event: String, params: String?, viewIds: String) {
        // 转Page层订阅处理器处理
        serviceEventListener?.notifyPageSubscribeHandler(
            event, params,
            JsonUtil.parseToIntArray(viewIds)
        )
    }

    /**
     * Service层的订阅处理器处理事件
     */
    fun subscribeHandler(event: String?, params: String?, viewId: Int) {
        Log.d(TAG, "AppService层执行 jsBridge.subscribeHandler")
        val jsFun = String.format(
            "javascript:jsBridge.subscribeHandler('%s',%s,%s)",
            event, params, viewId
        )
        serviceWebView.loadUrl(jsFun)
    }

    companion object {
        const val TAG = "MPApplication-AppService"
    }
}

package com.lishuxue.site

import MPWebView
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import org.json.JSONException
import org.json.JSONObject


/**
 * Page层，即小程序view展示层
 */
class Page(context: Context, pagePath: String, listener: OnEventListener) :
    LinearLayout(context), IBridge {
    private val pageWebView: MPWebView
    private val pageEventListener: OnEventListener
    private val url: String

    init {
        Log.d(TAG, "初始化Page，赋值pageWebView 和 pageEventListener")

        url = pagePath
        pageEventListener = listener
        LayoutInflater.from(context).inflate(R.layout.mp_page, this, true)
        val webLayout = findViewById<FrameLayout>(R.id.web_layout)
        pageWebView = MPWebView(context)
        pageWebView.setJsHandler(this, "PageJSBridge")
        webLayout.addView(
            pageWebView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        pageWebView.loadUrl(url)
    }

    private val viewId: Int
        get() = pageWebView?.getViewId() ?: 0

    override fun publish(event: String?, params: String?, viewIds: String?) {
        Log.d(TAG, "触发到原生Page中的 publish: $event")
        if ("DOMContentLoaded" == event) {
            onDomContentLoaded()
        } else {
            onPageEvent(event, params)
        }
    }

    override fun invoke(event: String?, params: String?, callbackId: String?) {
    }

    override fun callback(callbackId: String?, result: String?) {}

    /**
     * 页面dom内容加载完成
     */
    private fun onDomContentLoaded() {
        val eventName = "DOMContentLoaded"
        var eventParams = "{}"

        val json = JSONObject()
        json.put("webviewId", viewId)
        json.put("url", url)
        eventParams = json.toString()

        // 通知Service层的订阅处理器处理
        pageEventListener?.notifyServiceSubscribeHandler(eventName, eventParams, viewId)
    }

    /**
     * 页面事件
     */
    private fun onPageEvent(event: String?, params: String?) {
        // 转由PageManager层处理
        pageEventListener?.onPageEvent(event, params)
    }


    /**
     * Page层的订阅处理器处理事件
     */
    fun subscribeHandler(event: String?, params: String?, viewIds: IntArray?) {
        if (viewIds == null || viewIds.isEmpty()) {
            return
        }
        for (id in viewIds) {
            if (id == viewId) {
                val jsFun = String.format(
                    "javascript:PageJSBridge.subscribeHandler('%s',%s)",
                    event, params
                )
                pageWebView.loadUrl(jsFun)
                break
            }
        }
    }

    companion object {
        const val TAG = "MPApplication-Page"
    }
}

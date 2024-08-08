package com.lishuxue.site

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.FrameLayout


/**
 * Page 管理类
 */
class PageManager(context: Context) {
    private val mContext: Context
    private val container: FrameLayout

    init {
        Log.d(TAG, "初始化PageManager: 一个容器，添加和移除Page")
        mContext = context
        container = FrameLayout(context)
    }

    /**
     * 获取页面的容器
     */
    fun getContainer(): FrameLayout? {
        return container
    }

    /**
     * 获取当前页面数
     */
    fun getPageCount(): Int {
        return container.childCount
    }

    /**
     * 获取指定索引的页面
     */
    private fun getPageAt(index: Int): Page {
        return container.getChildAt(index) as Page
    }

    /**
     * 自顶部向下移除delta个页面，delta取值范围[1, [.getPageCount]-1]，否则不做移除
     */
    private fun removePages(delta: Int) {
        Log.d(TAG, "PageManager从容器顶部移除Page")
        val pageCount = getPageCount()
        if (delta <= 0 || delta >= pageCount) {
            return
        }
        for (i in pageCount - delta until pageCount) {
            container.removeViewAt(i)
        }
    }

    /**
     * 创建并添加一个页面
     */
    fun createPage(url: String, listener: OnEventListener) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        Log.d(TAG, "创建一个Page: $url")
        val page = Page(mContext, url, listener)
        Log.d(TAG, "PageManager将Page添加到容器，页面显示")
        container.addView(
            page, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
    }

    /**
     * 订阅事件处理器
     */
    fun subscribeHandler(event: String?, params: String?, viewIds: IntArray?) {
        if (viewIds == null || viewIds.isEmpty()) {
            return
        }
        val count = getPageCount()
        for (i in 0 until count) {
            val page = getPageAt(i)
            page.subscribeHandler(event, params, viewIds)
        }
    }

    /**
     * 处理页面事件
     */
    fun handlePageEvent(event: String, params: String?, listener: OnEventListener?) {
        Log.d(TAG, "PageManager处理page.html中发出的事件")
        if ("navigateTo" == event) {
            navigateToPage(JsonUtil.getStringValue(params, "url", ""), listener!!)
        } else if ("navigateBack" == event) {
            navigateBackPage(JsonUtil.getIntValue(params, "delta", 0))
        }
    }

    /**
     * 导航到页面
     */
    private fun navigateToPage(url: String, listener: OnEventListener) {
        Log.d(TAG, "原生中navigateToPage")
        if (TextUtils.isEmpty(url)) {
            return
        }
        val path = "file:///android_asset/$url"
        createPage(path, listener)
    }

    /**
     * 导航返回页面
     */
    fun navigateBackPage(delta: Int) {
        Log.d(TAG, "原生中navigateBackPage")
        removePages(delta)
    }

    companion object {
        private const val TAG = "MPApplication-PageManager"
    }
}

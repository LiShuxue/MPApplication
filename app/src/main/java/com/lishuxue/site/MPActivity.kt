package com.lishuxue.site

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity


class MPActivity : AppCompatActivity(), OnEventListener {
    private lateinit var mpContainer: FrameLayout
    private lateinit var mpAppService: AppService
    private lateinit var mpPageManager: PageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mpactivity)

        Log.d(TAG, "MPActivity create")
        // 小程序的Activity中，有一个容器，容器中有两个线程，一个是用来执行js逻辑的，另一个是用来渲染页面的
        // 初始化容器
        mpContainer = findViewById(R.id.container)
        // 初始化双线程
        initPage(mpContainer)

        // 添加返回按钮的回调
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mpPageManager.getPageCount() > 1) {
                    mpPageManager.navigateBackPage(1)
                } else {
                    // 如果没有页面，则调用默认的返回行为
                    finish()
                }
            }
        })
    }

    /**
     * 加载页面
     */
    private fun initPage(container: FrameLayout) {
        Log.d(TAG, "初始化双线程：AppService 和 PageManager")
        // 初始化js线程
        mpAppService = AppService(this, this)
        // 初始化页面渲染线程
        mpPageManager = PageManager(this)

        container.addView(
            mpAppService, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        container.addView(
            mpPageManager.getContainer(), FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
    }

    override fun onServiceReady() {
        Log.d(TAG, "MPActivity中onServiceReady，使用PageManager创建页面")
        mpPageManager.createPage("file:///android_asset/page1.html", this);
    }


    override fun notifyPageSubscribeHandler(event: String?, params: String?, viewIds: IntArray?) {
        mpPageManager.subscribeHandler(event, params, viewIds)
    }

    override fun notifyServiceSubscribeHandler(event: String?, params: String?, viewId: Int) {
        Log.d(TAG, "MPActivity中通知AppService层的订阅处理器处理: $event")
        mpAppService.subscribeHandler(event, params, viewId)
    }

    override fun onPageEvent(event: String?, params: String?) {
        Log.d(TAG, "MPActivity中通知PageManager层处理: $event")
        return mpPageManager.handlePageEvent(event!!, params, this)
    }

    companion object {
        const val TAG = "MPApplication-MPActivity"
    }
}
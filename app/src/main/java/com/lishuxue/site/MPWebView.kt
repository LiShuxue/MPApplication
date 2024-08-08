import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.webkit.WebView
import com.lishuxue.site.IBridge
import com.lishuxue.site.JSInterface


class MPWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    init {
        Log.d(TAG, "MPWebView 初始化")
        // 初始化 WebView 的设置
        // 使用js
        settings.javaScriptEnabled = true
        // 使用本地文件
        settings.allowFileAccess = true
    }

    fun setJsHandler(handler: IBridge?, name: String) {
        Log.d(TAG, "初始化原生桥 $name 和 对应的原生处理类 ${handler?.javaClass?.simpleName}")
        addJavascriptInterface(JSInterface(handler), name)
    }

    fun getViewId(): Int {
        return hashCode()
    }

    companion object {
        const val TAG = "MPApplication-MPWebView"
    }
}
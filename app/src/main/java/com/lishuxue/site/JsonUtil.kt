package com.lishuxue.site

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * json 操作工具类
 */
object JsonUtil {
    private const val TAG = "MPApplication-MPApplication-JsonUtil"

    /**
     * 从被给的JSON字符串中获取指定key的字符串类型值
     */
    fun getStringValue(json: String?, key: String?, defaultValue: String): String {
        try {
            val paramJson = JSONObject(json)
            if (paramJson.has(key)) {
                return paramJson.optString(key)
            }
        } catch (e: JSONException) {
            Log.e(TAG, "getStringValue exception!")
        }
        return defaultValue
    }

    /**
     * 从被给的JSON字符串中获取指定key的数字类型值
     */
    fun getIntValue(json: String?, key: String?, defaultValue: Int): Int {
        try {
            val paramJson = JSONObject(json)
            if (paramJson.has(key)) {
                return paramJson.optInt(key)
            }
        } catch (e: JSONException) {
            Log.e(TAG, "getIntValue exception!")
        }
        return defaultValue
    }

    /**
     * 解析 JSONArray 到 int 数组
     */
    fun parseToIntArray(jsonArrayStr: String?): IntArray {
        var ids: IntArray
        try {
            val jsonArray = JSONArray(jsonArrayStr)
            val len = jsonArray.length()
            if (len > 0) {
                ids = IntArray(len)
                for (i in 0 until len) {
                    ids[i] = jsonArray.getInt(i)
                }
                return ids
            }
        } catch (e: JSONException) {
            Log.e(
                TAG,
                String.format("parseViewIds(%s) exception, %s", jsonArrayStr, e.message)
            )
        }
        ids = IntArray(1)
        ids[0] = 0
        return ids
    }
}

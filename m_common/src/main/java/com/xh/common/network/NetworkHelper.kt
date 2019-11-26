package com.xh.common.network

import com.blankj.utilcode.util.ToastUtils
import com.google.gson.JsonSyntaxException
import com.xh.common.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


inline fun getErrorHintHelper(throwable: Throwable): String {
    var error = ""
    when (throwable) {
        is ApiException -> error = throwable.message ?: ""
        is KeyErrorException -> {
//                NetworkApi.instance.cleanKey()
            error = "请重试一下"
        }
        is LoginExpiredException -> {

        }
        is SocketTimeoutException -> error = "连接超时"
        is ConnectException -> error = "网络中断，请检查您的网络状态"
        is UnknownHostException -> error = "还没联网"
        is JsonSyntaxException, is JSONException -> error = "数据解析错误"
        else -> error = "发生未知错误"
    }
    return error
}


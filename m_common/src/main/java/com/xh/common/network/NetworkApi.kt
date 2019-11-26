package com.xh.common.network

import com.blankj.utilcode.util.LogUtils
import com.xh.common.config.BaseConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


object NetworkApi {

    private val HTTP_TIME_OUT = 10L
    private val cacheSize = (1024 * 1024 * 20).toLong()// 缓存文件最大限制大小20M
    private val cacheDirectory = BaseConfig.CACHE_PATH + "/http" // 设置缓存文件路径

    val retrofit: Retrofit
    var mLoginTimeOut: (() -> Unit)? = null


    init {
        val okHttpClient = OkHttpClient()
            .newBuilder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .connectTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .cache(Cache(File(cacheDirectory), cacheSize))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.ai.qq.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    }

    suspend inline fun <T> getData(
        call: Call<HttpResBean<T>>,
        crossinline errorListener: (e: Throwable?) -> Unit = {},
        isShowErrorHint: Boolean = true
    ): T? {
        var throwable: Throwable? = null
        val result = withContext(Dispatchers.IO) {
            try {
                val bean = call.execute().body()!!
                when (bean.ret) {
                    0 -> bean.data ?: bean.data as? T
                    ?: throw ApiException("报错")
                    1 -> throw LoginExpiredException()
                    else -> throw ApiException("报错")
                }
                return@withContext bean.data!!
            } catch (e: Throwable) {
                throwable = e
            }
            return@withContext null
        }
        if (throwable != null) {
            if (isShowErrorHint) {
                val hint = getErrorHintHelper(throwable!!)
//                hint.showToast()
                LogUtils.e(hint)
            }
            if (throwable is LoginExpiredException) {
                mLoginTimeOut?.invoke()
            } else {
                errorListener.invoke(throwable)
            }
        }
        return result
    }


}



package com.xh.common.network

/**
 * Created by Administrator on 2017/6/13.
 */



data class HttpResBean<T>(var ret: Int = 0,
                       var data: T? = null,
                       var msg: String = "")




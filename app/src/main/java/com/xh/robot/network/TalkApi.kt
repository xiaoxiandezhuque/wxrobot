package com.xh.common.network.api


import com.xh.common.network.HttpResBean
import com.xh.robot.bean.TalkBean
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TalkApi {
    @FormUrlEncoded
    @POST("fcgi-bin/nlp/nlp_textchat")
    fun talk(@FieldMap map: Map<String, String>): Call<HttpResBean<TalkBean>>
}


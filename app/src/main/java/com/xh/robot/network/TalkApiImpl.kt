package com.xh.robot.network.api

import com.xh.common.network.NetworkApi
import com.xh.common.network.api.TalkApi
import com.xh.robot.bean.TalkBean
import com.xh.robot.util.FileUtil
import com.xh.robot.util.MapUtil

class TalkApiImpl {
    private val mApi = NetworkApi.retrofit.create(TalkApi::class.java)

    suspend fun talk(context: String): TalkBean? {
        val bean = FileUtil.getRobot()
        if (bean == null) {
            return null
        }
        val map = hashMapOf<String, String>()
        map.put("app_id", bean.id)
        map.put("time_stamp", (System.currentTimeMillis() / 1000).toInt().toString())
        map.put("nonce_str", "fa577ce340859f9fe")
        map.put("session", "10000")
        map.put("question", context)
        map.put("sign", MapUtil.getSignature(map, bean.key))
        return NetworkApi.getData(mApi.talk(map))
    }

}

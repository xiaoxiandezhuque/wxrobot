package com.xh.robot.hook

import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.xh.robot.bean.SendMessageBean
import com.xh.robot.network.api.TalkApiImpl
import com.xh.robot.util.WxUtil
import de.robv.android.xposed.XC_MethodHook
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class ReceiveAndSendMessage : XC_MethodHook() {

    private val mApi = TalkApiImpl()
    private val mMainScope = MainScope()


    override fun beforeHookedMethod(param: MethodHookParam?) {
        super.beforeHookedMethod(param)
        LogUtils.e(
            param!!.args[1].toString(),
            param.args[2].toString(),
            param.args[3].toString(),
            param.args[4].toString(),
            param.args[5].toString()
        )
        //  1接收的是消息，3接收的是图片  47 表情
        if (param.args[3] != 1) {
            return
        }

        mMainScope.coroutineContext.cancelChildren()
        mMainScope.launch {
            delay(1000)
            val wxid = param.args[1].toString()
            var content = param.args[2].toString()

            if (StringUtils.isTrimEmpty(wxid) || StringUtils.isTrimEmpty(content)
                || content.startsWith("<?xml version=\"1.0\"?>")
            ) {
                return@launch
            }
            if (wxid.endsWith("@chatroom")) {
                val array = content.split("\n")
                if (array.size > 1) {
                    content = array[1]
                }
                if (StringUtils.isTrimEmpty(content)) {
                    return@launch
                }
                val nickname = WxUtil.getUserInfo(param.args[0], BaseHook.classLoader, 4)

                if (StringUtils.isTrimEmpty(nickname) || !content.contains("@$nickname")) {
                    return@launch
                }
                content = content.replace("@$nickname", "")
                if (StringUtils.isTrimEmpty(content)) {
                    return@launch
                }
            }
            LogUtils.e("wxid=${wxid}--content=${content}")
            try {
                val bean = mApi.talk(content)
                if (bean != null) {
                    WxUtil.sendMessage(
                        param.args[0], BaseHook.classLoader,
                        SendMessageBean(wxid, bean.answer)
                    )
                }
            } catch (e: Exception) {
                LogUtils.e(Log.getStackTraceString(e))
            }

        }


    }


}

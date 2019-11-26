package com.xh.robot.util

import com.xh.robot.bean.SendMessageBean
import de.robv.android.xposed.XposedHelpers

object WxUtil {


    //发送消息
    fun sendMessage(any: Any, classLoader: ClassLoader, sendMessageBean: SendMessageBean) {
        val azClass = XposedHelpers.findClass("com.tencent.mm.model.az", classLoader)
        val hClass = XposedHelpers.findClass("com.tencent.mm.modelmulti.h", classLoader)
        val pClass = XposedHelpers.findClass("com.tencent.mm.aj.p", classLoader)
        val mClass = XposedHelpers.findClass("com.tencent.mm.aj.m", classLoader)

        val zsMethod = azClass.getMethod("ZS")
        val p = zsMethod.invoke(any)
        val aMethod = pClass.getMethod("a", mClass, Int::class.java)

        val hCon = hClass.getConstructor(
            String::class.java,
            String::class.java,
            Int::class.java,
            Int::class.java,
            Any::class.java
        )
        val h = hCon.newInstance(sendMessageBean.wxid, sendMessageBean.content, 1, 0, null)
        aMethod.invoke(p, h, 0)
    }

    //    获取个人信息

    //
    fun getUserInfo(any: Any, classLoader: ClassLoader, type: Int): String {
        val gClass = XposedHelpers.findClass("com.tencent.mm.kernel.g", classLoader)
        val eClass = XposedHelpers.findClass("com.tencent.mm.kernel.e", classLoader)
        val zClass = XposedHelpers.findClass("com.tencent.mm.storage.z", classLoader)
//        aaC    fanhui e    aak  fanhui  z   get(int i, Object obj)

        val aacMethod = gClass.getMethod("aaC")
        val eAny = aacMethod.invoke(any)

        val aakMethod = eClass.getMethod("aak")
        val zAny = aakMethod.invoke(eAny)

        val getMethod = zClass.getMethod("get", Int::class.java, Any::class.java)

        return getMethod.invoke(zAny, type, null) as String

    }

}
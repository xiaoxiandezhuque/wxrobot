package com.xh.robot.hook

import android.content.ContextWrapper
import android.database.Cursor
import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import com.xh.robot.bean.WxUserBean
import com.xh.robot.ui.UserListDialog
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import java.util.*

class HomeUiActionBar(private val classLoader: ClassLoader) : XC_MethodHook() {

    private val mUserNameList = mutableListOf<WxUserBean>()
    private var mDialog: UserListDialog? = null

    override fun beforeHookedMethod(param: MethodHookParam?) {
        super.beforeHookedMethod(param)
        try {
            throw NullPointerException()
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.e(Log.getStackTraceString(e))
        }
    }

    override fun afterHookedMethod(param: MethodHookParam) {
        super.afterHookedMethod(param)

        val barClass = XposedHelpers.findClass("android.support.v7.app.ActionBar", classLoader)
        val homeUiClass = XposedHelpers.findClass("com.tencent.mm.ui.HomeUI", classLoader)
        val field = homeUiClass.getDeclaredField("mActionBar")
        field.isAccessible = true
        val actionBar = field.get(param.thisObject)
        val getViewMethod = barClass.getMethod("getCustomView")
        val view = getViewMethod.invoke(actionBar) as LinearLayout

        val llView = view.getChildAt(1)
        val layoutParams = llView.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = 0
        layoutParams.weight = 1f
        llView.layoutParams = layoutParams
        val textView = TextView(view.context)
        textView.text = "聊天列表"
        textView.setTextColor(Color.RED)
        view.addView(textView)
        textView.setOnClickListener {
            try {
//                send(param.thisObject)
                if (mDialog == null || ((mDialog!!.context) as ContextWrapper).baseContext !== it.context) {
                    mUserNameList.clear()
                    findHomeUser(param.thisObject)
                    findMailListUser(param.thisObject)
                    mDialog = UserListDialog(it.context, { imageView, username ->
                        setImageView(param.thisObject, imageView, username)
                    })
                    mDialog?.setData(mUserNameList)
                }

                mDialog?.show()

            } catch (e: Exception) {
                LogUtils.e(Log.getStackTraceString(e))
            }


        }

    }


    private fun findHomeUser(any: Any) {
        val cClass = XposedHelpers.findClass("com.tencent.mm.model.c", classLoader)
        val beClass = XposedHelpers.findClass("com.tencent.mm.storage.be", classLoader)
        val wClass = XposedHelpers.findClass("com.tencent.mm.model.w", classLoader)
        val akClass = XposedHelpers.findClass("com.tencent.mm.storage.ak", classLoader)


        val ajfMethod = cClass.getMethod("ajf")
        val beAny = ajfMethod.invoke(any)


        val aMethod = beClass.getMethod(
            "a", String::class.java, List::class.java,
            String::class.java, Boolean::class.java
        )
//        "@im.chatroom", "@chatroom", "@openim", "@micromsg.qq.com"
        val gdgField = wClass.getField("gdg")
        val gdgStr = gdgField.get(any)

        val list = ArrayList<String>()
        list.add("qmessage")
        val cursor = aMethod.invoke(beAny, gdgStr, list, null, true) as Cursor

        while (cursor.moveToNext()) {
            val akAny = akClass.newInstance()
            val convertFromMethod = akClass.getMethod("convertFrom", Cursor::class.java)
            convertFromMethod.invoke(akAny, cursor)

            val field_username = akClass.getField("field_username")
            val name = field_username.get(akAny) as String

            if (name.endsWith("@chatroom")) {
                val nickname = findGroupNicknameByUsername(any, name)
                mUserNameList.add(WxUserBean(nickname, name, true))
                LogUtils.e("首页 wxid=${name} nickname= ${nickname}")
            }
        }
    }


    //找通讯录的用户
    private fun findMailListUser(any: Any) {
        val cClass = XposedHelpers.findClass("com.tencent.mm.model.c", classLoader)
        val bdClass = XposedHelpers.findClass("com.tencent.mm.storage.bd", classLoader)
        val fClass = XposedHelpers.findClass("com.tencent.mm.storage.f", classLoader)


        val ajaMethod = cClass.getMethod("aja")
        val bdAny = ajaMethod.invoke(any)

        val aMethod = bdClass.getMethod(
            "a", String::class.java,
            String::class.java, List::class.java, List::class.java,
            Boolean::class.java, Boolean::class.java
        )

        val list1 = LinkedList<String>()
        list1.add("tmessage")
        list1.add("officialaccounts")
        list1.add("helper_entry")
        list1.add("blogapp")
        val list2 = LinkedList<String>()
        list1.add("weixin")
        val cursor = aMethod.invoke(
            bdAny, "@all.contact.without.chatroom.without.openim",
            null, list1, list2, true, true
        ) as Cursor
        while (cursor.moveToNext()) {
            val akAny = fClass.newInstance()
            val convertFromMethod = fClass.getMethod("convertFrom", Cursor::class.java)
            convertFromMethod.invoke(akAny, cursor)
            val field_username = fClass.getField("field_username")
            val name = field_username.get(akAny) as String
            val field_nickname = fClass.getField("field_nickname")
            val nickname = field_nickname.get(akAny) as String

            val field_verifyFlag = fClass.getField("field_verifyFlag")
            val verifyFlag = field_verifyFlag.get(akAny) as Int

            if (verifyFlag != 0) {
                LogUtils.e("通讯录 id=${name} nickname= ${nickname} verifyFlag=$verifyFlag ")
            }
            mUserNameList.add(WxUserBean(nickname, name, false))


        }
    }


    private fun findGroupNicknameByUsername(any: Any, username: String): String {
        val gClass = XposedHelpers.findClass("com.tencent.mm.kernel.g", classLoader)
        val cClass = XposedHelpers.findClass("com.tencent.mm.plugin.chatroom.a.c", classLoader)
        val ajClass = XposedHelpers.findClass("com.tencent.mm.model.aj", classLoader)

        val acMethod = gClass.getMethod("ac", Class::class.java)
        val cAny = acMethod.invoke(any, cClass)

        val ajjMethod = cClass.getMethod("ajj")
        val ajAny = ajjMethod.invoke(cAny)

        val ntMethod = ajClass.getMethod("nt", String::class.java)
        return ntMethod.invoke(ajAny, username) as String
    }

    private fun setImageView(any: Any, imageView: ImageView, username: String) {
        val gClass = XposedHelpers.findClass("com.tencent.mm.pluginsdk.ui.a\$b", classLoader)

        val cMethod = gClass.getMethod("c", ImageView::class.java, String::class.java)
        cMethod.invoke(any, imageView, username)

    }


}

package com.xh.robot.util

import android.os.Environment
import com.blankj.utilcode.util.FileIOUtils
import com.google.gson.Gson
import com.xh.robot.bean.RobotBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FileUtil {

    private val ROBOT_PATH =
        Environment.getExternalStorageDirectory().absolutePath + "/wxrobot/robot"

    suspend fun saveRobot(bean: RobotBean): String {
        return withContext(Dispatchers.IO) {
            FileIOUtils.writeFileFromString(
                ROBOT_PATH,
                Gson().toJson(bean)
            )
            "保存成功"
        }
    }

    suspend fun getRobot(): RobotBean? {
        return withContext(Dispatchers.IO) {
            Gson().fromJson(
                FileIOUtils.readFile2String(ROBOT_PATH),
                RobotBean::class.java
            )
        }
    }
}
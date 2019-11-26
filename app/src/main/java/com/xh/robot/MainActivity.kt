package com.xh.robot

import android.Manifest
import com.blankj.utilcode.util.StringUtils
import com.xh.common.base.BaseActivity
import com.xh.common.ktextended.showToast
import com.xh.robot.bean.RobotBean
import com.xh.robot.util.FileUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import permissions.dispatcher.*

@RuntimePermissions
class MainActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {

        btn_save.setOnClickListener {
            saveWithPermissionCheck()
        }
//        btn_print.setOnClickListener {
//            mMainScope.launch {
//                FileUtil.getRobot().toString().showToast()
//            }
//
//        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun save() {
        val id = et_id.text.toString()
        val key = et_key.text.toString()
        if (StringUtils.isTrimEmpty(id)) {
            et_id.hint.toString().showToast()
            return
        }
        if (StringUtils.isTrimEmpty(key)) {
            et_key.hint.toString().showToast()
            return
        }
        mMainScope.launch {
            val state = FileUtil.saveRobot(RobotBean(id, key))
            state.showToast()
        }
    }


    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onFileDenied() {
        "我要存文件,不然没发使用".showToast()
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showRationaleForFile(request: PermissionRequest) {
        "我要存文件".showToast()
        request.proceed()
//        request.cancel()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onFileNeverAskAgain() {
        "不在询问".showToast()
    }


}

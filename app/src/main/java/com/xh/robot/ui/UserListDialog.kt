package com.xh.robot.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.xh.robot.bean.WxUserBean


class UserListDialog(
    private val mContext: Context,
    private val loadImageHead: (imageView: ImageView, username: String) -> Unit
) : Dialog(mContext) {

    private val mAdapter by lazy {
        UserListAdapter(mContext, loadImageHead)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recyclerView = RecyclerView(mContext)
        recyclerView.setBackgroundColor(Color.parseColor("#ffffff"))
        recyclerView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setContentView(recyclerView)


        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                mContext,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.adapter = mAdapter


        window!!.setBackgroundDrawable(BitmapDrawable())
        val w = window
        val lp = w!!.attributes
        w.setGravity(Gravity.CENTER)
        lp.width = SizeUtils.dp2px(300f)
        lp.height = SizeUtils.dp2px(400f)
        w.attributes = lp
    }

    fun setData(list: List<WxUserBean>) {
        mAdapter.resetData(list)
    }
}

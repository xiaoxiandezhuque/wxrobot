package com.xh.robot.ui

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.blankj.utilcode.util.SizeUtils
import com.xh.common.base.ViewHolder
import com.xh.robot.bean.WxUserBean

class UserListAdapter(
    context: Context,
    private val loadImageHead: (imageView: ImageView, username: String) -> Unit
) : BaseHookAdapter<WxUserBean>(context, false) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val linear = LinearLayout(mContext)
        linear.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        linear.setPadding(SizeUtils.dp2px(10f))
        linear.orientation = LinearLayout.HORIZONTAL

        val imageView = ImageView(mContext)
        imageView.layoutParams = ViewGroup.LayoutParams(
            SizeUtils.dp2px(50f),
            SizeUtils.dp2px(50f)
        )


        val textview = TextView(mContext)
        textview.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textview.setPadding(SizeUtils.dp2px(5f), 0, 0, 0)
        textview.setSingleLine()
        textview.setTextSize(SizeUtils.sp2px(8f).toFloat())

        linear.addView(imageView)
        linear.addView(textview)
        return ViewHolder(linear)
    }

    override fun onMyBindViewHolder(holder: ViewHolder, position: Int, data: WxUserBean) {
        super.onMyBindViewHolder(holder, position, data)
        val linear = holder.convertView as LinearLayout
        loadImageHead.invoke(linear.getChildAt(0) as ImageView, data.field_username)

        (linear.getChildAt(1) as TextView).setText(data.field_nickname)

    }
}
package com.xh.robot.ui


import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xh.common.base.ViewHolder


abstract class BaseHookAdapter<T> constructor(
    protected val mContext: Context,
    private var isOpenAddMore: Boolean = true
) : RecyclerView.Adapter<ViewHolder>() {


    val mData: MutableList<T>


    private var mItemClickListener: ((view: View, postion: Int, data: T) -> Unit)? = null

    private var isOpenNullLayout: Boolean = false
    private var mNullLayoutResId: Int = 0


    init {

        mData = mutableListOf()
    }


    override fun getItemCount(): Int {

        return mData.size
    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        onMyBindViewHolder(holder, position, getItemData(position))
        holder.convertView.setOnClickListener { v ->
            mItemClickListener?.invoke(v, position, mData[position])
        }

    }


    override fun getItemViewType(position: Int): Int {
        return LAYOUT_ORDINARY
    }


    //重置数据
    fun resetData(dataList: List<T>) {
        mData.clear()
        addAllData(dataList)
        notifyDataSetChanged()
    }

    fun addAllData(dataList: List<T>) {

        mData.addAll(dataList)
        notifyDataSetChanged()
//        notifyItemRangeChanged(mData.size, dataList.size)
    }

    fun addData(data: T) {
        mData.add(data)
        notifyItemChanged(mData.size)

    }

    fun addData(position: Int, data: T) {
        mData.add(position, data)
        notifyItemInserted(position)
    }

    fun replaceData(position: Int, data: T) {
        mData[position] = data
        notifyItemChanged(position)
    }

    fun updateData(position: Int) {
        if (itemCount > position) {
            notifyItemChanged(position)
        }
    }


    fun removeData(data: T) {
        if (mData.contains(data)) {
            val position = mData.indexOf(data)
            this.mData.remove(data)
            notifyItemRemoved(position)
        }
    }

    fun removeData(position: Int) {
        if (this.itemCount > position) {
            this.mData.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItemData(position: Int): T {
        return mData[position]
    }


    fun setOnItemClickListener(listener: (view: View, postion: Int, data: T) -> Unit) {
        mItemClickListener = listener
    }


    open protected fun onMyBindViewHolder(holder: ViewHolder, position: Int, data: T) {

    }

    open protected fun onMyBindHeadViewHolder(holder: ViewHolder) {

    }

    open protected fun onMyCreateOtherViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        return null
    }

    open protected fun onMyBindOtherViewHolder(holder: ViewHolder, position: Int, data: T) {

    }


    companion object {
        val LAYOUT_ORDINARY = 1

    }

}

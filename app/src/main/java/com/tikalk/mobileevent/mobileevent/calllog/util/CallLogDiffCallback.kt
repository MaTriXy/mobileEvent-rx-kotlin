package com.tikalk.mobileevent.mobileevent.calllog.util

import android.support.v7.util.DiffUtil
import com.tikalk.mobileevent.mobileevent.data.CallLogDao

class CallLogDiffCallback(
        val oldList: List<CallLogDao>,
        val newList: List<CallLogDao>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].number.equals(newList[newItemPosition].number)
    }
}
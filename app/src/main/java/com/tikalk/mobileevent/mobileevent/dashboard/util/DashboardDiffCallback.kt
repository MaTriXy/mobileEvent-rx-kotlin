package com.tikalk.mobileevent.mobileevent.dashboard.util

import android.support.v7.util.DiffUtil
import com.tikalk.mobileevent.mobileevent.dashboard.data.DashboardItem

class DashboardDiffCallback(
        val oldList: List<DashboardItem>,
        val newList: List<DashboardItem>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].type == newList[newItemPosition].type
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].count.equals(newList[newItemPosition].count)
    }
}
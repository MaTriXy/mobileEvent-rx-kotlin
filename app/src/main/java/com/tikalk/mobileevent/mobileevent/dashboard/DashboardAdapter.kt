package com.tikalk.mobileevent.mobileevent.dashboard

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tikalk.mobileevent.mobileevent.R
import com.tikalk.mobileevent.mobileevent.dashboard.data.DashboardItem
import com.tikalk.mobileevent.mobileevent.dashboard.util.DashboardDiffCallback
import com.tikalk.mobileevent.mobileevent.util.setDrawableLeft
import com.tikalk.mobileevent.mobileevent.util.toCompoundDuration

class DashboardAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var dataset: List<DashboardItem>

    init {
        dataset = emptyList<DashboardItem>()
    }

    fun setItems(items: List<DashboardItem>) {
        val diff = DiffUtil.calculateDiff(
                DashboardDiffCallback(dataset, items), false)
        dataset = items
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_dashboard, parent, false)
        return DashBoardHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val model = dataset[position]

        val h = holder as DashBoardHolder
        when (model.type) {
            DashboardItem.TYPE_TOTAL -> {
                h.title.setDrawableLeft(holder.itemView.context, R.drawable.ic_call_all)
                h.title.text = "Total Calls"
            }
            DashboardItem.TYPE_INCOMING -> {
                h.title.setDrawableLeft(holder.itemView.context, R.drawable.ic_call_received)
                h.title.text = "Incoming Calls"
            }
            DashboardItem.TYPE_OUTGOING -> {
                h.title.setDrawableLeft(holder.itemView.context, R.drawable.ic_call_made)
                h.title.text = "Outgoing Calls"
            }
            DashboardItem.TYPE_MISSED -> {
                h.title.setDrawableLeft(holder.itemView.context, R.drawable.ic_call_missed)
                h.title.text = "Missed Calls"
            }
        }

        h.counter.text = model.count.toString()
        h.duration.text = model.duration.toCompoundDuration()
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class DashBoardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        val counter: TextView
        val duration: TextView

        init {
            title = itemView.findViewById<TextView>(R.id.dashboard_item_title)
            counter = itemView.findViewById<TextView>(R.id.dashboard_item_counter)
            duration = itemView.findViewById<TextView>(R.id.dashboard_item_duration)
        }
    }
}
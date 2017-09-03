package com.tikalk.mobileevent.mobileevent.dashboard

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.tikalk.mobileevent.mobileevent.R
import com.tikalk.mobileevent.mobileevent.dashboard.data.DashboardItem
import com.tikalk.mobileevent.mobileevent.dashboard.util.DashboardDiffCallback
import com.tikalk.mobileevent.mobileevent.util.click
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

        return DashBoardHolder(view).click { pos, type ->
            val item = dataset.get(pos)
            Toast.makeText(parent.context, "Type is : " + item.type, Toast.LENGTH_SHORT).show()
            //TODO do other stuff here
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val model = dataset[position]

        val h = holder as DashBoardHolder
        val context = holder.counter.context
        when (model.type) {
            DashboardItem.TYPE_TOTAL -> {
                h.title.setDrawableLeft(holder.itemView.context, R.drawable.ic_call_all)
	    h.title.text = context.getString(R.string.total_calls_lbl)
            }
            DashboardItem.TYPE_INCOMING -> {
                h.title.setDrawableLeft(holder.itemView.context, R.drawable.ic_call_received)
	    h.title.text = context.getString(R.string.incoming_calls_lbl)
            }
            DashboardItem.TYPE_OUTGOING -> {
                h.title.setDrawableLeft(holder.itemView.context, R.drawable.ic_call_made)
	    h.title.text = context.getString(R.string.outgoing_calls_lbl)
            }
            DashboardItem.TYPE_MISSED -> {
                h.title.setDrawableLeft(holder.itemView.context, R.drawable.ic_call_missed)
	    h.title.text = context.getString(R.string.missed_calls_lbl)
            }
        }

        h.counter.text = model.count.toString()
        h.duration.text = model.duration.toCompoundDuration()
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class DashBoardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.dashboard_item_title)
        val counter: TextView = itemView.findViewById(R.id.dashboard_item_counter)
        val duration: TextView = itemView.findViewById(R.id.dashboard_item_duration)

    }
}
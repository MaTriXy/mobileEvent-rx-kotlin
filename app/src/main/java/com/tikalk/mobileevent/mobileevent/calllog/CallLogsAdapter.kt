package com.tikalk.mobileevent.mobileevent.calllog

import android.provider.CallLog
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tikalk.mobileevent.mobileevent.R
import com.tikalk.mobileevent.mobileevent.calllog.util.CallLogDiffCallback
import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import java.text.SimpleDateFormat
import java.util.*

class CallLogsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var dataset: List<CallLogDao>

    init {
        dataset = emptyList<CallLogDao>()
    }

    fun setItems(items: List<CallLogDao>) {
        val diff = DiffUtil.calculateDiff(
                CallLogDiffCallback(dataset, items), false)
        dataset = items
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_calllog, parent, false)
        return CallLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val model = dataset[position]

        val h = holder as CallLogViewHolder
        h.name.text = model.name
        h.phone.text = formatNumber(model.number)
        h.type.text = formatType(model.type)
        h.date.text = formatDate(model.date)

        h.icon.setImageResource(getIconResId(model.type))
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    inner class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView
        val phone: TextView
        val type: TextView
        val date: TextView
        val icon: ImageView

        init {
            name = itemView.findViewById<TextView>(R.id.card_name)
            phone = itemView.findViewById<TextView>(R.id.card_number)
            type = itemView.findViewById<TextView>(R.id.card_type)
            date = itemView.findViewById<TextView>(R.id.card_date)
            icon = itemView.findViewById<ImageView>(R.id.card_icon)
        }
    }

    companion object {
        val pattern: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

        fun formatDate(timestamp: Long): String? {
            return pattern.format(timestamp)
        }

        fun formatNumber(number: String?): String? {
            when (number) {
                "-1" -> return "Unknown"
                else -> return number
            }
        }

        fun formatType(type: Int): String? {
            var typeString = "Unknown"

            when (type) {
                CallLog.Calls.INCOMING_TYPE -> typeString = "Incoming"
                CallLog.Calls.OUTGOING_TYPE -> typeString = "Outgoing"
                CallLog.Calls.MISSED_TYPE -> typeString = "Missed"
                CallLog.Calls.VOICEMAIL_TYPE -> typeString = "Voicemail"
                CallLog.Calls.REJECTED_TYPE -> typeString = "Rejected"
                CallLog.Calls.BLOCKED_TYPE -> typeString = "Blocked"
            }

            return typeString
        }

        fun getIconResId(type: Int): Int {
            when (type) {
                CallLog.Calls.INCOMING_TYPE -> return R.drawable.ic_call_received
                CallLog.Calls.OUTGOING_TYPE -> return R.drawable.ic_call_made
                CallLog.Calls.MISSED_TYPE -> return R.drawable.ic_call_missed
                else -> return 0
            }
        }
    }
}
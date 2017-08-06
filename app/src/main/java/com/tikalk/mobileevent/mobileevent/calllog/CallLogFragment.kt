package com.tikalk.mobileevent.mobileevent.calllog

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tikalk.mobileevent.mobileevent.R
import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import timber.log.Timber

/**
 * Created by ronelg on 8/6/17.
 */
class CallLogFragment : Fragment(), CallLogContract.View {

  override var presenter: CallLogContract.Presenter? = null

  lateinit var adapter:CallLogsAdapter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {

    val root = inflater.inflate(R.layout.calllog_fragment, container, false)

    with(root) {
      val recyclerView = findViewById<RecyclerView>(R.id.list)!!
      setupRecyclerView(recyclerView)
    }

    return root
  }

  override fun onResume() {
    super.onResume()
    presenter?.subscribe()
  }

  override fun onPause() {
    super.onPause()
    presenter?.unsubscribe()
  }

  override fun setLoadingIndicator(active: Boolean) {

  }

  override fun showCallLogs(calllogs: List<CallLogDao>) {
    Timber.i(calllogs.toString())
    adapter.setItems(calllogs)
  }

  private fun setupRecyclerView(recyclerView: RecyclerView) {
    adapter = CallLogsAdapter()

    val padding = resources.getDimensionPixelOffset(R.dimen.list_item_padding)

    recyclerView.addItemDecoration(SpaceItemDecoration(
        padding,padding/2))
    recyclerView.adapter = adapter
  }

  class CallLogsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var dataset: List<CallLogDao>

    init {
      dataset = emptyList<CallLogDao>()
    }

    fun setItems(items: List<CallLogDao>) {
      val diff = DiffUtil.calculateDiff(CallogDiff(dataset, items), false)
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
      h.phone.text = model.number
      h.type.text = model.type.toString()
      h.date.text = model.date.toString()
    }

    override fun getItemCount(): Int {
      return dataset.size
    }

    inner class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      val name: TextView
      val phone: TextView
      val type: TextView
      val date: TextView

      init {
        name = itemView.findViewById<TextView>(R.id.card_name)
        phone = itemView.findViewById<TextView>(R.id.card_number)
        type = itemView.findViewById<TextView>(R.id.card_type)
        date = itemView.findViewById<TextView>(R.id.card_date)
      }
    }
  }

  class CallogDiff(
      val oldList: List<CallLogDao>,
      val newList: List<CallLogDao>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
      return oldList.size
    }

    override fun getNewListSize(): Int {
      return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return oldList[oldItemPosition].number.equals(newList[newItemPosition].number)
    }

  }

  class SpaceItemDecoration(val left: Int,val top: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView,
        state: RecyclerView.State?) {
      super.getItemOffsets(outRect, view, parent, state)

      outRect.top = top
      outRect.left = left
      outRect.right = left

      if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
          outRect.bottom = top
      }
    }
  }


  companion object {
    fun newInstance(): CallLogFragment {
      return CallLogFragment()
    }
  }

}
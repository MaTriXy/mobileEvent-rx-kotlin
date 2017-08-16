package com.tikalk.mobileevent.mobileevent.calllog

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tikalk.mobileevent.mobileevent.R
import com.tikalk.mobileevent.mobileevent.calllog.util.SpaceItemDecoration
import com.tikalk.mobileevent.mobileevent.data.CallLogDao
import timber.log.Timber

/**
 * Created by ronelg on 8/6/17.
 */
class CallLogFragment : Fragment(), CallLogContract.View {

  override var presenter: CallLogContract.Presenter? = null

  lateinit var adapter: CallLogsAdapter

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
        padding, padding / 2))
    recyclerView.adapter = adapter
  }

  companion object {
    fun newInstance(): CallLogFragment {
      return CallLogFragment()
    }
  }
}